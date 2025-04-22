package com.eldritch.game;

import java.util.List;

public class Location {
    private String id;
    private String train;
    private String ship;
    private String uncharted;
    private String space;
    private List<String> connections;

    public Location(String id, String train, String ship, String uncharted, String space, List<String> connections) {
        this.id = id;
        this.train = train;
        this.ship = ship;
        this.uncharted = uncharted;
        this.space = space;
        this.connections = connections;
    }

    public String getId() {
        return id;
    }

    public List<String> getConnections() {
        return connections;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", train='" + train + '\'' +
                ", ship='" + ship + '\'' +
                ", uncharted='" + uncharted + '\'' +
                ", space='" + space + '\'' +
                ", connections=" + connections +
                '}';
    }

    public boolean hasClues() {
        return false;
    }
}