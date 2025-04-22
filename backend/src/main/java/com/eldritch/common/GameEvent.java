package com.eldritch.common;

import java.util.UUID;

public interface GameEvent {
    String getEventType();
    UUID getOriginId();
    UUID getEventId();

    // Flexible payload handling
    default <T> T getPayload(Class<T> payloadType) {
        return null; // Override in implementations that use payloads
    }

    // Helper method for simple payloads
    default Object getPayload() {
        return null; // Maintain backward compatibility
    }
}