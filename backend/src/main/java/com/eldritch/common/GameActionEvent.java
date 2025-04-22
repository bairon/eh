package com.eldritch.common;

import com.eldritch.engine.GameAction;

import java.util.UUID;

public class GameActionEvent implements GameEvent {
    private final UUID eventId;
    private final long timestamp;
    private final GameAction action;
    private final UUID playerId;

    public GameActionEvent(GameAction action, UUID playerId) {
        this.eventId = UUID.randomUUID();
        this.timestamp = System.currentTimeMillis();
        this.action = action;
        this.playerId = playerId;
    }

    @Override
    public String getEventType() {
        return "GAME_ACTION";
    }

    @Override
    public Object getPayload() {
        return action;
    }

    @Override
    public UUID getOriginId() {
        return playerId;
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    public GameAction getAction() {
        return action;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
