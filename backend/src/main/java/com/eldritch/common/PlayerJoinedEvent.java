package com.eldritch.common;

import java.util.UUID;

public class PlayerJoinedEvent implements GameEvent {
    private UUID playerId;
    private String playerName;

    public PlayerJoinedEvent(UUID playerId) {
        this.playerId = playerId;
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
