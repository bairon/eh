package com.eldritch.ancientone;

import com.eldritch.session.GameSession;
import com.eldritch.game.GameState;
import com.eldritch.session.GameSessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@RequestMapping("/api/ancient-one")
public class AncientOneController {

    @Autowired
    private AncientOneService ancientOneService;

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // For WebSocket broadcasting

    @GetMapping("/list")
    public List<AncientOne> getAncientOnes(HttpSession session) {
        return ancientOneService.getAncientOnes();
    }

    @PostMapping("/select")
    public ResponseEntity<Object> selectAncientOne(@RequestBody Map<String, String> request, HttpSession session) {
        // Retrieve the GameSession ID from the HttpSession
        String gameSessionId = (String) session.getAttribute("gameSessionId");
        if (gameSessionId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game session not found. Please start or join a game.");
        }

        // Retrieve the game state from the GameSession
        GameSession gameSession = gameSessionService.getGameSession(gameSessionId);
        GameState gameState = gameSession.getGameState();
        if (gameState == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game state not found.");
        }

        // Set the selected Ancient One in the game state
        String ancientId = request.get("id");
        gameState.setAncientId(ancientId);

        // Broadcast the updated game state to all clients in the session
        messagingTemplate.convertAndSend("/topic/gameSession/" + gameSessionId, gameSession);

        return ResponseEntity.ok(gameSession);
    }
}