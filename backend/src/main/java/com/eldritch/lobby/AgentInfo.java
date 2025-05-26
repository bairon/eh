package com.eldritch.lobby;

public class AgentInfo {
    private final String id;
    private final String nickname;

    public AgentInfo(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}
