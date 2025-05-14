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
    private final LobbyManager lobbyManager;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public QuizController(LobbyManager lobbyManager, SimpMessagingTemplate messagingTemplate) {
        this.lobbyManager = lobbyManager;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/quiz/join")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> joinQuiz(
            @RequestParam String nickname) {
        try {
            // Get or create an available lobby
            Lobby lobby = lobbyManager.join(nickname);

            // Prepare response with both player and lobby info
            Map<String, Object> response = new HashMap<>();
            response.put("player", lobby.getLastJoinedPlayer());
            response.put("lobbyId", lobby.getId());

            QuizMessage message = new QuizMessage();
            message.setType(QuizMessage.MessageType.JOIN);
            message.setPlayers(new ArrayList<>(lobby.getPlayers()));
            message.setContent(nickname + " joined the quiz");

            messagingTemplate.convertAndSend("/topic/quiz/" + lobby.getId(), message);

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

        Lobby lobby = lobbyManager.getLobby(lobbyId);
        if (lobby != null) {
            QuizMessage message = new QuizMessage();
            message.setType(QuizMessage.MessageType.UPDATE);
            message.setPlayers(new ArrayList<>(lobby.getPlayers()));
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

        Lobby lobby = lobbyManager.getLobby(lobbyId);
        if (lobby != null && lobby.getGameInstance().getPlayer(playerId) != null) {
            QuizMessage message = new QuizMessage();
            message.setType(QuizMessage.MessageType.CONNECTED);
            message.setPlayers(new ArrayList<>(lobby.getPlayers()));
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

        Lobby lobby = lobbyManager.getLobby(lobbyId);
        if (lobby != null) {
            QuizPlayer player = lobby.getGameInstance().getPlayer(playerId);
            if (player != null) {
                // Create full state message
                QuizMessage message = new QuizMessage();
                message.setType(QuizMessage.MessageType.REJOIN);
                message.setPlayers(new ArrayList<>(lobby.getPlayers()));
                message.setContent(player.getNickname() + " rejoined");

                // Include current question if game is active
                if (lobby.getGameInstance().isQuizRunning()) {
                    message.setQuestion(lobby.getGameInstance().getCurrentQuestion());
                    message.setPlayer(lobby.getGameInstance().getCurrentPlayer());
                }

                return message;
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

    private void broadcastPlayerUpdate(String lobbyId) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        Collection<QuizPlayer> currentPlayers = lobby.getPlayers();
        System.out.println("Broadcasting players: " + currentPlayers);

        QuizMessage message = new QuizMessage();
        message.setType(QuizMessage.MessageType.UPDATE);
        message.setPlayers(new ArrayList<>(currentPlayers));
        message.setContent(currentPlayers.isEmpty() ? "Waiting for players" : "Players updated");

        messagingTemplate.convertAndSend("/topic/quiz", message);
    }

    @MessageMapping("/quiz/{lobbyId}/answer")
    public void processAnswer(@Payload Map<String, Object> payload,
                              @DestinationVariable String lobbyId) {
        String playerId = (String) payload.get("playerId");
        Integer answerIndex = (Integer) payload.get("answerIndex");

        Lobby lobby = lobbyManager.getLobby(lobbyId);
        if (lobby != null) {
            lobby.getGameInstance().processAnswer(playerId, answerIndex);
            // Send update to this lobby only
            messagingTemplate.convertAndSend("/topic/quiz/" + lobbyId,
                    createPlayerUpdate(lobby));
        }
    }
    private QuizMessage createPlayerUpdate(Lobby lobby) {
        QuizMessage message = new QuizMessage();
        message.setType(QuizMessage.MessageType.UPDATE);
        message.setPlayers(new ArrayList<>(lobby.getPlayers()));
        message.setContent("Players updated");
        return message;
    }

    private void sendPlayersUpdate(String lobbyId) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        QuizMessage message = new QuizMessage();
        message.setType(QuizMessage.MessageType.UPDATE);
        message.setPlayers(new ArrayList<>(lobby.getPlayers()));
        messagingTemplate.convertAndSend("/topic/quiz", message);
    }

    @EventListener
    public void handleWebSocketConnect(SessionConnectedEvent event) {
        System.out.println("New WebSocket connection: " + event.getUser());
    }
}