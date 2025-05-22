package com.eldritch.lobby;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EhLobby {
    private final String id;
    private final ConcurrentHashMap<String, EhAgent> agents = new ConcurrentHashMap<>();
    private final EhServer server;
    private EhAgent lastJoined;

    public EhLobby(SimpMessagingTemplate messagingTemplate) {
        this.id = UUID.randomUUID().toString();  // Generate ID in constructor
        this.server = new EhServer(messagingTemplate, this.id);
    }

    public EhLobby(SimpMessagingTemplate messagingTemplate, String id) {
        this.id = id;
        this.server = new EhServer(messagingTemplate, this.id);
    }

    // Remove setId() since ID is now final and set in constructor
    public String getId() {
        return id;
    }

    public EhServer getServer() {
        return server;
    }

    public Collection<EhAgent> getAgents() {
        return agents.values();
    }

    public synchronized EhAgent addAgent(String nickname) {
        lastJoined = server.addAgent(nickname);
        agents.put(lastJoined.getId(), lastJoined);
        return lastJoined;
    }

    public EhAgent addAgent(EhAgent agent) {
        lastJoined = agent;
        agents.put(lastJoined.getId(), lastJoined);
        return lastJoined;
    }

    public EhAgent getLastJoined() {
        return lastJoined;
    }

    public void removeAgent(String id) {
        agents.remove(id);
    }

    public boolean isFull() {
        return server.isRunning();
    }
    public boolean isEmpty() {
        return agents.isEmpty();
    }

    public boolean hasAgent(String id) {
        return agents.containsKey(id);
    }

    public EhAgent getAgent(String id) {
        return agents.get(id);
    }

    public LobbyInfo info() {
        return new LobbyInfo();
    }
}