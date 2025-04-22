package com.eldritch.engine;

import com.eldritch.common.GameState;
import com.eldritch.common.Location;

public class MovementValidator implements ActionValidator {
    @Override
    public boolean validate(GameAction action, GameState state) {
        if (!(action instanceof MoveAction move)) return false;

        Investigator investigator = state.getInvestigator(move.getPlayerId());
        Location current = state.getLocation(investigator.getLocationId());
        Location destination = state.getLocation(move.getDestinationId());

        return current.getConnections().contains(destination.getId());
    }
}