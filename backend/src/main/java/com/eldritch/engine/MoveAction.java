package com.eldritch.engine;

import com.eldritch.common.*;

import java.util.List;
import java.util.UUID;

public class MoveAction implements GameAction {
    private final UUID investigatorId;
    private final UUID destinationId;

    public MoveAction(UUID investigatorId, UUID destinationId) {
        this.investigatorId = investigatorId;
        this.destinationId = destinationId;
    }

    @Override
    public UUID getActionId() {
        return null;
    }

    @Override
    public UUID getPlayerId() {
        return null;
    }

    public UUID getInvestigatorId() {
        return investigatorId;
    }

    @Override
    public boolean canExecute(GameState state) {
        return true;
    }

    @Override
    public GameEvent execute(GameState state) {

        return new InvestigatorMovedEvent(
                investigatorId,
                investigatorId,
                destinationId
        );
    }

    @Override
    public boolean requiresPlayerChoice() {
        return false;
    }

    @Override
    public List<GameAction> getPossibleChoices(GameState state) {
        return List.of();
    }

    @Override
    public String getDescription() {
        return "";
    }

    public UUID getDestinationId() {
        return destinationId;
    }
}