package com.eldritch.engine;

import com.eldritch.ancientone.AncientOne;
import com.eldritch.common.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class EldritchHorrorGame {
    private static final Logger logger = LogManager.getLogger(EldritchHorrorGame.class);
    private final GameState gameState;
    private final EventBus eventBus;
    private final GameServer gameServer;

    public static void main(String[] args) {
        // Initialize game
        EldritchHorrorGame game = new EldritchHorrorGame();

        // Start game server
        //game.getGameServer().start();

        // Begin game loop
        game.startGame();
    }

    private GameServer getGameServer() {
        return gameServer;
    }

    public EldritchHorrorGame() {
        // Initialize core components
        this.gameServer = new GameServer();
        this.eventBus = new SimpleEventBus();
        this.gameState = new PersistentGameState();


        // Setup game board
        initializeGame();
    }

    private void initializeGame() {
        gameState.initializeNewGame();
        // 1. Choose Ancient One
        AncientOne ancientOne = new AncientOne();
        gameState.setAncientOne(ancientOne);

        // 2. Initialize investigators
        initializeInvestigators();

        // 4. Set starting conditions
        gameState.setCurrentPhase(GamePhase.UPKEEP);
        gameState.incrementRound(); // Round 1

        logger.info("Game initialized with {} investigators",
                gameState.getInvestigators().size());
    }

    private void initializeInvestigators() {
        Investigator[] startingInvestigators = {
                new Investigator("Amanda Sharpe"),
                new Investigator("Harvey Walters"),
                new Investigator("Michael McGlen")
        };

        for (Investigator investigator : startingInvestigators) {
            gameState.addInvestigator(investigator);
            // Place investigators in starting location
            gameState.applyEvent(new InvestigatorMovedEvent(
                    null, // System event
                    investigator.getId(),
                    UUID.fromString("00000000-0000-0000-0000-000000000001") // Arkham
            ));
        }
    }

    public void startGame() {
        logger.info("Starting Eldritch Horror game");

        // Begin first round
        while (!isGameOver()) {
            executeGameRound();
        }

        endGame();
    }

    private void executeGameRound() {
        // 1. Upkeep Phase
        gameState.setCurrentPhase(GamePhase.UPKEEP);
        eventBus.publish(new PhaseStartedEvent(GamePhase.UPKEEP));
        resolveUpkeep();

        // 2. Action Phase
        gameState.setCurrentPhase(GamePhase.ACTION);
        eventBus.publish(new PhaseStartedEvent(GamePhase.ACTION));
        resolveActionPhase();

        // 3. Encounter Phase
        gameState.setCurrentPhase(GamePhase.ENCOUNTER);
        eventBus.publish(new PhaseStartedEvent(GamePhase.ENCOUNTER));
        //resolveEncounters();

        // 4. Mythos Phase
        gameState.setCurrentPhase(GamePhase.MYTHOS);
        eventBus.publish(new PhaseStartedEvent(GamePhase.MYTHOS));
        resolveMythosPhase();

        // Advance round
        gameState.incrementRound();
    }

    private void resolveMythosPhase() {
        // Draw and resolve mythos card
        if (!gameState.getMythosDeck().isEmpty()) {
            UUID cardId = gameState.getMythosDeck().remove(0);
            MythosCard card = (MythosCard) gameState.getCard(cardId);

            GameEvent drawnEvent = card.play(null);
            eventBus.publish(drawnEvent);

            GameEvent resolvedEvent = card.resolve();
            eventBus.publish(resolvedEvent);

            // Move to discard pile
            gameState.getDiscardPile().add(cardId);
        }
    }
    public void handlePlayerAction(UUID playerId, GameAction action) {
        if (gameState.getCurrentPhase() == GamePhase.ACTION) {
            GameActionEvent actionEvent = new GameActionEvent(action, playerId);
            eventBus.publish(actionEvent);
        } else {
            eventBus.publish(new ActionRejectedEvent(
                    null, // No original event ID
                    "Actions can only be taken during the Action phase"
            ));
        }
    }
    private boolean isGameOver() {
        // Check for Ancient One awakening
        if (gameState.getAncientOne().getDoom() <= 0) {
            logger.info("Ancient One has awakened! Game over.");
            return true;
        }

        // Check if all investigators are defeated
        boolean allDefeated = gameState.getInvestigators().stream()
                .allMatch(inv -> inv.getHealth() <= 0 || inv.getSanity() <= 0);

        if (allDefeated) {
            logger.info("All investigators are defeated! Game over.");
        }

        return allDefeated;
    }

    private void endGame() {
        boolean playersWon = gameState.getAncientOne().getDoom() > 0;
        String result = playersWon ? "Investigators win!" : "Ancient One awakens!";

        eventBus.publish(new GameOverEvent(result));
        logger.info("Game ended. Result: {}", result);

        // Cleanup resources
        //gameServer.stop();
    }
    private void resolveUpkeep() {
        logger.debug("Resolving upkeep phase");

        // 1. Refresh investigator actions
        gameState.getInvestigators().forEach(Investigator::resetActions);

        // 2. Handle ongoing effects
        eventBus.publish(new OngoingEffectsEvent(null));

        // 3. Draw starting resources
        eventBus.publish(new ResourcePhaseEvent(null));
    }

    private void resolveActionPhase() {
        logger.debug("Resolving action phase");
        // This phase is primarily player-driven
        // The server just waits for action events

        // Set timeout for action phase (e.g., 2 minutes per player)
        //new Timer().schedule(new TimerTask() {
        //    @Override
        //    public void run() {
        //        eventBus.publish(new PhaseTimeoutEvent(GamePhase.ACTION));
        //    }
        //}, calculateActionPhaseDuration());
    }

/*    private void resolveEncounters() {
        logger.debug("Resolving encounters");

        // Process encounters for each investigator
        gameState.getInvestigators().stream()
                .filter(inv -> inv.getHealth() > 0 && inv.getSanity() > 0)
                .forEach(inv -> {
                    UUID locationId = inv.getLocationId();
                    Location location = gameState.getLocation(locationId);

                    // Draw encounter based on location type
                    Encounter encounter = drawEncounter(location.getType());
                    eventBus.publish(new EncounterEvent(inv.getId(), encounter));
                });
    }

    private Encounter drawEncounter(LocationType type) {
        // Implement your encounter deck logic
        return new Encounter("Strange Tome",
                "You find an ancient book. Test Lore (3) to gain 1 clue.",
                Attribute.LORE, 3,
                new GainClueEffect(1));
    }
 */
}
