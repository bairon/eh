package com.eldritch.common;

import java.util.*;

public class Location {
    private final UUID id;
    private final String name;
    private final LocationType type;
    private final int x;
    private final int y;
    private final Set<UUID> connections = new HashSet<>();
    private final Set<UUID> monsters = new HashSet<>();
    private int clueCount = 0;

    public Location(String name, LocationType type, int x, int y) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    // Getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public LocationType getType() { return type; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Set<UUID> getConnections() { return Collections.unmodifiableSet(connections); }
    public Set<UUID> getMonsters() { return Collections.unmodifiableSet(monsters); }
    public int getClueCount() { return clueCount; }

    // Methods
    public void addConnection(UUID locationId) {
        connections.add(locationId);
    }

    public void addMonster(UUID monsterId) {
        monsters.add(monsterId);
    }

    public void removeMonster(UUID monsterId) {
        monsters.remove(monsterId);
    }

    public void setClueCount(int count) {
        this.clueCount = count;
    }
}