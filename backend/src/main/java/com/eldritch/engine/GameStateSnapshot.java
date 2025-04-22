package com.eldritch.engine;

import com.eldritch.ancientone.AncientOne;
import com.eldritch.common.Card;
import com.eldritch.common.Location;
import com.eldritch.common.Monster;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// Serialization helper class
public class GameStateSnapshot implements Serializable {
    private final GamePhase currentPhase;
    private final int roundNumber;
    private final Map<UUID, Investigator> investigators;
    private final AncientOne ancientOne;
    private final List<UUID> mythosDeck;
    private final List<UUID> discardPile;
    private final Map<UUID, Location> locations;
    private final Map<UUID, Monster> monsters;
    private final Map<UUID, Card> cards;

    public GameStateSnapshot(
            GamePhase currentPhase,
            int roundNumber,
            Map<UUID, Investigator> investigators,
            AncientOne ancientOne,
            List<UUID> mythosDeck,
            List<UUID> discardPile,
            Map<UUID, Location> locations,
            Map<UUID, Monster> monsters,
            Map<UUID, Card> cards) {

        this.currentPhase = currentPhase;
        this.roundNumber = roundNumber;
        this.investigators = investigators;
        this.ancientOne = ancientOne;
        this.mythosDeck = mythosDeck;
        this.discardPile = discardPile;
        this.locations = locations;
        this.monsters = monsters;
        this.cards = cards;
    }

    // Getters for all fields
    public GamePhase getCurrentPhase() { return currentPhase; }
    public int getRoundNumber() { return roundNumber; }
    public Map<UUID, Investigator> getInvestigators() { return investigators; }
    public AncientOne getAncientOne() { return ancientOne; }
    public List<UUID> getMythosDeck() { return mythosDeck; }
    public List<UUID> getDiscardPile() { return discardPile; }
    public Map<UUID, Location> getLocations() { return locations; }
    public Map<UUID, Monster> getMonsters() { return monsters; }
    public Map<UUID, Card> getCards() { return cards; }
}
