package com.eldritch.engine;

import com.eldritch.common.*;

public class MythosPhaseHandler implements EventListener {
    private final GameState gameState;
    private final EventBus eventBus;

    public MythosPhaseHandler(GameState gameState, EventBus eventBus) {
        this.gameState = gameState;
        this.eventBus = eventBus;
    }

    @Override
    public void handleEvent(GameEvent event) {
        // Resolve mythos effects
        resolveMythosEffects();

        // Move to next round
        eventBus.publish(new GenericGameEvent("MYTHOS_COMPLETED"));
        eventBus.publish(new GenericGameEvent("UPKEEP_STARTED"));
    }

    private void resolveMythosEffects() {
        // Implement mythos card resolution
        eventBus.publish(new GenericGameEvent("GAME_STATE_UPDATED"));
    }
}