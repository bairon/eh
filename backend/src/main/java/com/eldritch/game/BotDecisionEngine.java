package com.eldritch.game;

import java.util.List;
import java.util.Random;

public class BotDecisionEngine {
    private final Random random = new Random();

    public BotDecision makeSpellDecision(GameState state, Player bot) {
        List<Spell> availableSpells = bot.getSpells();

        // Simple logic - cast first available spell 60% of the time
        if (!availableSpells.isEmpty() && random.nextDouble() < 0.6) {
            Spell spell = availableSpells.get(0);
            return new BotDecision("cast", spell.getId());
        }
        return new BotDecision();
    }

    // More sophisticated example:
    public BotDecision makeTargetDecision(GameState state, List<Location> options) {
        // Prefer locations with clues
        return options.stream()
                .filter(loc -> loc.hasClues())
                .findFirst()
                .map(loc -> new BotDecision("select", loc.getId()))
                .orElse(new BotDecision("select", options.get(0).getId()));
    }
}
