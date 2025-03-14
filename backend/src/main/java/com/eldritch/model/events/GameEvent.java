package com.eldritch.model.events;

public abstract class GameEvent {
    private String description;

    public GameEvent(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}