package com.eldritch.model;

import java.util.ArrayList;
import java.util.List;

public class GameSession {
    private String sessionId;
    private String gameName;
    private List<Player> players;
    private GameState gameState;
    private GameStatus gameStatus;

    public GameSession(String sessionId, String gameName) {
        this.sessionId = sessionId;
        this.gameName = gameName;
        this.players = new ArrayList<>();
        this.gameState = new GameState();
    }
    // Getters and Setters

    public String getSessionId() {
        return sessionId;
    }

    public String getGameName() {
        return gameName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Player getPlayerById(String id) {
        return players.stream().filter(player -> player.getPlayerId().equals(id)).findFirst().orElse(null);
    }
}