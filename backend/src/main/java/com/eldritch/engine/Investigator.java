package com.eldritch.engine;

import java.util.List;
import java.util.UUID;

public class Investigator extends GameObject {
    private int health;
    private int sanity;
    private UUID locationId;
    private List<UUID> inventory;
    private List<UUID> conditions;
    private UUID playerId; // Links to controlling player

    public Investigator(String name) {
        super(name);
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

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }

    public List<UUID> getInventory() {
        return inventory;
    }

    public void setInventory(List<UUID> inventory) {
        this.inventory = inventory;
    }

    public List<UUID> getConditions() {
        return conditions;
    }

    public void setConditions(List<UUID> conditions) {
        this.conditions = conditions;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public void resetActions() {

    }
// Getters and setters
}