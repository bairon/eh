package com.eldritch.controller;

import com.eldritch.model.*;
import com.eldritch.service.GameSessionService;
import com.eldritch.service.InvestigatorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/investigator")
public class InvestigatorController {

    @Autowired
    private InvestigatorService investigatorService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // For WebSocket broadcasting
    @Autowired
    private GameSessionService gameSessionService;

    @GetMapping("/list")
    public List<Investigator> getInvestigators() {
        Locale locale = LocaleContextHolder.getLocale();
        return investigatorService.getInvestigators(locale);
    }

    @PostMapping("/select")
    public ResponseEntity<Object> selectInvestigator(@RequestBody Map<String, String> request, HttpSession session) {
        // Retrieve the GameSession ID from the HttpSession
        String gameSessionId = (String) session.getAttribute("gameSessionId");
        if (gameSessionId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game session not found. Please start or join a game.");
        }

        // Retrieve the game state from the GameSession
        String userId = (String) session.getAttribute("userId");
        GameSession gameSession = gameSessionService.getGameSession(gameSessionId);
        Player player = gameSessionService.getPlayer(userId);

        if (player == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You are not in the game.");
        }

        String investigatorName = request.get("investigatorName");
        player.setInvestigatorName(investigatorName);

        // Broadcast the updated game state to all clients in the session
        messagingTemplate.convertAndSend("/topic/gameSession/" + gameSessionId, gameSession);

        return ResponseEntity.ok().build();
    }
}