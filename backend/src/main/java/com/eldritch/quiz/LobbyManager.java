package com.eldritch.quiz;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LobbyManager {
    private final ConcurrentHashMap<String, Lobby> activeLobbies = new ConcurrentHashMap<>();
    private Lobby availableLobby;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectProvider<Lobby> lobbyProvider;

    @Autowired
    public LobbyManager(SimpMessagingTemplate messagingTemplate,
                        ObjectProvider<Lobby> lobbyProvider) {
        this.messagingTemplate = messagingTemplate;
        this.lobbyProvider = lobbyProvider;
        getAvailableLobby();
    }

    public synchronized Lobby getAvailableLobby() {
        if (availableLobby == null) {
            availableLobby = lobbyProvider.getObject(this.messagingTemplate, UUID.randomUUID().toString());
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
        Lobby ret = availableLobby;
        availableLobby.addPlayer(nickname);
        if (availableLobby.isFull()) {
            lobbyStartedGame();
        }
        return ret;
    }

}