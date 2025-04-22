package com.eldritch.client;

import com.eldritch.common.*;
import com.eldritch.engine.*;

import java.util.UUID;

public class GameClient implements Client {
    private EventBus localEventBus;
    private ClientConnection serverConnection;
    private GameState localGameState;

    public void onServerEvent(GameEvent event) {
        localGameState.applyEvent(event);
        localEventBus.publish(event);
    }

    public void submitAction(GameAction action) {
        //serverConnection.sendEvent(new GameActionEvent(action, UUID.randomUUID()));
    }

    @Override
    public void connectToServer(String address) {

    }

    @Override
    public void sendToServer(GameEvent event) {

    }

    @Override
    public void registerLocalListener(EventListener listener) {

    }

    public GameState getLocalGameState() {
        return localGameState;
    }
}