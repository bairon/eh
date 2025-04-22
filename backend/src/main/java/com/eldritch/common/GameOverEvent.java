package com.eldritch.common;

import java.util.UUID;

public class GameOverEvent implements GameEvent {
    private final UUID eventId = UUID.randomUUID();
    private final String result;

    public GameOverEvent(String result) {
        this.result = result;
    }

    @Override public String getEventType() { return "GAME_OVER"; }
    @Override public UUID getOriginId() { return null; }
    @Override public UUID getEventId() { return eventId; }
    @Override public Object getPayload() { return result; }
}




