package com.eldritch.engine;

import com.eldritch.common.GameState;

public class DefaultActionValidator implements ActionValidator {
    @Override
    public boolean validate(GameAction action, GameState state) {
        return false;
    }
}
