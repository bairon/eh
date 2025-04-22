package com.eldritch.common;

import com.eldritch.engine.GamePhase;

import java.util.UUID;

public class PhaseStartedEvent implements GameEvent {
    private final UUID eventId = UUID.randomUUID();
    private final GamePhase phase;

    public PhaseStartedEvent(GamePhase phase) {
        this.phase = phase;
    }

    @Override public String getEventType() { return "PHASE_STARTED"; }
    @Override public UUID getOriginId() { return null; } // System event
    @Override public UUID getEventId() { return eventId; }
    @Override public Object getPayload() { return phase; }

    public GamePhase getPhase() { return phase; }
}