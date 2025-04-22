package com.eldritch.common;

import java.util.UUID;

public class SanityChangedEvent implements GameEvent {
    private final UUID eventId;
    private final UUID originId;
    private final UUID investigatorId;
    private final int delta;

    public SanityChangedEvent(UUID originId, UUID investigatorId, int delta) {
        this.eventId = UUID.randomUUID();
        this.originId = originId;
        this.investigatorId = investigatorId;
        this.delta = delta;
    }

    @Override public String getEventType() { return "SANITY_CHANGED"; }
    @Override public UUID getOriginId() { return originId; }
    @Override public UUID getEventId() { return eventId; }

    public UUID getInvestigatorId() { return investigatorId; }
    public int getDelta() { return delta; }

    @Override
    public SanityChangedPayload getPayload() {
        return new SanityChangedPayload(investigatorId, delta);
    }

    public static class SanityChangedPayload {
        private final UUID investigatorId;
        private final int delta;

        public SanityChangedPayload(UUID investigatorId, int delta) {
            this.investigatorId = investigatorId;
            this.delta = delta;
        }

        public UUID getInvestigatorId() { return investigatorId; }
        public int getDelta() { return delta; }
    }
}