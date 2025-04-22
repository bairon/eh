package com.eldritch.engine;

import com.eldritch.common.*;

public class UpkeepPhaseHandler implements EventListener {
    private final GameState gameState;
    private final EventBus eventBus;

    public UpkeepPhaseHandler(GameState gameState, EventBus eventBus) {
        this.gameState = gameState;
        this.eventBus = eventBus;
    }

    @Override
    public void handleEvent(GameEvent event) {
        // 1. Refresh investigator resources
        gameState.getInvestigators().forEach(investigator -> {
            //investigator.setActions(2); // Reset actions per turn
        });

        // 2. Move to next phase
        eventBus.publish(new GenericGameEvent("UPKEEP_COMPLETED"));
        eventBus.publish(new GenericGameEvent("ACTIONS_STARTED"));
    }
}