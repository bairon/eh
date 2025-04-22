package com.eldritch.common;

import com.eldritch.engine.GameAction;

import java.util.List;
import java.util.UUID;

public class EndTurnAction implements GameAction {
    @Override
    public UUID getActionId() {
        return null;
    }

    @Override
    public UUID getPlayerId() {
        return null;
    }

    @Override
    public boolean canExecute(GameState state) {
        return false;
    }

    @Override
    public GameEvent execute(GameState state) {
        return null;
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
}
