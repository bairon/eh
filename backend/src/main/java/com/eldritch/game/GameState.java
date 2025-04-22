package com.eldritch.game;

import com.eldritch.ancientone.AncientOne;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState {

    private String ancientId;
    private List<Portal> portals; // List of portal tokens

    private Map<String, Object> interactionState = new HashMap<>();
    private String currentInteractionType;
    private String interactionPrompt;
    public enum GamePhase {
        SETUP,
        INVESTIGATOR_SELECTION,
        MYTHOS_PHASE,
        ACTION_PHASE,
        ENCOUNTER_PHASE,
        MONSTER_PHASE,
        UPKEEP_PHASE,
        GAME_END
    }

    private GamePhase currentPhase;
    private int roundNumber;
    private String activePlayerId;
    private Map<String, Object> phaseData = new HashMap<>();

    // Transition methods
    public void advancePhase() {
        switch(currentPhase) {
            case SETUP:
                currentPhase = GamePhase.INVESTIGATOR_SELECTION;
                break;
            case INVESTIGATOR_SELECTION:
                currentPhase = GamePhase.MYTHOS_PHASE;
                roundNumber = 1;
                break;
            // ... other transitions
        }
    }
    // Interaction types
    public static final String INTERACTION_NONE = "none";
    public static final String INTERACTION_SPELL_CAST = "spell_cast";
    public static final String INTERACTION_SPELL_TARGET = "spell_target";
    public static final String INTERACTION_CHOICE = "choice";

    public Map<String, Object> getInteractionState() {
        return interactionState;
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public String getActivePlayerId() {
        return activePlayerId;
    }

    public Map<String, Object> getPhaseData() {
        return phaseData;
    }

    public void setInteraction(String type, String prompt, Map<String, Object> data) {
        this.currentInteractionType = type;
        this.interactionPrompt = prompt;
        this.interactionState = data != null ? data : new HashMap<>();
    }

    public void clearInteraction() {
        this.currentInteractionType = INTERACTION_NONE;
        this.interactionPrompt = null;
        this.interactionState.clear();
    }

    // Getters
    public boolean isAwaitingInteraction() {
        return !INTERACTION_NONE.equals(currentInteractionType);
    }
    public String getCurrentInteractionType() { return currentInteractionType; }
    public String getInteractionPrompt() { return interactionPrompt; }
    public <T> T getInteractionData(String key) { return (T) interactionState.get(key); }

    public List<Portal> getPortals() {
        return portals;
    }

    public void setPortals(List<Portal> portals) {
        this.portals = portals;
    }

    public String getAncientId() {
        return ancientId;
    }

    public void setAncientId(String ancientId) {
        this.ancientId = ancientId;
    }
}