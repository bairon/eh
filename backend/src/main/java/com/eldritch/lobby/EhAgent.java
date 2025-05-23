package com.eldritch.lobby;

import com.eldritch.logic.Interraction;

import java.util.concurrent.CompletableFuture;

public abstract class EhAgent implements InterractionListener {
    protected String id;

    public abstract CompletableFuture<String> interract(Interraction interraction);

    public EhAgent() {
    }

    public EhAgent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
