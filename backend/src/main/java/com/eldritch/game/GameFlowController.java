package com.eldritch.game;

import com.eldritch.session.GameSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.eldritch.game.GameState.GamePhase.*;

@Service
public class GameFlowController {

    public void advanceGame(GameSession session) {
        GameState state = session.getGameState();

        switch(state.getCurrentPhase()) {
            case MYTHOS_PHASE:
                //handleMythosPhase(session);
                break;

            case ACTION_PHASE:
                //handleActionPhase(session);
                break;

            case ENCOUNTER_PHASE:
                //handleEncounterPhase(session);
                break;

            // ... other phases
        }

        state.advancePhase();
        //checkGameEndConditions(session);
    }

    private void handleActionPhase(GameSession session) {
        // Process each player's actions
        for (Player player : session.getPlayers()) {
            if (player instanceof BotPlayer) {
                handleBotActions(session, (BotPlayer)player);
            }
            // Human players will trigger actions through API calls
        }
    }

    private void handleBotActions(GameSession session, BotPlayer bot) {
        // Get bot decisions
        /*List<PlayerAction> actions = bot.generateActions(session.getGameState());

        // Execute each action
        for (PlayerAction action : actions) {
            switch(action.getType()) {
                case MOVE:
                    movePlayer(session, bot, action.getData());
                    break;
                case CAST_SPELL:
                    castSpell(session, bot, action.getData());
                    break;
                // ... other action types
            }
        }*/
    }

    public void forcePhaseAdvance(GameSession session) {

    }
}
