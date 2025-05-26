package com.eldritch.ancientone;

import com.eldritch.lobby.EhLobby;
import com.eldritch.lobby.EhLobbyManager;
import com.eldritch.lobby.LobbyInfo;
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
    private EhLobbyManager lobbyManager;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // For WebSocket broadcasting

    @GetMapping("/list")
    public List<AncientOne> getAncientOnes(HttpSession session) {
        return ancientOneService.getAncientOnes();
    }

    @PostMapping("/select")
    public ResponseEntity<Object> selectAncientOne(@RequestBody Map<String, String> request, HttpSession session) {
        // Retrieve the GameSession ID from the HttpSession
        String lobbyId = (String) session.getAttribute("lobbyId");
        if (lobbyId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lobby not found. Please start or join a game.");
        }

        EhLobby lobby = lobbyManager.getLobby(lobbyId);

        if (lobby == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lobby not found.");
        }

        String ancientId = request.get("id");
        lobby.setAncientId(ancientId);

        // Broadcast the updated game state to all clients in the session
        LobbyInfo lobbyInfo = lobby.info();
        messagingTemplate.convertAndSend("/topic/ehlobby/" + lobbyId, lobbyInfo);

        return ResponseEntity.ok(lobbyInfo);
    }
}