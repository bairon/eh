package com.eldritch.controller;

import com.eldritch.model.AncientOne;
import com.eldritch.model.GameSession;
import com.eldritch.model.GameState;
import com.eldritch.service.AncientOneService;
import com.eldritch.service.GameSessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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
    public List<AncientOne> getAncientOnes() {
        Locale locale = LocaleContextHolder.getLocale();
        return ancientOneService.getAncientOnes(locale);
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
        String ancientOneName = request.get("name");
        gameState.setAncientOne(new AncientOne(ancientOneName));

        // Broadcast the updated game state to all clients in the session
        messagingTemplate.convertAndSend("/topic/gameSession/" + gameSessionId, gameSession);

        return ResponseEntity.ok().build();
    }
}