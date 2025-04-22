package com.eldritch.common;

import java.util.UUID;

public interface Card {
    UUID getId();
    String getName();
    CardType getType();

    // Common card functionality
    boolean canPlay(GameState state);
    GameEvent play(UUID investigatorId);
    GameEvent resolve();
    void discard();
}