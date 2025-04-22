// MythosEffect.java
package com.eldritch.common;

public interface MythosEffect {
    void apply(GameState state);
    void onDiscard();
    boolean shouldTrigger(int currentDoom);
}

