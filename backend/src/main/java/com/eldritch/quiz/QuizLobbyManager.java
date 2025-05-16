package com.eldritch.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class QuizLobbyManager {
    private final ConcurrentHashMap<String, QuizLobby> activeLobbies = new ConcurrentHashMap<>();
    private QuizLobby availableQuizLobby;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public QuizLobbyManager(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        getAvailableLobby();
    }

    public synchronized QuizLobby getAvailableLobby() {
        if (availableQuizLobby == null) {
            availableQuizLobby = new QuizLobby(this.messagingTemplate);
        }
        return availableQuizLobby;
    }

    public QuizLobby getLobby(String lobbyId) {
        if (availableQuizLobby != null && lobbyId.equals(availableQuizLobby.getId())) return availableQuizLobby;
        return activeLobbies.get(lobbyId);
    }

    public void lobbyStartedGame() {
        activeLobbies.put(availableQuizLobby.getId(), availableQuizLobby);
        availableQuizLobby = null;
    }

    public void cleanupLobby(String lobbyId) {
        activeLobbies.remove(lobbyId);
    }

    public QuizLobby join(String nickname) {
        QuizLobby ret = getAvailableLobby();  // Get current available lobby
        ret.addPlayer(nickname);          // Add player to it
        if (ret.isFull()) {               // Check if it's full
            lobbyStartedGame();           // Move to active if full
        }
        return ret;
    }

    public QuizLobby find(String quizPlayerId, String currentLobbyId) {
        if (availableQuizLobby != null && availableQuizLobby.hasPlayer(quizPlayerId)) return availableQuizLobby;
        for (QuizLobby quizLobby : activeLobbies.values()) {
            if (quizLobby.hasPlayer(quizPlayerId)) return quizLobby;
        }
        return null;
    }
}