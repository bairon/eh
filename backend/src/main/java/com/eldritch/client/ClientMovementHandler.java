package com.eldritch.client;

import com.eldritch.common.MovementEvent;
import com.eldritch.common.EventListener;
import com.eldritch.common.GameEvent;

public class ClientMovementHandler implements EventListener {
    private GameClient client;

    @Override
    public void handleEvent(GameEvent event) {
        if (event instanceof MovementEvent) {
            // Optimistically update local state
            client.getLocalGameState().applyEvent(event);

            // Show animation
            animateMovement((MovementEvent)event);
        }
    }

    private void animateMovement(MovementEvent event) {

    }
}