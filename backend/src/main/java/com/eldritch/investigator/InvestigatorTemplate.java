package com.eldritch.investigator;

public class InvestigatorTemplate {
    private String id;
    private String image;
    private String imageback;
    private String description;
    private String location;
    private String bonus;
    private int lore;
    private int influence;
    private int observation;
    private int strength;
    private int will;
    private String clues;
    private String assets;
    private String spells;

    public InvestigatorTemplate() {
    }

    public InvestigatorTemplate(String id, String image, String imageback, String description,
                                String location, String bonus, int lore, int influence,
                                int observation, int strength, int will, String clues,
                                String assets, String spells) {
        this.id = id;
        this.image = image;
        this.imageback = imageback;
        this.description = description;
        this.location = location;
        this.bonus = bonus;
        this.lore = lore;
        this.influence = influence;
        this.observation = observation;
        this.strength = strength;
        this.will = will;
        this.clues = clues;
        this.assets = assets;
        this.spells = spells;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getImageback() {
        return imageback;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getBonus() {
        return bonus;
    }

    public int getLore() {
        return lore;
    }

    public int getInfluence() {
        return influence;
    }

    public int getObservation() {
        return observation;
    }

    public int getStrength() {
        return strength;
    }

    public int getWill() {
        return will;
    }

    public String getClues() {
        return clues;
    }

    public String getAssets() {
        return assets;
    }

    public String getSpells() {
        return spells;
    }
}