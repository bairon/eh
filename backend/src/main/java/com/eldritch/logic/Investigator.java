package com.eldritch.logic;

import com.eldritch.engine.GameObject;
import com.eldritch.game.Asset;
import com.eldritch.game.Clue;
import com.eldritch.investigator.InvestigatorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Investigator {
    private int lore;
    private int influence;
    private int observation;
    private int strength;
    private int will;
    private String bonus;
    private final List<Clue> clues = new ArrayList<>();
    private final List<Asset> assets = new ArrayList<>();
    private final List<Asset> spells = new ArrayList<>();

    public Investigator(InvestigatorTemplate iTmpl) {
        this.lore = iTmpl.getLore();
        this.influence = iTmpl.getInfluence();
        this.observation = iTmpl.getObservation();
        this.strength = iTmpl.getStrength();
        this.will = iTmpl.getWill();
        this.bonus = iTmpl.getBonus();
        iTmpl.getAssets();
        iTmpl.getSpells();
        iTmpl.getClues();
        iTmpl.getDescription();
        iTmpl.getImage();
        iTmpl.getImageback();
        iTmpl.getLocation();
    }

}