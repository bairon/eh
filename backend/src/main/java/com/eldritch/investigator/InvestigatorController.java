package com.eldritch.investigator;

import com.eldritch.game.Player;
import com.eldritch.lobby.EhAgent;
import com.eldritch.lobby.EhLobby;
import com.eldritch.lobby.EhLobbyManager;
import com.eldritch.session.GameSession;
import com.eldritch.session.GameSessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/investigator")
public class InvestigatorController {

    @Autowired
    private InvestigatorService investigatorService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // For WebSocket broadcasting

    @Autowired
    private EhLobbyManager lobbyManager;

    @GetMapping("/list")
    public List<InvestigatorTemplate> getInvestigators() {
        return investigatorService.getInvestigators();
    }

    @PostMapping("/select")
    public ResponseEntity<Object> selectInvestigator(@RequestBody Map<String, String> request, HttpSession session) {
        // Retrieve the GameSession ID from the HttpSession
        String lobbyId = (String) session.getAttribute("lobbyId");
        if (lobbyId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game session not found. Please start or join a game.");
        }

        // Retrieve the game state from the GameSession
        String userId = (String) session.getAttribute("userId");
        EhLobby lobby = lobbyManager.getLobby(lobbyId);
        EhAgent agent = lobby.getAgent(userId);

        if (agent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You are not in the game.");
        }

        String investigatorId = request.get("id");
        agent.setInvestigatorId(investigatorId);

        // Broadcast the updated game state to all clients in the session
        messagingTemplate.convertAndSend("/topic/ehlobby/" + lobbyId, lobby.info());

        return ResponseEntity.ok().build();
    }
}