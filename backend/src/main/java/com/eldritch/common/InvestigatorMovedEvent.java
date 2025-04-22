package com.eldritch.common;

import java.util.UUID;

public class InvestigatorMovedEvent implements GameEvent {
    private final UUID eventId;
    private final UUID originId;
    private final UUID investigatorId;
    private final UUID destinationId;

    public InvestigatorMovedEvent(UUID originId, UUID investigatorId, UUID destinationId) {
        this.eventId = UUID.randomUUID();
        this.originId = originId;
        this.investigatorId = investigatorId;
        this.destinationId = destinationId;
    }

    @Override
    public String getEventType() {
        return "INVESTIGATOR_MOVED";
    }

    @Override
    public InvestigatorMovePayload getPayload() {
        return new InvestigatorMovePayload(investigatorId, destinationId);
    }

    @Override
    public UUID getOriginId() {
        return originId;
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    public UUID getInvestigatorId() {
        return investigatorId;
    }

    public UUID getDestinationId() {
        return destinationId;
    }

    public static class InvestigatorMovePayload {
        private final UUID investigatorId;
        private final UUID destinationId;

        public InvestigatorMovePayload(UUID investigatorId, UUID destinationId) {
            this.investigatorId = investigatorId;
            this.destinationId = destinationId;
        }

        public UUID getInvestigatorId() {
            return investigatorId;
        }

        public UUID getDestinationId() {
            return destinationId;
        }
    }
}