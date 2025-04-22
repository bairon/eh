package com.eldritch.session;

import com.eldritch.game.GameFlowController;
import com.eldritch.game.GameStatus;
import com.eldritch.game.HumanPlayer;
import com.eldritch.game.Player;
import com.eldritch.user.UserData;
import com.eldritch.exception.GameNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameSessionService {

    private static final int MAX_PLAYERS = 4;
    private static final int MAX_PHASE_TIME = 1000;
    private final Map<String, GameSession> activeSessions = new HashMap<>();
    private final Map<String, Player> activePlayers = new HashMap<>();
    @Autowired
    private GameFlowController gameFlowController;

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
            } else {
                gameSession.getPlayers().get(0).setMaster(true);
            }
        }
        return gameSession;
    }

    public Player getPlayer(UserData userData) {
        activePlayers.putIfAbsent(userData.getId(), new HumanPlayer(userData));
        return activePlayers.get(userData.getId());
    }
    public Player getPlayer(String userId) {
        return activePlayers.get(userId);
    }

    @Scheduled(fixedRate = 5000) // Check every 5 seconds
    public void processGamePhases() {
        for (GameSession session : activeSessions.values()) {
            if (session.needsPhaseAdvancement()) {
                gameFlowController.advanceGame(session);
                pushStateToPlayers(session);
            }
        }
    }
    @Scheduled(fixedRate = 1000)
    public void checkPhaseTimeouts() {
        activeSessions.values().stream()
                .filter(s -> s.getPhaseDuration() > MAX_PHASE_TIME)
                .forEach(gameFlowController::forcePhaseAdvance);
    }
    private void pushStateToPlayers(GameSession session) {


    }

}