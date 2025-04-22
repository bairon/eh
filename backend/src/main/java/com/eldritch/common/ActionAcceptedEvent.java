package com.eldritch.common;

import java.util.UUID;

public class ActionAcceptedEvent implements GameEvent {
    private final UUID eventId = UUID.randomUUID();
    private final UUID originalEventId;

    public ActionAcceptedEvent(UUID originalEventId) {
        this.originalEventId = originalEventId;
    }

    @Override public String getEventType() { return "ACTION_ACCEPTED"; }
    @Override public UUID getOriginId() { return originalEventId; }
    @Override public UUID getEventId() { return eventId; }

    @Override
    public <T> T getPayload(Class<T> payloadType) {
        return null; // No payload for acceptance events
    }
}