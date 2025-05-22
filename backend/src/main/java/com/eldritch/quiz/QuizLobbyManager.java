package com.eldritch.quiz;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import jakarta.annotation.PreDestroy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Service
public class QuizLobbyManager implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LogManager.getLogger(QuizLobbyManager.class);

    private final ConcurrentHashMap<String, QuizLobby> activeLobbies = new ConcurrentHashMap<>();
    private QuizLobby availableQuizLobby;
    private final SimpMessagingTemplate messagingTemplate;
    private final FirestoreService firestoreService;

    @Autowired
    public QuizLobbyManager(SimpMessagingTemplate messagingTemplate, FirestoreService firestoreService) {
        this.messagingTemplate = messagingTemplate;
        this.firestoreService = firestoreService;
        restoreLobbiesFromFirestore();
        getAvailableLobby();
    }

    private void restoreLobbiesFromFirestore() {
        try {
            // Query all lobbies from Firestore
            ApiFuture<QuerySnapshot> query = firestoreService.getDb()
                    .collection("quizLobbies")
                    .get();

            QuerySnapshot querySnapshot = query.get();
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                Map<String, Object> lobbyData = document.getData();
                if (lobbyData != null) {
                    QuizLobby quizLobby = restoreLobby(lobbyData);
                    if (quizLobby == null) continue;
                    if (quizLobby.getGameInstance() != null && !quizLobby.getGameInstance().isQuizRunning() && !quizLobby.getGameInstance().isQuizFinished()) {
                        availableQuizLobby = quizLobby;
                    } else {
                        activeLobbies.put(document.getId(), quizLobby);
                    }
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to restore lobbies from Firestore", e);
        }
    }
    private QuizLobby restoreLobby(String lobbyId) {
        Map<String, Object> lobbyState = firestoreService.loadLobbyState(lobbyId);
        return lobbyState == null ? null : restoreLobby(lobbyState);
    }


    private QuizLobby restoreLobby(Map<String, Object> lobbyData) {
        try {
            // Create new lobby with the same ID
            String id = (String) lobbyData.get("id");
            QuizLobby lobby = new QuizLobby(messagingTemplate, id);
            // Restore players
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> playersData = (List<Map<String, Object>>) lobbyData.get("players");
            if (playersData != null) {
                for (Map<String, Object> playerData : playersData) {
                    QuizPlayer player = new QuizPlayer(
                            (String) playerData.get("id"),
                            (String) playerData.get("nickname"),
                            ((Number) playerData.get("score")).intValue(),
                            (boolean) playerData.get("active")
                    );
                    lobby.addPlayer(player);
                }
            }

            // Restore game state
            @SuppressWarnings("unchecked")
            Map<String, Object> gameState = (Map<String, Object>) lobbyData.get("gameState");
            if (gameState != null) {
                lobby.getGameInstance().restoreState(gameState);
            }
            logger.info("Lobby " + lobby.getId() + " restored with players: " + lobby.getPlayers());
            return lobby;
        } catch (Exception e) {
            logger.error("", e);
            firestoreService.deleteLobbyState((String) lobbyData.get("id"));
            return null;
        }
    }

    public synchronized QuizLobby getAvailableLobby() {
        if (availableQuizLobby == null) {
            availableQuizLobby = new QuizLobby(this.messagingTemplate);
        }
        return availableQuizLobby;
    }

    public QuizLobby getLobby(String lobbyId) {
        if (availableQuizLobby != null && lobbyId.equals(availableQuizLobby.getId())) {
            return availableQuizLobby;
        }
        QuizLobby lobby = activeLobbies.get(lobbyId);
        if (lobby == null) {
            lobby = restoreLobby(lobbyId);
        }
        return lobby;
    }

    public void lobbyStartedGame() {
        activeLobbies.put(availableQuizLobby.getId(), availableQuizLobby);
        availableQuizLobby = null;
    }

    public QuizLobby join(String nickname) {
        QuizLobby ret = getAvailableLobby();
        ret.addPlayer(nickname);

        if (ret.isFull()) {
            lobbyStartedGame();
        }
        return ret;
    }

    public QuizLobby find(String quizPlayerId) {
        if (availableQuizLobby != null && availableQuizLobby.hasPlayer(quizPlayerId)) {
            return availableQuizLobby;
        }

        Iterator<Map.Entry<String, QuizLobby>> iterator = activeLobbies.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, QuizLobby> entry = iterator.next();
            QuizLobby lobby = entry.getValue();

            if (lobby.getGameInstance().isQuizFinished()) {
                firestoreService.deleteLobbyState(lobby.getId());
                iterator.remove();
            } else if (lobby.hasPlayer(quizPlayerId)) {
                return lobby;
            }
        }

        return null;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        saveAllLobbies();
    }
    public void saveAllLobbies() {
        logger.debug("Saving all lobbies");
        // Save the available lobby if it exists
        if (availableQuizLobby != null && !availableQuizLobby.isEmpty()) {
            saveLobbyState(availableQuizLobby);
        }

        // Save all active lobbies
        for (QuizLobby lobby : activeLobbies.values()) {
            saveLobbyState(lobby);
        }

        logger.info("Saved all lobby states on shutdown");
    }

    private void saveLobbyState(QuizLobby lobby) {
        try {
            Map<String, Object> state = new HashMap<>();
            state.put("id", lobby.getId());

            // Save players
            List<Map<String, Object>> playersList = new ArrayList<>();
            for (QuizPlayer player : lobby.getPlayers()) {
                playersList.add(player.toMap());
            }
            state.put("players", playersList);

            // Save game state
            state.put("gameState", lobby.getGameInstance().getCurrentState());

            if (lobby.getGameInstance().isQuizFinished()) {
                firestoreService.deleteLobbyState(lobby.getId());
                logger.info("Lobby " + lobby.getId() + " removed with players: " + lobby.getPlayers());
            } else {
                firestoreService.saveLobbyState(lobby.getId(), state);
                logger.info("Lobby " + lobby.getId() + " saved with players: " + lobby.getPlayers());
            }

        } catch (Exception e) {
            logger.error("Failed to save lobby state for lobby: " + lobby.getId(), e);
        }
    }


    public void deleteAllLobbies() {
        activeLobbies.clear();
        availableQuizLobby = null;
        firestoreService.deleteAllLobbies();
    }
}