package com.eldritch.game;

import com.eldritch.staticdata.ConfigService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SpellService {
    private static final int SPELLS_SIZE = 20;

    public List<Spell> getSpells() {

        Properties props = ConfigService.getProperties("spells.properties");
        List<Spell> spells = new ArrayList<>();

        for (int i = 1; i <= SPELLS_SIZE; i++) {
            String prefix = "spell" + String.format("%02d", i) + ".";
            Spell spell = new Spell(
                    props.getProperty(prefix + "id"),
                    props.getProperty(prefix + "name"),
                    props.getProperty(prefix + "test"),
                    parseEffects(props.getProperty(prefix + "success")),
                    parseEffects(props.getProperty(prefix + "fail")),
                    Boolean.parseBoolean(props.getProperty(prefix + "requiresTarget")),
                    Arrays.asList(props.getProperty(prefix + "targetTypes", "").split(",")),
                    props.getProperty(prefix + "aiPriority", "general"),
                    Integer.parseInt(props.getProperty(prefix + "aiWeight", "50"))
            );
            spells.add(spell);
        }
        return spells;
    }

    private Map<String, String> parseEffects(String effectString) {
        Map<String, String> effects = new HashMap<>();
        if (effectString != null) {
            for (String effect : effectString.split(";")) {
                String[] parts = effect.split(":");
                if (parts.length == 2) {
                    effects.put(parts[0], parts[1]);
                }
            }
        }
        return effects;
    }

    public void invokeSpell(Spell spell) {

    }
}