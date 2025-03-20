package com.eldritch.service;

import com.eldritch.model.GameSession;
import com.eldritch.model.GameStatus;
import com.eldritch.model.Player;
import com.eldritch.service.exception.GameNotAvailableException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameSessionService {

    private static final int MAX_PLAYERS = 4;
    private final Map<String, GameSession> activeSessions = new HashMap<>();
    private final Map<String, Player> activePlayers = new HashMap<>();

    public GameSession createGameSession(String gameName, Player player) {
        String sessionId = UUID.randomUUID().toString();
        GameSession session = new GameSession(sessionId, gameName);
        session.setGameStatus(GameStatus.LOBBY);
        session.addPlayer(player);
        activeSessions.put(sessionId, session);

        return session;
    }

    public GameSession getGameSession(String sessionId) {
        return activeSessions.get(sessionId);
    }

    public synchronized GameSession joinGameSession(String sessionId, Player player) throws GameNotAvailableException {
        GameSession session = activeSessions.get(sessionId);
        if (session != null && session.getPlayers().size() < MAX_PLAYERS) {
            session.getPlayers().add(player);
        }
        return session;
    }

    public List<GameSession> getAvailableGames() {
        return activeSessions.values().stream().filter(gameSession -> gameSession.getGameStatus() == GameStatus.LOBBY).collect(Collectors.toList());
    }

    public GameSession leaveGameSession(String gameSessionId, String playerId) {
        GameSession gameSession = activeSessions.get(gameSessionId);
        if (gameSession != null) {
            gameSession.getPlayers().removeIf(player -> player.getPlayerId().equals(playerId));
            if (gameSession.getPlayers().isEmpty()) {
                activeSessions.remove(gameSessionId);
                return null;
            }
        }
        return gameSession;
    }

    public Player getPlayer(String id) {
        activePlayers.putIfAbsent(id, new Player(UUID.randomUUID().toString()));
        return activePlayers.get(id);
    }
}