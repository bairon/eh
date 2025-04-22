package com.eldritch.engine;

import com.eldritch.common.GameState;

public interface ActionValidator {
    boolean validate(GameAction action, GameState state);
}