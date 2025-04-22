package com.eldritch.engine;

import com.eldritch.common.GameEvent;
import com.eldritch.common.GameState;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public interface GameAction extends Serializable {
    UUID getActionId();
    UUID getPlayerId();
    boolean canExecute(GameState state);
    GameEvent execute(GameState state);
    boolean requiresPlayerChoice();
    List<GameAction> getPossibleChoices(GameState state);
    String getDescription();
}