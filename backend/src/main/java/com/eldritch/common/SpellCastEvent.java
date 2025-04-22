package com.eldritch.common;

import com.eldritch.engine.Attribute;
import java.util.UUID;

public class SpellCastEvent implements GameEvent {
    private final UUID eventId;
    private final UUID originId;
    private final UUID spellId;
    private final Attribute testAttribute;
    private final int testModifier;

    public SpellCastEvent(UUID spellId, UUID casterId,
                          Attribute testAttribute, int testModifier) {
        this.eventId = UUID.randomUUID();
        this.originId = casterId;
        this.spellId = spellId;
        this.testAttribute = testAttribute;
        this.testModifier = testModifier;
    }

    @Override public String getEventType() { return "SPELL_CAST"; }
    @Override public UUID getOriginId() { return originId; }
    @Override public UUID getEventId() { return eventId; }

    // Type-safe payload access
    public UUID getSpellId() { return spellId; }
    public Attribute getTestAttribute() { return testAttribute; }
    public int getTestModifier() { return testModifier; }

    // Implement payload methods
    @Override
    public <T> T getPayload(Class<T> payloadType) {
        if (payloadType.isAssignableFrom(getClass())) {
            return payloadType.cast(this);
        }
        return null;
    }

    @Override
    public Object getPayload() {
        return this; // Return self as payload
    }
}