package com.eldritch.common;

public class ResourcePhaseEvent extends GenericGameEvent {
    public ResourcePhaseEvent(Object payload) {
        super("GAME_OVER", payload);
    }
}
