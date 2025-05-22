package com.eldritch.lobby;

import com.eldritch.logic.Interraction;

import java.util.concurrent.CompletableFuture;

public abstract class EhAgent implements InterractionListener {
    protected String id;
    protected String nickname;

    public abstract CompletableFuture<String> interract(Interraction interraction);

    public EhAgent() {
    }

    public EhAgent(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
