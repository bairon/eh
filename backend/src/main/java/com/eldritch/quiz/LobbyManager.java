package com.eldritch.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class LobbyManager {
    private final ConcurrentHashMap<String, Lobby> activeLobbies = new ConcurrentHashMap<>();
    private Lobby availableLobby;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public LobbyManager(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        getAvailableLobby();
    }

    public synchronized Lobby getAvailableLobby() {
        if (availableLobby == null) {
            availableLobby = new Lobby(this.messagingTemplate);
        }
        return availableLobby;
    }

    public Lobby getLobby(String lobbyId) {
        if (availableLobby != null && lobbyId.equals(availableLobby.getId())) return availableLobby;
        return activeLobbies.get(lobbyId);
    }

    public void lobbyStartedGame() {
        activeLobbies.put(availableLobby.getId(), availableLobby);
        availableLobby = null;
    }

    public void cleanupLobby(String lobbyId) {
        activeLobbies.remove(lobbyId);
    }

    public Lobby join(String nickname) {
        Lobby ret = getAvailableLobby();  // Get current available lobby
        ret.addPlayer(nickname);          // Add player to it
        if (ret.isFull()) {               // Check if it's full
            lobbyStartedGame();           // Move to active if full
        }
        return ret;
    }

    public Lobby find(String quizPlayerId, String currentLobbyId) {
        if (availableLobby != null && availableLobby.hasPlayer(quizPlayerId)) return availableLobby;
        for (Lobby lobby : activeLobbies.values()) {
            if (lobby.hasPlayer(quizPlayerId)) return lobby;
        }
        return null;
    }
}