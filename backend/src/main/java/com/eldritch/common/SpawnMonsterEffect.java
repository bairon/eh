package com.eldritch.common;

import java.util.UUID;

// Example implementation:
public class SpawnMonsterEffect implements MythosEffect {
    private final MonsterType monsterType;
    private final UUID locationId;

    public SpawnMonsterEffect(MonsterType monsterType, UUID locationId) {
        this.monsterType = monsterType;
        this.locationId = locationId;
    }

    @Override
    public void apply(GameState state) {
        Monster monster = new Monster("", monsterType, 1, 1);
        state.getLocation(locationId).addMonster(monster.getId());
    }

    @Override
    public void onDiscard() {
        // Cleanup if needed
    }

    @Override
    public boolean shouldTrigger(int currentDoom) {
        return true; // Always trigger or implement doom-based logic
    }
}