package com.eldritch.common;

import java.util.UUID;

public class SpellResolvedEvent implements GameEvent {
    private final UUID eventId;
    private final UUID spellId;

    public SpellResolvedEvent(UUID spellId) {
        this.eventId = UUID.randomUUID();
        this.spellId = spellId;
    }

    @Override public String getEventType() { return "SPELL_RESOLVED"; }
    @Override public UUID getEventId() { return eventId; }
    @Override public UUID getOriginId() { return null; } // System-generated event

    public UUID getSpellId() { return spellId; }
}