package com.eldritch.common;

import java.util.UUID;

public class MythosResolvedEvent implements GameEvent {
    private final UUID eventId = UUID.randomUUID();
    private final UUID cardId;
    private final MythosType mythosType;
    private final MythosEffect effect;
    private final int doomThreshold;
    private final boolean immediate;

    public MythosResolvedEvent(UUID cardId, MythosType mythosType,
                               MythosEffect effect, int doomThreshold,
                               boolean immediate) {
        this.cardId = cardId;
        this.mythosType = mythosType;
        this.effect = effect;
        this.doomThreshold = doomThreshold;
        this.immediate = immediate;
    }

    @Override public String getEventType() { return "MYTHOS_RESOLVED"; }
    @Override public UUID getOriginId() { return null; } // System event
    @Override public UUID getEventId() { return eventId; }
    @Override public <T> T getPayload(Class<T> type) { return type.cast(this); }
    @Override public Object getPayload() { return this; }

    public UUID getCardId() { return cardId; }
    public MythosType getMythosType() { return mythosType; }
    public MythosEffect getEffect() { return effect; }
    public int getDoomThreshold() { return doomThreshold; }
    public boolean isImmediate() { return immediate; }
}
