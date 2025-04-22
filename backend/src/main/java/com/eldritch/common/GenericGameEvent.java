package com.eldritch.common;

import java.util.UUID;

public class GenericGameEvent implements GameEvent {
    private final UUID eventId;
    private final String eventType;
    private final UUID originId;
    private final Object payload;

    public GenericGameEvent(String eventType) {
        this.eventId = UUID.randomUUID();
        this.eventType = eventType;
        this.originId = null;
        this.payload = null;
    }

    public GenericGameEvent(String eventType, Object payload) {
        this.eventId = UUID.randomUUID();
        this.eventType = eventType;
        this.originId = null;
        this.payload = payload;
    }

    public GenericGameEvent(String eventType, UUID originId, Object payload) {
        this.eventId = UUID.randomUUID();
        this.eventType = eventType;
        this.originId = originId;
        this.payload = payload;
    }

    @Override public String getEventType() { return eventType; }
    @Override public UUID getOriginId() { return originId; }
    @Override public UUID getEventId() { return eventId; }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getPayload(Class<T> payloadType) {
        return payloadType.isInstance(payload) ? (T) payload : null;
    }

    @Override
    public Object getPayload() {
        return payload;
    }
}