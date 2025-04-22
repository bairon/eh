package com.eldritch.common;

import com.eldritch.ancientone.AncientOne;
import com.eldritch.engine.GamePhase;
import com.eldritch.engine.GameStateSnapshot;
import com.eldritch.engine.Investigator;

import java.util.List;
import java.util.UUID;

public interface GameState {
    GamePhase getCurrentPhase();
    int getRoundNumber();
    List<Investigator> getInvestigators();
    AncientOne getAncientOne();
    void applyEvent(GameEvent event);
    GameStateSnapshot getSnapshot();

    // Additional methods to support full functionality
    Investigator getInvestigator(UUID investigatorId);
    void addInvestigator(Investigator investigator);
    void setCurrentPhase(GamePhase phase);
    void incrementRound();
    void setAncientOne(AncientOne ancientOne);

    // Location and game board methods
    List<Location> getLocations();
    Location getLocation(UUID locationId);

    // Card deck methods
    List<UUID> getMythosDeck();
    List<UUID> getDiscardPile();

    // Monster methods
    List<Monster> getMonsters();
    Monster getMonster(UUID monsterId);

    // Card methods
    Card getCard(UUID cardId);

    boolean canMoveInvestigator(UUID originId, UUID investigatorId, UUID destinationId);

    void initializeNewGame();
}