package com.eldritch.quiz;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class QuizLobby {
    private final String id;
    private final ConcurrentHashMap<String, QuizPlayer> players = new ConcurrentHashMap<>();
    private final QuizService gameInstance;
    private QuizPlayer lastJoinedPlayer;

    public QuizLobby(SimpMessagingTemplate messagingTemplate) {
        this.id = UUID.randomUUID().toString();  // Generate ID in constructor
        this.gameInstance = new QuizService(messagingTemplate, this.id);
    }

    public QuizLobby(SimpMessagingTemplate messagingTemplate, String id) {
        this.id = id;  // Generate ID in constructor
        this.gameInstance = new QuizService(messagingTemplate, this.id);
    }


    // Remove setId() since ID is now final and set in constructor
    public String getId() {
        return id;
    }

    public QuizService getGameInstance() {
        return gameInstance;
    }

    public Collection<QuizPlayer> getPlayers() {
        return players.values();
    }

    public QuizPlayer addPlayer(String nickname) {
        lastJoinedPlayer = gameInstance.addPlayer(nickname);
        players.put(lastJoinedPlayer.getId(), lastJoinedPlayer);
        return lastJoinedPlayer;
    }

    public QuizPlayer addPlayer(QuizPlayer player) {
        lastJoinedPlayer = player;
        players.put(lastJoinedPlayer.getId(), lastJoinedPlayer);
        return lastJoinedPlayer;
    }

    public QuizPlayer getLastJoinedPlayer() {
        return lastJoinedPlayer;
    }

    public void removePlayer(String playerId) {
        players.remove(playerId);
    }

    public boolean isFull() {
        return gameInstance.isQuizRunning();
    }
    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean hasPlayer(String quizPlayerId) {
        return players.containsKey(quizPlayerId);
    }

    public QuizPlayer getPlayer(String quizPlayerId) {
        return players.get(quizPlayerId);
    }
}