package com.eldritch.common;

import java.util.UUID;

public class DoomChangedEvent implements GameEvent {
    private final UUID eventId;
    private final UUID originId;
    private final int delta;

    public DoomChangedEvent(UUID originId, int delta) {
        this.eventId = UUID.randomUUID();
        this.originId = originId;
        this.delta = delta;
    }

    @Override public String getEventType() { return "DOOM_CHANGED"; }
    @Override public UUID getOriginId() { return originId; }
    @Override public UUID getEventId() { return eventId; }

    public int getDelta() { return delta; }

    @Override
    public DoomChangedPayload getPayload() {
        return new DoomChangedPayload(delta);
    }

    public static class DoomChangedPayload {
        private final int delta;

        public DoomChangedPayload(int delta) {
            this.delta = delta;
        }

        public int getDelta() { return delta; }
    }
}