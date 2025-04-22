package com.eldritch.game;

import com.eldritch.investigator.InvBase;
import com.eldritch.session.GameSession;
import com.eldritch.user.UserData;

import java.util.List;

public abstract class Player {
    private String playerId;
    private String name;
    private String location;
    private int health;
    private int sanity;
    private List<String> items;
    private String investigatorName;
    private boolean master;
    private String investigatorId;
    private InvBase investigator;
    protected PlayerType type;
    private List<Spell> spells;

    public List<Spell> getSpells() {
        return spells;
    }

    public void setSpells(List<Spell> spells) {
        this.spells = spells;
    }

    public enum PlayerType {
        HUMAN,
        BOT_EASY,
        BOT_MEDIUM,
        BOT_HARD
    }

    public abstract void handleGameStateUpdate(GameSession session);

    public Player() {
    }

    public Player(String name, String location, int health, int sanity, List<String> items) {
        this.name = name;
        this.location = location;
        this.health = health;
        this.sanity = sanity;
        this.items = items;
    }

    public Player(String name, String investigatorName) {
        this.name = name;
        this.investigatorName = investigatorName;

    }

    public Player(String id) {
        this.playerId = id;
    }

    public Player(UserData userData) {
        this.playerId = userData.getId();
        this.name = userData.getNickname();
    }

    public String getInvestigatorName() {
        return investigatorName;
    }

    public void setInvestigatorName(String investigatorName) {
        this.investigatorName = investigatorName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSanity() {
        return sanity;
    }

    public void setSanity(int sanity) {
        this.sanity = sanity;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public boolean isMaster() {
        return master;
    }

    public void setInvestigatorId(String investigatorId) {
        this.investigatorId = investigatorId;
    }

    public String getInvestigatorId() {
        return investigatorId;
    }

    public void setInvestigator(InvBase investigator) {
        this.investigator = investigator;
    }

    public InvBase getInvestigator() {
        return investigator;
    }
// Getters and Setters
}