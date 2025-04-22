package com.eldritch.common;

import java.util.UUID;

public class CardPlayedEvent implements GameEvent {
    private final UUID eventId = UUID.randomUUID();
    private final UUID cardId;
    private final UUID investigatorId;

    public CardPlayedEvent(UUID cardId, UUID investigatorId) {
        this.cardId = cardId;
        this.investigatorId = investigatorId;
    }

    @Override public String getEventType() { return "CARD_PLAYED"; }
    @Override public UUID getOriginId() { return investigatorId; }
    @Override public UUID getEventId() { return eventId; }

    @Override
    public <T> T getPayload(Class<T> payloadType) {
        if (payloadType.equals(UUID.class)) {
            return payloadType.cast(cardId);
        }
        return null;
    }

    @Override
    public Object getPayload() {
        return cardId;
    }

    public UUID getCardId() {
        return cardId;
    }

    public UUID getInvestigatorId() {
        return investigatorId;
    }
}