package com.eldritch.lobby;

import com.eldritch.game.GameService;
import com.eldritch.game.Player;
import com.eldritch.session.GameSession;
import com.eldritch.user.UserData;
import com.eldritch.session.GameSessionService;
import com.eldritch.user.UserService;
import com.eldritch.exception.GameNotAvailableException;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // For WebSocket broadcasting

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public String getUserName(@AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            return principal.getAttribute("name"); // "name" is the attribute for the user's full name
        }
        return null;
    }

    @PostMapping("/create")
    public ResponseEntity<GameSession> createGameSession(@RequestBody Map<String, String> request, HttpSession session) {
        System.out.println("/create:" + session.getId());
        String gameName = request.get("gameName");
        String userId = (String) session.getAttribute("userId");
        UserData userData = userService.getUserDataById(userId);
        Player player = gameSessionService.getPlayer(userData);
        player.setMaster(true);
        GameSession gameSession = gameSessionService.createGameSession(gameName, player);

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
        String sessionId = request.get("sessionId");
        String userId = session.getAttribute("userId").toString();
        UserData userData = userService.getUserDataById(userId);
        Player player = gameSessionService.getPlayer(userData);
        player.setMaster(false);
        try {
            GameSession gameSession = gameSessionService.joinGameSession(sessionId, player);
            if (gameSession != null) {
                session.setAttribute("gameSessionId", sessionId);
                messagingTemplate.convertAndSend("/topic/gameSession/" + gameSession.getSessionId(), gameSession);
                return ResponseEntity.ok(gameSession);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (GameNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This game is no longer available for joining.");
        }
    }

    @PostMapping("/leave")
    public ResponseEntity<Void> leaveGameSession(HttpSession session) {
        System.out.println("/leave:" + session.getId());
        String gameSessionId = (String) session.getAttribute("gameSessionId");
        String userId = session.getAttribute("userId").toString();
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }
        if (gameSessionId != null) {
            GameSession gameSession = gameSessionService.leaveGameSession(gameSessionId, userId);
            session.removeAttribute("gameSessionId");
            if (gameSession != null) {
                messagingTemplate.convertAndSend("/topic/gameSession/" + gameSessionId, gameSession);
            }
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/kick")
    public ResponseEntity<Void> kickPlayer(HttpSession session) {
        System.out.println("/kick:" + session.getId());
        String gameSessionId = (String) session.getAttribute("gameSessionId");
        String userId = session.getAttribute("userId").toString();
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }
        if (gameSessionId != null) {
            GameSession gameSession = gameSessionService.leaveGameSession(gameSessionId, userId);
            session.removeAttribute("gameSessionId");
            if (gameSession != null) {
                messagingTemplate.convertAndSend("/topic/gameSession/" + gameSessionId, gameSession);
            }
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ping")
    public ResponseEntity<Void> ping(HttpSession session) {
        System.out.println("/ping:" + session.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<GameSession> checkGameSession(HttpSession session) {
        System.out.println("/check:" + session.getId());
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
    @PostMapping("/start")
    public ResponseEntity<Void> start(HttpSession session) {
        String gameSessionId = (String) session.getAttribute("gameSessionId");
        if (gameSessionId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        GameSession gameSession = gameSessionService.getGameSession(gameSessionId);
        if (gameSession == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.debug("Starting game " + session.getId());
        gameService.start(gameSession);
        messagingTemplate.convertAndSend("/topic/gameSession/" + gameSessionId, gameSession);
        return ResponseEntity.ok().build();
    }

}