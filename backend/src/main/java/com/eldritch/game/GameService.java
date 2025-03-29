package com.eldritch.game;

import com.eldritch.event.MythosEvent;
import com.eldritch.event.OmenChangeEvent;
import com.eldritch.event.ReckoningEvent;
import com.eldritch.session.GameSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private GameState gameState;

    @Autowired
    private PortalService portalService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public GameState getGameState() {
        return gameState;
    }

    public void playerAction(String playerName, String action) {
        // Implement player actions (move, acquire item, fight monster)
    }

    public void environmentPhase() {
        // Implement environment phase (reduce doom, spawn monsters)
    }
    public void changeOmen() {
        eventPublisher.publishEvent(new OmenChangeEvent("The omen has changed!"));
    }

    public void triggerReckoning() {
        eventPublisher.publishEvent(new ReckoningEvent("Reckoning begins!"));
    }

    public void drawMythos() {
        eventPublisher.publishEvent(new MythosEvent("A new Mythos card is drawn!"));
    }

    public GameSession initDepo(GameSession gameSession) {
        gameSession.setDepo(new Depo(
                portalService.getPortals(),
                clueService.getClues()
                )
        );
        return gameSession;
    }

    public void start(GameSession gameSession) {
        gameSession.setGameStatus(GameStatus.ONGOING);

        initDepo(gameSession);

    }
}