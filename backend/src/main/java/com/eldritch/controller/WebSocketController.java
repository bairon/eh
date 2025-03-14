package com.eldritch.controller;

import com.eldritch.model.GameState;
import com.eldritch.service.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private GameSessionService gameSessionService;

    @MessageMapping("/updateGameState")
    @SendTo("/topic/gameSession")
    public GameState updateGameState(String sessionId) {
        return gameSessionService.getGameSession(sessionId).getGameState();
    }
}