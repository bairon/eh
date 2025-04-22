package com.eldritch.common;

import java.util.UUID;

// Supporting event classes
public class MythosDrawnEvent implements GameEvent {
    private final UUID eventId = UUID.randomUUID();
    private final UUID cardId;
    private final MythosType mythosType;

    public MythosDrawnEvent(UUID cardId, MythosType mythosType) {
        this.cardId = cardId;
        this.mythosType = mythosType;
    }

    @Override public String getEventType() { return "MYTHOS_DRAWN"; }
    @Override public UUID getOriginId() { return null; } // System event
    @Override public UUID getEventId() { return eventId; }
    @Override public <T> T getPayload(Class<T> type) { return type.cast(this); }
    @Override public Object getPayload() { return this; }

    public UUID getCardId() { return cardId; }
    public MythosType getMythosType() { return mythosType; }
}
