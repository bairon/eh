package com.eldritch.common;

public class OngoingEffectsEvent extends GenericGameEvent {
    public OngoingEffectsEvent(Object payload) {
        super("ONGOING_EFFECT", payload);
    }
}
