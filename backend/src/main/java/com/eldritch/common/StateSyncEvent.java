package com.eldritch.common;

import com.eldritch.engine.GameStateSnapshot;

import java.util.UUID;

public class StateSyncEvent implements GameEvent {
    private GameStateSnapshot snapshot;

    public StateSyncEvent(GameStateSnapshot snapshot) {
        this.snapshot = snapshot;
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

