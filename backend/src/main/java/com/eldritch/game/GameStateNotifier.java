package com.eldritch.game;

import com.eldritch.session.GameSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class GameStateNotifier {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyPlayers(GameSession session) {
        GameState state = session.getGameState();

        //for (Player player : session.getPlayers()) {
            messagingTemplate.convertAndSend(
                    "/topic/game/" + session.getSessionId(),
                    session
            );
        //}
    }
}
