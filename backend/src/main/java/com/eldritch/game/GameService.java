package com.eldritch.game;

import com.eldritch.event.MythosEvent;
import com.eldritch.event.OmenChangeEvent;
import com.eldritch.event.ReckoningEvent;
import com.eldritch.investigator.InvBase;
import com.eldritch.investigator.InvestigatorFactory;
import com.eldritch.investigator.InvestigatorService;
import com.eldritch.investigator.InvestigatorTemplate;
import com.eldritch.session.GameSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameService {

    private static final int MOVE = 0;
    private static final int CAST_SPELL = 1;
    private GameState gameState;

    @Autowired
    private InvestigatorService investigatorService;

    @Autowired
    private PortalService portalService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private SpellService spellService;


    public void start(GameSession gameSession) {
        gameSession.setGameStatus(GameStatus.ONGOING);

        initDepo(gameSession);
        initInvestigators(gameSession);

    }

    public void initDepo(GameSession gameSession) {
        gameSession.setDepo(new Depo(
                portalService.getPortals(),
                clueService.getClues(),
                assetService.getAssets(),
                spellService.getSpells()
                )
        );
    }


    private void initInvestigators(GameSession gameSession) {
        List<Player> players = gameSession.getPlayers();
        Depo depo = gameSession.getDepo();

        for (Player player : players) {
            String investigatorId = player.getInvestigatorId();
            InvBase investigator = InvestigatorFactory.create(investigatorId);
            InvestigatorTemplate template = investigatorService.getInvestigators()
                    .stream()
                    .filter(inv -> inv.getId().equals(investigatorId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Investigator template not found: " + investigatorId));

            // Initialize stats
            investigator.setLore(template.getLore());
            investigator.setInfluence(template.getInfluence());
            investigator.setObservation(template.getObservation());
            investigator.setStrength(template.getStrength());
            investigator.setWill(template.getWill());

            // Initialize starting clues
            if (template.getClues() != null && !template.getClues().isEmpty()) {
                for (String clueId : template.getClues().split(",")) {
                    Clue clue = depo.acquireClue();
                    if (clue != null) {
                        investigator.addClue(clue);
                    }
                }
            }

            // Initialize starting assets
            if (template.getAssets() != null && !template.getAssets().isEmpty()) {
                for (String assetId : template.getAssets().split(",")) {
                    Asset asset = depo.acuireAsset(assetId.trim());
                    if (asset != null) {
                        investigator.addAsset(asset);
                    }
                }
            }

            // Initialize starting spells
            if (template.getSpells() != null && !template.getSpells().isEmpty()) {
                // Handle spell options (| means OR, || means AND)
                String[] spellOptions = template.getSpells().split("\\|\\|");
                for (String optionGroup : spellOptions) {
                    String[] spells = optionGroup.split("\\|");
                    // For now, just take the first available spell in each OR group
                    boolean spellAdded = false;
                    for (String spellId : spells) {
                        Asset spell = depo.acuireAsset(spellId.trim());
                        if (spell != null) {
                            investigator.addSpell(spell);
                            spellAdded = true;
                            break;
                        }
                    }
                }
            }

            // Initialize bonus if present
            if (template.getBonus() != null && !template.getBonus().isEmpty()) {
                investigator.setBonus(template.getBonus());
            }

            player.setInvestigator(investigator);
        }
    }

    public void castSpell(GameSession session, String spellId, String playerId) {
        Player player = session.getPlayerById(playerId);
        Spell spell = session.getDepo().acuireSpell(spellId);

        // Set up initial interaction state
        Map<String, Object> interaction = new HashMap<>();
        interaction.put("spellId", spellId);
        interaction.put("playerId", playerId);
        interaction.put("requiredTest", spell.getTest());

        session.getGameState().setInteraction(
                GameState.INTERACTION_SPELL_CAST,
                "Cast " + spell.getName() + "? (Test: " + spell.getTest() + ")",
                interaction
        );
    }

    public void handleSpellInteraction(GameSession session, String playerId, String interactionType, Map<String, Object> response) {
        switch (interactionType) {
            case "spell_test_result":
                handleSpellTestResult(session, response);
                break;
            case "spell_target":
                handleSpellTarget(session, response);
                break;
            case "spell_choice":
                handleSpellChoice(session, response);
                break;
        }
    }

    private void handleSpellChoice(GameSession session, Map<String, Object> response) {

    }

    private void handleSpellTarget(GameSession session, Map<String, Object> response) {

    }

    private void handleSpellTestResult(GameSession session, Map<String, Object> response) {
        int successes = (int) response.get("successes");
        String spellId = (String) response.get("spellId");
        Spell spell = session.getDepo().acuireSpell(spellId);

        if (successes >= spell.getRequiredSuccesses()) {
            // Success - request target if needed
            if (spell.requiresTarget()) {
                Map<String, Object> nextInteraction = new HashMap<>();
                // ... populate target data ...
                session.getGameState().setInteraction(
                        GameState.INTERACTION_SPELL_TARGET,
                        "Select target for " + spell.getName(),
                        nextInteraction
                );
            }
        } else {
            // Handle failure effects
            applySpellFailure(session, spell, successes);
            session.getGameState().clearInteraction();
        }
    }

    private void applySpellFailure(GameSession session, Spell spell, int successes) {

    }
    public GameState getGameState() {
        return gameState;
    }

    public void handlePlayerAction(GameSession session, String playerId, PlayerAction action) {
        //validatePlayerTurn(session, playerId);

        switch(action.getType()) {
            case MOVE:
                //handleMoveAction(session, playerId, action);
                break;
            case CAST_SPELL:
                //handleSpellAction(session, playerId, action);
                break;
            // ... other actions
        }

        checkPhaseCompletion(session);
    }

    private void checkPhaseCompletion(GameSession session) {
        GameState state = session.getGameState();
        if (allPlayersCompletedActions(session)) {
            //gameFlowController.advanceGame(session);
        }
    }

    private boolean allPlayersCompletedActions(GameSession session) {
        return false;
    }

}