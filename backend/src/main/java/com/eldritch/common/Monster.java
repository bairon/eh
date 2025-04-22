package com.eldritch.common;

import java.util.UUID;

public class Monster {
    private final UUID id;
    private final String name;
    private final MonsterType type;
    private final int toughness;
    private final int horrorRating;

    public Monster(String name, MonsterType type, int toughness, int horrorRating) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.type = type;
        this.toughness = toughness;
        this.horrorRating = horrorRating;
    }

    // Getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public MonsterType getType() { return type; }
    public int getToughness() { return toughness; }
    public int getHorrorRating() { return horrorRating; }
}