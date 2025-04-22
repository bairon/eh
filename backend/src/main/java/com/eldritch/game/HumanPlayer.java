package com.eldritch.game;

import com.eldritch.session.GameSession;
import com.eldritch.user.UserData;

public class HumanPlayer extends Player {
    public HumanPlayer(UserData userData) {
        super(userData);
    }

    @Override
    public void handleGameStateUpdate(GameSession session) {
        // Forward to UI (no change from current behavior)
    }
}
