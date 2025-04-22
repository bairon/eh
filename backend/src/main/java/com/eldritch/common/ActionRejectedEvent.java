package com.eldritch.common;

import java.util.UUID;

public class ActionRejectedEvent implements GameEvent {
    private final UUID eventId = UUID.randomUUID();
    private final UUID originalEventId;
    private final String reason;

    public ActionRejectedEvent(UUID originalEventId, String reason) {
        this.originalEventId = originalEventId;
        this.reason = reason;
    }

    @Override public String getEventType() { return "ACTION_REJECTED"; }
    @Override public UUID getOriginId() { return originalEventId; }
    @Override public UUID getEventId() { return eventId; }

    @Override
    public <T> T getPayload(Class<T> payloadType) {
        if (payloadType.equals(String.class)) {
            return payloadType.cast(reason);
        }
        return null;
    }

}