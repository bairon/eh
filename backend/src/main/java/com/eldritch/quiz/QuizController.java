package com.eldritch.quiz;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
public class QuizController {
    private static final Logger logger = LogManager.getLogger(QuizController.class);
    private final QuizLobbyManager quizLobbyManager;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public QuizController(QuizLobbyManager quizLobbyManager, SimpMessagingTemplate messagingTemplate) {
        this.quizLobbyManager = quizLobbyManager;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/quiz/check")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkQuiz(
            @RequestParam String quizPlayerId, @RequestParam String currentLobbyId) {
        try {
            // Get or create an available lobby
            QuizLobby quizLobby = quizLobbyManager.find(quizPlayerId, currentLobbyId);
            Map<String, Object> response = new HashMap<>();
            if (quizLobby != null) {
                response.put("player", quizLobby.getPlayer(quizPlayerId));
                response.put("currentLobbyId", quizLobby.getId());
            }
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Failed to check quiz for quizPlayerId: " + quizPlayerId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to check quiz");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    @PostMapping("/quiz/join")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> joinQuiz(
            @RequestParam String nickname) {
        try {
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
    @MessageMapping("/quiz/{lobbyId}/state")
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

    @MessageMapping("/quiz/state")
    @SendTo("/topic/quiz")
    public QuizMessage sendInitialState() {
        // This should be lobby-specific or removed
        QuizMessage message = new QuizMessage();
        message.setType(QuizMessage.MessageType.UPDATE);
        message.setContent("Please join a specific lobby");
        return message;
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
}