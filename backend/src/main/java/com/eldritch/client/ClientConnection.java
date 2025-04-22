package com.eldritch.client;

import com.eldritch.common.PlayerChoiceRequest;
import com.eldritch.engine.GameAction;
import com.eldritch.engine.GameStateSnapshot;

import java.util.UUID;

public interface ClientConnection {
    UUID getClientId();
    void gameStateUpdated(GameStateSnapshot state);
    GameAction requestPlayerChoice(PlayerChoiceRequest request);
}