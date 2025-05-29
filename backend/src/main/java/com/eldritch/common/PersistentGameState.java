package com.eldritch.common;

import com.eldritch.ancientone.AncientOne;
import com.eldritch.engine.GamePhase;
import com.eldritch.engine.GameStateSnapshot;
import com.eldritch.logic.Investigator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class PersistentGameState implements GameState {
    private static final Logger logger = LogManager.getLogger(PersistentGameState.class);
    private static final String SAVE_FILE = "D:/Projects/eh/game_state.sav";

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    // Core game state
    private GamePhase currentPhase;
    private int roundNumber;
    private final Map<UUID, Investigator> investigators;
    private AncientOne ancientOne;
    private final List<UUID> mythosDeck;
    private final List<UUID> discardPile;

    // Additional game elements
    private final Map<UUID, Location> locations;
    private final Map<UUID, Monster> monsters;
    private final Map<UUID, Card> cards;

    public PersistentGameState() {
        this.investigators = new HashMap<>();
        this.mythosDeck = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        this.locations = new HashMap<>();
        this.monsters = new HashMap<>();
        this.cards = new HashMap<>();

        //loadState();
    }

    @Override
    public GamePhase getCurrentPhase() {
        readLock.lock();
        try {
            return currentPhase;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void setCurrentPhase(GamePhase phase) {
        writeLock.lock();
        try {
            this.currentPhase = phase;
            //saveState();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public int getRoundNumber() {
        readLock.lock();
        try {
            return roundNumber;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void incrementRound() {
        writeLock.lock();
        try {
            this.roundNumber++;
            //saveState();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public List<Investigator> getInvestigators() {
        readLock.lock();
        try {
            return new ArrayList<>(investigators.values());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Investigator getInvestigator(UUID investigatorId) {
        readLock.lock();
        try {
            return investigators.get(investigatorId);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void addInvestigator(Investigator investigator) {
        writeLock.lock();
        try {
            //investigators.put(investigator.getId(), investigator);
            //saveState();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public AncientOne getAncientOne() {
        readLock.lock();
        try {
            return ancientOne;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void setAncientOne(AncientOne ancientOne) {
        writeLock.lock();
        try {
            this.ancientOne = ancientOne;
            //saveState();
        } finally {
            writeLock.unlock();
        }
    }
    // Location methods
    @Override
    public List<Location> getLocations() {
        readLock.lock();
        try {
            return new ArrayList<>(locations.values());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Location getLocation(UUID locationId) {
        readLock.lock();
        try {
            return locations.get(locationId);
        } finally {
            readLock.unlock();
        }
    }

    // Card deck methods
    @Override
    public List<UUID> getMythosDeck() {
        readLock.lock();
        try {
            return new ArrayList<>(mythosDeck);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<UUID> getDiscardPile() {
        readLock.lock();
        try {
            return new ArrayList<>(discardPile);
        } finally {
            readLock.unlock();
        }
    }

    // Monster methods
    @Override
    public List<Monster> getMonsters() {
        readLock.lock();
        try {
            return new ArrayList<>(monsters.values());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Monster getMonster(UUID monsterId) {
        readLock.lock();
        try {
            return monsters.get(monsterId);
        } finally {
            readLock.unlock();
        }
    }

    // Card methods
    @Override
    public Card getCard(UUID cardId) {
        readLock.lock();
        try {
            return cards.get(cardId);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean canMoveInvestigator(UUID originId, UUID investigatorId, UUID destinationId) {
        return true;
    }

    @Override
    public void applyEvent(GameEvent event) {
        writeLock.lock();
        try {
            // Handle different event types
            switch (event.getEventType()) {
                case "INVESTIGATOR_MOVED":
                    handleInvestigatorMoved(event);
                    break;
                case "SANITY_CHANGED":
                    handleSanityChanged(event);
                    break;
                case "HEALTH_CHANGED":
                    handleHealthChanged(event);
                    break;
                case "DOOM_CHANGED":
                    handleDoomChanged(event);
                    break;
                // Add other event types as needed
                default:
                    logger.warn("Unhandled event type: {}", event.getEventType());
            }

            //saveState();
        } finally {
            writeLock.unlock();
        }
    }

    private void handleInvestigatorMoved(GameEvent event) {
        if (!(event instanceof InvestigatorMovedEvent moveEvent)) {
            logger.error("Received wrong event type for investigator movement: {}", event.getEventType());
            return;
        }

        UUID investigatorId = moveEvent.getInvestigatorId();
        UUID locationId = moveEvent.getDestinationId();

        Investigator investigator = investigators.get(investigatorId);
        if (investigator != null) {
            //investigator.setLocationId(locationId);
            logger.debug("Investigator {} moved to location {}", investigatorId, locationId);
        } else {
            logger.warn("Attempted to move non-existent investigator: {}", investigatorId);
        }
    }

    private void handleSanityChanged(GameEvent event) {
        if (!(event instanceof SanityChangedEvent sanityEvent)) {
            logger.error("Received wrong event type for sanity change: {}", event.getEventType());
            return;
        }

        UUID investigatorId = sanityEvent.getInvestigatorId();
        int delta = sanityEvent.getDelta();

        Investigator investigator = investigators.get(investigatorId);
        if (investigator != null) {
            //investigator.setSanity(investigator.getSanity() + delta);
            logger.debug("Investigator {} sanity changed by {}", investigatorId, delta);
        } else {
            logger.warn("Attempted to change sanity for non-existent investigator: {}", investigatorId);
        }
    }

    private void handleHealthChanged(GameEvent event) {
        if (!(event instanceof HealthChangedEvent healthEvent)) {
            logger.error("Received wrong event type for health change: {}", event.getEventType());
            return;
        }

        UUID investigatorId = healthEvent.getInvestigatorId();
        int delta = healthEvent.getDelta();

        Investigator investigator = investigators.get(investigatorId);
        if (investigator != null) {
            //investigator.setHealth(investigator.getHealth() + delta);
            logger.debug("Investigator {} health changed by {}", investigatorId, delta);
        } else {
            logger.warn("Attempted to change health for non-existent investigator: {}", investigatorId);
        }
    }

    private void handleDoomChanged(GameEvent event) {
        if (!(event instanceof DoomChangedEvent doomEvent)) {
            logger.error("Received wrong event type for doom change: {}", event.getEventType());
            return;
        }

        int delta = doomEvent.getDelta();

        if (ancientOne != null) {
            ancientOne.setDoom(ancientOne.getDoom() + delta);
            logger.debug("Doom track changed by {}", delta);
        } else {
            logger.warn("Attempted to change doom without Ancient One being set");
        }
    }
    @Override
    public GameStateSnapshot getSnapshot() {
        readLock.lock();
        try {
            return new GameStateSnapshot(
                    currentPhase,
                    roundNumber,
                    new HashMap<>(investigators),
                    ancientOne,
                    new ArrayList<>(mythosDeck),
                    new ArrayList<>(discardPile),
                    new HashMap<>(locations),
                    new HashMap<>(monsters),
                    new HashMap<>(cards)
            );
        } finally {
            readLock.unlock();
        }
    }

    private void saveState() {
        writeLock.lock();
        try {
            Path path = Paths.get(SAVE_FILE);
            try (ObjectOutputStream out = new ObjectOutputStream(
                    Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {

                GameStateSnapshot snapshot = getSnapshot();
                out.writeObject(snapshot);
                logger.info("Game state saved successfully");

            } catch (IOException e) {
                logger.error("Failed to save game state", e);
            }
        } finally {
            writeLock.unlock();
        }
    }

    private void loadState() {
        Path path = Paths.get(SAVE_FILE);
        if (!Files.exists(path)) {
            logger.info("No saved game state found, initializing new game");
            initializeNewGame();
            return;
        }

        readLock.lock();
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {

            GameStateSnapshot snapshot = (GameStateSnapshot) in.readObject();

            // Restore state from snapshot
            this.currentPhase = snapshot.getCurrentPhase();
            this.roundNumber = snapshot.getRoundNumber();
            this.investigators.clear();
            this.investigators.putAll(snapshot.getInvestigators());
            this.ancientOne = snapshot.getAncientOne();
            this.mythosDeck.clear();
            this.mythosDeck.addAll(snapshot.getMythosDeck());
            this.discardPile.clear();
            this.discardPile.addAll(snapshot.getDiscardPile());
            this.locations.clear();
            this.locations.putAll(snapshot.getLocations());
            this.monsters.clear();
            this.monsters.putAll(snapshot.getMonsters());
            this.cards.clear();
            this.cards.putAll(snapshot.getCards());

            logger.info("Game state loaded successfully");

        } catch (IOException | ClassNotFoundException e) {
            logger.error("Failed to load game state, initializing new game", e);
            initializeNewGame();
        } finally {
            readLock.unlock();
        }
    }
    @Override
    public void initializeNewGame() {
        writeLock.lock();
        try {
            this.currentPhase = GamePhase.UPKEEP;
            this.roundNumber = 0;
            this.investigators.clear();
            this.mythosDeck.clear();
            this.discardPile.clear();
            this.locations.clear();
            this.monsters.clear();
            this.cards.clear();

            // Initialize game board, locations, etc.
            initializeGameBoard();

            //saveState();
        } finally {
            writeLock.unlock();
        }
    }
    private void initializeGameBoard() {
        writeLock.lock();
        try {
            logger.info("Initializing new game board");

            // Clear any existing state
            locations.clear();
            monsters.clear();
            mythosDeck.clear();
            discardPile.clear();

            // 1. Initialize all standard locations
            initializeStandardLocations();

            // 2. Set up location connections
            initializeLocationConnections();

            // 3. Place starting monsters
            initializeStartingMonsters();

            // 4. Set up initial mythos deck
            initializeMythosDeck();

            // 5. Set up other game elements
            initializeOtherGameElements();

            logger.info("Game board initialization complete with {} locations, {} monsters, and {} mythos cards",
                    locations.size(), monsters.size(), mythosDeck.size());
        } finally {
            writeLock.unlock();
        }
    }

    private void initializeStandardLocations() {
        // Create all standard game locations with proper coordinates
        Location arkham = new Location("Arkham", LocationType.CITY, 1, 1);
        Location tokyo = new Location("Tokyo", LocationType.CITY, 8, 3);
        Location london = new Location("London", LocationType.CITY, 2, 4);
        Location sydney = new Location("Sydney", LocationType.CITY, 10, 6);
        Location newYork = new Location("New York", LocationType.CITY, 4, 2);
        Location pyramids = new Location("The Pyramids", LocationType.WILDERNESS, 6, 5);
        Location amazon = new Location("The Amazon", LocationType.WILDERNESS, 3, 7);
        Location himalayas = new Location("The Himalayas", LocationType.WILDERNESS, 7, 2);
        Location antarctica = new Location("Antarctica", LocationType.WILDERNESS, 5, 8);
        Location dreamlands = new Location("The Dreamlands", LocationType.OTHERWORLD, 9, 4);

        // Add to locations map
        locations.put(arkham.getId(), arkham);
        locations.put(tokyo.getId(), tokyo);
        locations.put(london.getId(), london);
        locations.put(sydney.getId(), sydney);
        locations.put(newYork.getId(), newYork);
        locations.put(pyramids.getId(), pyramids);
        locations.put(amazon.getId(), amazon);
        locations.put(himalayas.getId(), himalayas);
        locations.put(antarctica.getId(), antarctica);
        locations.put(dreamlands.getId(), dreamlands);
    }

    private void initializeLocationConnections() {
        // Get location references
        Location arkham = locations.values().stream().filter(l -> l.getName().equals("Arkham")).findFirst().orElse(null);
        Location newYork = locations.values().stream().filter(l -> l.getName().equals("New York")).findFirst().orElse(null);
        Location tokyo = locations.values().stream().filter(l -> l.getName().equals("Tokyo")).findFirst().orElse(null);
        Location himalayas = locations.values().stream().filter(l -> l.getName().equals("The Himalayas")).findFirst().orElse(null);

        // Set up connections (bidirectional)
        if (arkham != null && newYork != null) {
            arkham.addConnection(newYork.getId());
            newYork.addConnection(arkham.getId());
        }

        if (tokyo != null && himalayas != null) {
            tokyo.addConnection(himalayas.getId());
            himalayas.addConnection(tokyo.getId());
        }

        // Add more connections as needed...
    }

    private void initializeStartingMonsters() {
        // Create initial monsters
        Monster cultist = new Monster("Cultist", MonsterType.CULTIST, 2, 2);
        Monster deepOne = new Monster("Deep One", MonsterType.NIGHTMARE, 3, 3);

        // Add to monsters map
        monsters.put(cultist.getId(), cultist);
        monsters.put(deepOne.getId(), deepOne);

        // Place monsters on locations
        Location arkham = locations.values().stream().filter(l -> l.getName().equals("Arkham")).findFirst().orElse(null);
        Location sydney = locations.values().stream().filter(l -> l.getName().equals("Sydney")).findFirst().orElse(null);

        if (arkham != null) {
            arkham.addMonster(cultist.getId());
        }
        if (sydney != null) {
            sydney.addMonster(deepOne.getId());
        }
    }

    private void initializeMythosDeck() {
        // Create initial mythos cards
        Card rumors = new MythosCard("Rumors from the Orient", MythosType.GREEN, new SpawnMonsterEffect(MonsterType.BEAST, UUID.randomUUID()), 1, true);
        Card sightings = new MythosCard("Strange Sightings", MythosType.YELLOW, new SpawnMonsterEffect(MonsterType.BEAST, UUID.randomUUID()), 1, true);
        Card cultActivity = new MythosCard("Cult Activity", MythosType.BLUE, new SpawnMonsterEffect(MonsterType.BEAST, UUID.randomUUID()), 1, true);

        // Add to cards map and deck
        cards.put(rumors.getId(), rumors);
        cards.put(sightings.getId(), sightings);
        cards.put(cultActivity.getId(), cultActivity);

        mythosDeck.add(rumors.getId());
        mythosDeck.add(sightings.getId());
        mythosDeck.add(cultActivity.getId());

        // Shuffle the deck
        Collections.shuffle(mythosDeck);
    }
    public MythosCard getMythosCard(UUID cardId) {
        Card card = cards.get(cardId);
        return (card != null && card.getType() == CardType.MYTHOS) ? (MythosCard) card : null;
    }
    private void initializeOtherGameElements() {
        // Place starting clues
        Location pyramids = locations.values().stream().filter(l -> l.getName().equals("The Pyramids")).findFirst().orElse(null);
        Location himalayas = locations.values().stream().filter(l -> l.getName().equals("The Himalayas")).findFirst().orElse(null);

        if (pyramids != null) {
            pyramids.setClueCount(1);
        }
        if (himalayas != null) {
            himalayas.setClueCount(1);
        }

        // Initialize other game state as needed
        this.roundNumber = 0;
        this.currentPhase = GamePhase.UPKEEP;
    }
}