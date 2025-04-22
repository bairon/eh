package com.eldritch.common;

import java.util.UUID;

public class HealthChangedEvent implements GameEvent {
    private final UUID eventId;
    private final UUID originId;
    private final UUID investigatorId;
    private final int delta;

    public HealthChangedEvent(UUID originId, UUID investigatorId, int delta) {
        this.eventId = UUID.randomUUID();
        this.originId = originId;
        this.investigatorId = investigatorId;
        this.delta = delta;
    }

    @Override public String getEventType() { return "HEALTH_CHANGED"; }
    @Override public UUID getOriginId() { return originId; }
    @Override public UUID getEventId() { return eventId; }

    public UUID getInvestigatorId() { return investigatorId; }
    public int getDelta() { return delta; }

    @Override
    public HealthChangedPayload getPayload() {
        return new HealthChangedPayload(investigatorId, delta);
    }

    public static class HealthChangedPayload {
        private final UUID investigatorId;
        private final int delta;

        public HealthChangedPayload(UUID investigatorId, int delta) {
            this.investigatorId = investigatorId;
            this.delta = delta;
        }

        public UUID getInvestigatorId() { return investigatorId; }
        public int getDelta() { return delta; }
    }
}