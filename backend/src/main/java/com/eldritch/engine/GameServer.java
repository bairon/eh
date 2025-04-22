package com.eldritch.engine;

import com.eldritch.client.ClientConnection;
import com.eldritch.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameServer implements Server {
    private static final Logger logger = LoggerFactory.getLogger(GameServer.class);
    private final EventBus eventBus;
    private final GameState gameState;
    private final Map<UUID, ClientConnection> clients;

    public GameServer() {
        this.eventBus = new SimpleEventBus();
        this.gameState = new PersistentGameState();
        this.clients = new ConcurrentHashMap<>();

        // Initialize phase handlers
        UpkeepPhaseHandler upkeepHandler = new UpkeepPhaseHandler(gameState, eventBus);
        ActionPhaseHandler actionHandler = new ActionPhaseHandler(gameState, eventBus);
        ContactPhaseHandler contactHandler = new ContactPhaseHandler(gameState, eventBus);
        MythosPhaseHandler mythosHandler = new MythosPhaseHandler(gameState, eventBus);

        // Register event listeners
        this.eventBus.subscribe("UPKEEP_STARTED", upkeepHandler);
        this.eventBus.subscribe("ACTIONS_STARTED", actionHandler);
        this.eventBus.subscribe("CONTACTS_STARTED", contactHandler);
        this.eventBus.subscribe("MYTHOS_STARTED", mythosHandler);

        // Add state update listener
        this.eventBus.subscribe("GAME_STATE_UPDATED", e -> broadcastGameState());
    }

    // Add helper method to get client connection
    public ClientConnection getClientConnection(UUID playerId) {
        return clients.get(playerId);
    }    @Override
    public void broadcastGameState() {
        GameStateSnapshot snapshot = gameState.getSnapshot();
        clients.values().forEach(connection -> {
            connection.gameStateUpdated(snapshot);
        });
    }

    @Override
    public void registerClient(ClientConnection connection) {
        UUID clientId = connection.getClientId();
        clients.put(clientId, connection);
    }

    @Override
    public void start() {
        initializeGame();
        eventBus.publish(new GenericGameEvent("UPKEEP_STARTED"));
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    private void initializeGame() {
        // load all game elements from the configuration database
        gameState.initializeNewGame();
    }

}