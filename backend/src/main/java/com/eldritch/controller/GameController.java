package com.eldritch.controller;

import com.eldritch.model.GameSession;
import com.eldritch.model.Player;
import com.eldritch.service.GameSessionService;
import com.eldritch.service.exception.GameNotAvailableException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameSessionService gameSessionService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate; // For WebSocket broadcasting

    @PostMapping("/create")
    public ResponseEntity<GameSession> createGameSession(@RequestBody Map<String, String> request, HttpSession session) {
        String gameName = request.get("gameName");
        String playerName = request.get("playerName");
        GameSession gameSession = gameSessionService.createGameSession(gameName);
        Player player = new Player();
        player.setName(playerName);
        player.setPlayerId(session.getId());
        gameSession.addPlayer(player);

        // Store the GameSession ID in the HttpSession
        session.setAttribute("gameSessionId", gameSession.getSessionId());

        // Broadcast the updated game state to all clients in the session
        messagingTemplate.convertAndSend("/topic/gameSession/" + gameSession.getSessionId(), gameSession);

        return ResponseEntity.ok(gameSession);
    }

    @GetMapping("/list")
    public ResponseEntity<List<GameSession>> getAvailableGames() {
        List<GameSession> games = gameSessionService.getAvailableGames();
        return ResponseEntity.ok(games);
    }

    @PostMapping("/join")
    public ResponseEntity<Object> joinGameSession(@RequestBody Map<String, String> request, HttpSession session) {
        String playerName = request.get("playerName");
        String sessionId = request.get("sessionId");
        Player player = new Player();
        player.setPlayerId(session.getId());
        player.setName(playerName);
        try {
            GameSession gameSession = gameSessionService.joinGameSession(sessionId, player);
            // Store the GameSession ID in the HttpSession
            session.setAttribute("gameSessionId", sessionId);
            // Broadcast the updated game state to all clients in the session
            messagingTemplate.convertAndSend("/topic/gameSession/" + gameSession.getSessionId(), gameSession);

            return ResponseEntity.ok(gameSession);
        } catch (GameNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This game is no longer available for joining.");
        }
    }

    @PostMapping("/leave")
    public ResponseEntity<Void> leaveGameSession(HttpSession session) {
        String gameSessionId = (String) session.getAttribute("gameSessionId");
        if (gameSessionId != null) {
            GameSession gameSession = gameSessionService.leaveGameSession(gameSessionId, session.getId());
            session.setAttribute("gameSessionId", null);
            if (gameSession != null) {
                messagingTemplate.convertAndSend("/topic/gameSession/" + gameSessionId, gameSession);
            }
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<GameSession> checkGameSession(HttpSession session) {
        // Retrieve the game state from the session
        String gameSessionId = (String) session.getAttribute("gameSessionId");
        if (gameSessionId == null) {
            return ResponseEntity.ok().build();
        }
        GameSession gameSession = gameSessionService.getGameSession(gameSessionId);
        if (gameSession == null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(gameSession);
    }

}