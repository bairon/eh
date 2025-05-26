package com.eldritch.lobby;

import com.eldritch.logic.EhStatus;

import java.util.List;

public class LobbyInfo {

    private final String id;
    private final String gameName;
    private final List<AgentInfo> agents;
    private final String ancientId;
    private final EhStatus status;

    public LobbyInfo(String id, List<AgentInfo> agents, String ancientId, String gameName, EhStatus status) {
        this.id = id;
        this.agents = agents;
        this.ancientId = ancientId;
        this.gameName = gameName;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getGameName() {
        return gameName;
    }

    public List<AgentInfo> getAgents() {
        return agents;
    }

    public String getAncientId() {
        return ancientId;
    }

    public EhStatus getStatus() {
        return status;
    }
}
