package com.eldritch.engine;

import com.eldritch.common.*;
import com.eldritch.client.ClientConnection;

import java.util.List;
import java.util.UUID;

public class ActionPhaseHandler implements EventListener {
    private final GameState gameState;
    private final EventBus eventBus;
    private int actionsProcessed = 0;

    public ActionPhaseHandler(GameState gameState, EventBus eventBus) {
        this.gameState = gameState;
        this.eventBus = eventBus;
    }

    @Override
    public void handleEvent(GameEvent event) {
        if (allActionsCompleted()) {
            eventBus.publish(new GenericGameEvent("ACTIONS_COMPLETED"));
            eventBus.publish(new GenericGameEvent("CONTACTS_STARTED"));
            return;
        }

        Investigator current = getCurrentInvestigator();
        //if (current.getActions() > 0) {
            GameAction chosenAction = requestPlayerAction(current.getId());
            processPlayerAction(current.getId(), chosenAction);
        //} else {
            actionsProcessed++;
            handleEvent(event); // Process next investigator
        //}
    }

    private Investigator getCurrentInvestigator() {
        List<Investigator> investigators = gameState.getInvestigators();
        return investigators.get(actionsProcessed % investigators.size());
    }

    private GameAction requestPlayerAction(UUID playerId) {
        // In a real implementation, this would use the ClientConnection
        // For now, we'll simulate player input
        System.out.println("Player " + playerId + ", choose your action:");
        System.out.println("1. Move");
        System.out.println("2. Rest");
        // ... other actions

        // Return a simulated action
        return new MoveAction(playerId, null);
    }

    private void processPlayerAction(UUID playerId, GameAction action) {
        action.execute(gameState);
        actionsProcessed++;
        eventBus.publish(new GenericGameEvent("GAME_STATE_UPDATED"));
    }

    private boolean allActionsCompleted() {
        return actionsProcessed >= gameState.getInvestigators().size() * 2;
    }
}