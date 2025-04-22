package com.eldritch.common;

import com.eldritch.client.ClientConnection;

public interface EventBus {
    void subscribe(String eventType, EventListener listener);
    void unsubscribe(String eventType, EventListener listener);
    void publish(GameEvent event);
    void registerRemoteListener(ClientConnection connection);
}
