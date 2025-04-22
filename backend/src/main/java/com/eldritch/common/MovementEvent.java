package com.eldritch.common;

import java.util.UUID;

public class MovementEvent implements GameEvent {
    public MovementEvent(UUID investigatorId, UUID destinationId) {
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
}
