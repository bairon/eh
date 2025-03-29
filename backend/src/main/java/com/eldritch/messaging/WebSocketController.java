package com.eldritch.messaging;

import com.eldritch.game.GameState;
import com.eldritch.session.GameSessionService;
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