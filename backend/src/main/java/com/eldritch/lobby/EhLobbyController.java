package com.eldritch.lobby;

import com.eldritch.exception.GameNotAvailableException;
import com.eldritch.game.Player;
import com.eldritch.session.GameSession;
import com.eldritch.user.UserData;
import com.eldritch.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lobby")
public class EhLobbyController {
    private static final Logger logger = LogManager.getLogger(EhLobbyController.class);
    private final EhLobbyManager lobbyManager;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;

    @Autowired
    public EhLobbyController(EhLobbyManager lobbyManager, SimpMessagingTemplate messagingTemplate, UserService userService) {
        this.lobbyManager = lobbyManager;
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<LobbyInfo>> list() {
        return ResponseEntity.ok(lobbyManager.list());
    }

    @PostMapping("/create")
    public ResponseEntity<LobbyInfo> create(@RequestBody Map<String, String> request, HttpSession session) {
        logger.info("/create:" + session.getId());
        String gameName = request.get("gameName");
        String userId = (String) session.getAttribute("userId");

        EhLobby newOrExistingLobby = lobbyManager.create(gameName, userId);

        session.setAttribute("lobbyId", newOrExistingLobby.getId());

        return ResponseEntity.ok(newOrExistingLobby.info());
    }

    @PostMapping("/check")
    public ResponseEntity<LobbyInfo> check(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        try {
            if (userId != null) {
                EhLobby lobby = lobbyManager.findByUserId(userId);
                if (lobby != null) {
                    return ResponseEntity.ok(lobby.info());
                }
            }
        } catch (RuntimeException e) {
            logger.error("Failed to check game for userId: " + userId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
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

    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> join(@RequestBody Map<String, String> request, HttpSession session) {
        String lobbyId = request.get("lobbyId");
        String userId = session.getAttribute("userId").toString();
        try {
            UserData userData = userService.getUserDataById(userId);
            lobbyManager.join(lobbyId, userId);
            // Get or create an available lobby
            QuizLobby quizLobby = quizLobbyManager.join(nickname);

            // Prepare response with both player and lobby info
            Map<String, Object> response = new HashMap<>();
            response.put("player", quizLobby.getLastJoinedPlayer());
            response.put("lobbyId", quizLobby.getId());

            QuizService quizService = quizLobby.getGameInstance();
            messagingTemplate.convertAndSend("/topic/quiz/" + quizLobby.getId(), quizService.getMessage());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Log the exception
            logger.error("Failed to join quiz for nickname: " + nickname, e);

            // Return more detailed error information
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to join quiz");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    /*    @MessageMapping("/quiz/{lobbyId}/state")
        @SendTo("/topic/quiz/{lobbyId}")
        public QuizMessage sendLobbyState(
                @DestinationVariable String lobbyId) {

            QuizLobby quizLobby = quizLobbyManager.getLobby(lobbyId);
            if (quizLobby != null) {
                QuizMessage message = new QuizMessage();
                message.setType(QuizMessage.MessageType.UPDATE);
                message.setPlayers(new ArrayList<>(quizLobby.getPlayers()));
                message.setContent("Lobby state update");
                return message;
            }
            return null;
        }
    */
    @MessageMapping("/quiz/connect/{lobbyId}/{playerId}")
    @SendTo("/topic/quiz/{lobbyId}")
    public QuizMessage handleConnect(
            @DestinationVariable String lobbyId,
            @DestinationVariable String playerId) {

        QuizLobby quizLobby = quizLobbyManager.getLobby(lobbyId);
        if (quizLobby != null && quizLobby.getGameInstance().getPlayer(playerId) != null) {
            QuizMessage message = new QuizMessage();
            message.setType(QuizMessage.MessageType.CONNECTED);
            message.setPlayers(new ArrayList<>(quizLobby.getPlayers()));
            message.setContent("Player reconnected");
            return message;
        }
        return null;
    }


    @MessageMapping("/quiz/{lobbyId}/rejoin/{playerId}")
    @SendTo("/topic/quiz/{lobbyId}")
    public QuizMessage handleRejoin(
            @DestinationVariable String lobbyId,
            @DestinationVariable String playerId) {

        QuizLobby quizLobby = quizLobbyManager.getLobby(lobbyId);
        if (quizLobby != null) {
            QuizPlayer player = quizLobby.getGameInstance().getPlayer(playerId);
            if (player != null) {
                QuizService quizService = quizLobby.getGameInstance();
                return quizService.getMessage();
            }
        }
        return null;
    }

    @MessageMapping("/quiz/{lobbyId}/answer")
    public void processAnswer(@Payload Map<String, Object> payload,
                              @DestinationVariable String lobbyId) {
        String playerId = (String) payload.get("playerId");
        Integer answerIndex = (Integer) payload.get("answerIndex");

        QuizLobby quizLobby = quizLobbyManager.getLobby(lobbyId);
        if (quizLobby != null) {
            quizLobby.getGameInstance().onAnswerReceived(playerId, answerIndex);
        }

    }

    @EventListener
    public void handleWebSocketConnect(SessionConnectedEvent event) {
        System.out.println("New WebSocket connection: " + event.getUser());
    }
    @PostMapping("/quiz/deleteAllLobbies")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteAllLobbies() {
        try {
            quizLobbyManager.deleteAllLobbies();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "All lobbies deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to delete lobbies: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}