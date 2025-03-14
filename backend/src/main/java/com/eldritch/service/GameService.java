package com.eldritch.service;

import com.eldritch.model.GameState;
import com.eldritch.model.Player;
import com.eldritch.model.AncientOne;
import com.eldritch.model.events.MythosEvent;
import com.eldritch.model.events.OmenChangeEvent;
import com.eldritch.model.events.ReckoningEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {
    private GameState gameState;

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

}