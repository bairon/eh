package com.eldritch.engine;

import com.eldritch.common.EventListener;
import com.eldritch.common.GameEvent;

public interface Client {
    void connectToServer(String address);
    void sendToServer(GameEvent event);
    void registerLocalListener(EventListener listener);
}
