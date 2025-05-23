package com.eldritch.logic;

import com.eldritch.lobby.EhLobby;
import com.eldritch.lobby.EhLobbyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class EhLogicController {
    private final EhLobbyManager lobbyManager;

    @Autowired
    public EhLogicController(EhLobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }

    @MessageMapping("/topic/lobby/{lobbyId}/answer")
    public void interract(@Payload Map<String, Object> payload, @DestinationVariable String lobbyId, SimpMessageHeaderAccessor headerAccessor) {
        String userId = headerAccessor.getUser().getName();
        String answer = (String) payload.get("answer");

        EhLobby lobby = lobbyManager.getLobby(lobbyId);
        if (lobby != null) {
            lobby.getServer().onAnswerReceived(userId, answer);
        }
    }


}
