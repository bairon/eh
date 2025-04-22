package com.eldritch.investigator;

import com.eldritch.game.Asset;
import com.eldritch.game.Clue;
import java.util.ArrayList;
import java.util.List;

public abstract class InvBase {
    // Instance variables
    private int lore;
    private int influence;
    private int observation;
    private int strength;
    private int will;
    private String bonus;
    private final List<Clue> clues = new ArrayList<>();
    private final List<Asset> assets = new ArrayList<>();
    private final List<Asset> spells = new ArrayList<>();

    public abstract void act();
    public abstract void ability();

    // Stats setters
    public void setLore(int lore) {
        this.lore = lore;
    }

    public void setInfluence(int influence) {
        this.influence = influence;
    }

    public void setObservation(int observation) {
        this.observation = observation;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setWill(int will) {
        this.will = will;
    }

    // Bonus setter
    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    // Item management
    public void addClue(Clue clue) {
        if (clue != null) {
            this.clues.add(clue);
        }
    }

    public void addAsset(Asset asset) {
        if (asset != null) {
            this.assets.add(asset);
        }
    }

    public void addSpell(Asset spell) {
        if (spell != null) {
            this.spells.add(spell);
        }
    }

    // Getters
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

    public String getBonus() {
        return bonus;
    }

    public List<Clue> getClues() {
        return new ArrayList<>(clues); // Return copy to maintain encapsulation
    }

    public List<Asset> getAssets() {
        return new ArrayList<>(assets); // Return copy to maintain encapsulation
    }

    public List<Asset> getSpells() {
        return new ArrayList<>(spells); // Return copy to maintain encapsulation
    }

    // Utility methods
    public boolean hasClue(String clueId) {
        return clues.stream().anyMatch(c -> c.getId().equals(clueId));
    }

    public boolean hasAsset(String assetId) {
        return assets.stream().anyMatch(a -> a.getId().equals(assetId));
    }

    public boolean hasSpell(String spellId) {
        return spells.stream().anyMatch(s -> s.getId().equals(spellId));
    }
}