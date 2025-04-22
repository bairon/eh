package com.eldritch.game;

public class Monster {
    private String id;
    private String name;
    private Integer testWillModifier;
    private Integer testStrengthModifier;
    private Integer horror;
    private Integer damage;
    private Integer toughnessBase;
    private Integer toughnessModifier;
    private String special;
    private String reckoning;
    private String onSpawn;
    private String onDefeat;
    private String movement;

    public Monster(String id, String name, Integer testWillModifier, Integer testStrengthModifier,
                   Integer horror, Integer damage, Integer toughnessBase, Integer toughnessModifier,
                   String special, String reckoning, String onSpawn, String onDefeat,
                   String movement) {
        this.id = id;
        this.name = name;
        this.testWillModifier = testWillModifier;
        this.testStrengthModifier = testStrengthModifier;
        this.horror = horror;
        this.damage = damage;
        this.toughnessBase = toughnessBase;
        this.toughnessModifier = toughnessModifier;
        this.special = special;
        this.reckoning = reckoning;
        this.onSpawn = onSpawn;
        this.onDefeat = onDefeat;
        this.movement = movement;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public Integer getTestWillModifier() { return testWillModifier; }
    public Integer getTestStrengthModifier() { return testStrengthModifier; }
    public Integer getHorror() { return horror; }
    public Integer getDamage() { return damage; }
    public Integer getToughnessBase() { return toughnessBase; }
    public Integer getToughnessModifier() { return toughnessModifier; }
    public String getSpecial() { return special; }
    public String getReckoning() { return reckoning; }
    public String getOnSpawn() { return onSpawn; }
    public String getOnDefeat() { return onDefeat; }
    public String getMovement() { return movement; }

    // Calculate dynamic toughness
    public int calculateToughness(int investigatorCount) {
        if (toughnessBase == null) return 0;
        int modifier = toughnessModifier != null ? toughnessModifier : 0;
        return toughnessBase.toString().contains("investigators_count") ?
                investigatorCount + modifier :
                toughnessBase + modifier;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", testWillModifier=" + testWillModifier +
                ", testStrengthModifier=" + testStrengthModifier +
                ", horror=" + horror +
                ", damage=" + damage +
                ", toughnessBase=" + toughnessBase +
                ", toughnessModifier=" + toughnessModifier +
                '}';
    }
}