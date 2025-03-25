package com.eldritch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class GameSession {

    private String sessionId;
    private String gameName;
    private Player player;
    private List<Player> players;
    private GameState gameState;
    private GameStatus gameStatus;
    @JsonIgnore
    private Depo depo;


    public GameSession(String sessionId, String gameName) {
        this.sessionId = sessionId;
        this.gameName = gameName;
        this.players = new ArrayList<>();
        this.gameState = new GameState();
    }

    public GameSession(GameSession gameSession) {
        this.sessionId = gameSession.sessionId;
        this.gameName = gameSession.gameName;
        this.player = gameSession.player;
        this.players = gameSession.getPlayers();
        this.gameState = gameSession.getGameState();
        this.gameStatus = gameSession.getGameStatus();
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayerById(String id) {
        return players.stream().filter(player -> player.getPlayerId().equals(id)).findFirst().orElse(null);
    }

    public Depo getDepo() {
        return depo;
    }

    public void setDepo(Depo depo) {
        this.depo = depo;
    }
}