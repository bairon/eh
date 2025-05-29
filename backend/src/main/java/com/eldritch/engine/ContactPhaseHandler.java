package com.eldritch.engine;

import com.eldritch.common.*;
import com.eldritch.logic.Investigator;

import java.util.UUID;

public class ContactPhaseHandler implements EventListener {
    private final GameState gameState;
    private final EventBus eventBus;

    public ContactPhaseHandler(GameState gameState, EventBus eventBus) {
        this.gameState = gameState;
        this.eventBus = eventBus;
    }

    @Override
    public void handleEvent(GameEvent event) {
        // Process contacts
        gameState.getInvestigators().forEach(investigator -> {
            if (hasContactOpportunity(investigator)) {
                //processContact(investigator.getId());
            }
        });

        // Move to next phase
        eventBus.publish(new GenericGameEvent("CONTACTS_COMPLETED"));
        eventBus.publish(new GenericGameEvent("MYTHOS_STARTED"));
    }

    private boolean hasContactOpportunity(Investigator investigator) {
        return false;//gameState.getLocation(investigator.getLocationId())
                //.getContacts().size() > 0;
    }

    private void processContact(UUID playerId) {
        // Simulate contact processing
        eventBus.publish(new GenericGameEvent("CONTACT_RESOLVED"));
    }
}