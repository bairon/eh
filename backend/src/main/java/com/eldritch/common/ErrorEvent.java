package com.eldritch.common;

import java.util.UUID;

public class ErrorEvent implements GameEvent {
    private final String message;
    private final UUID eventId = UUID.randomUUID();

    public ErrorEvent(String message) {
        this.message = message;
    }

    @Override
    public String getEventType() {
        return "";
    }

    @Override
    public Object getPayload() {
        return null;
    }

    @Override
    public UUID getOriginId() {
        return null;
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    // Implement GameEvent methods...
}