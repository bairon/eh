package com.eldritch.engine;

import com.eldritch.client.ClientConnection;
import com.eldritch.common.EventBus;
import com.eldritch.common.GameEvent;

import java.util.UUID;

public interface Server {
    void broadcastGameState();
    void registerClient(ClientConnection connection);
    void start();

    EventBus getEventBus();
}
