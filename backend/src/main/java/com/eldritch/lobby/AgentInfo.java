package com.eldritch.lobby;

public class AgentInfo {
    private final String id;
    private final String nickname;
    private final String investigatorId;
    private final boolean master;

    public AgentInfo(String id, String nickname, String investigatorId, boolean master) {
        this.id = id;
        this.nickname = nickname;
        this.investigatorId = investigatorId;
        this.master = master;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getInvestigatorId() {
        return investigatorId;
    }

    public boolean isMaster() {
        return master;
    }
}
