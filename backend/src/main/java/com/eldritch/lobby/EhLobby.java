package com.eldritch.lobby;

import com.eldritch.logic.EhStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class EhLobby {
    private final String id;
    private final String gameName;
    private final Map<String, EhAgent> agents = new LinkedHashMap<>();
    private final EhServer server;
    private EhAgent lastJoined;
    private String ancientId;

    public EhLobby(SimpMessagingTemplate messagingTemplate, String gameName) {
        this.id = UUID.randomUUID().toString();  // Generate ID in constructor
        this.gameName = gameName;
        this.server = new EhServer(messagingTemplate, this.id);
    }

    // Remove setId() since ID is now final and set in constructor
    public String getId() {
        return id;
    }

    public EhServer getServer() {
        return server;
    }

    public synchronized EhAgent addAgent(EhAgent agent) {
        lastJoined = server.addAgent(agent);
        agents.put(lastJoined.getId(), lastJoined);
        return lastJoined;
    }


    public synchronized void removeAgent(String userId) {
        server.removeAgent(userId);
        agents.remove(userId);
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

    public String getAncientId() {
        return ancientId;
    }

    public void setAncientId(String ancientId) {
        this.ancientId = ancientId;
    }

    public LobbyInfo info() {
        EhStatus status = EhStatus.LOBBY;
        if (server.isStarted()) {
            status = server.getStatus();
        }
        return new LobbyInfo(id,
                agents.values().stream().map(agent -> new AgentInfo(agent.getId(), agent.getNickname(), agent.getInvestigatorId(), agent.isMaster())).toList(),
                ancientId,
                gameName, status);
    }

    public void terminate() {
        server.stopServer();
    }

    public void start() {
        server.startServer();
    }
}