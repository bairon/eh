package com.eldritch.common;

import java.util.UUID;

public class PlayerLeftEvent implements GameEvent {
    private UUID playerId;

    public PlayerLeftEvent(UUID clientId) {
        playerId = clientId;
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
        return null;
    }
    // Implementation
}

