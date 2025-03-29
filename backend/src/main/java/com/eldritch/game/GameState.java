package com.eldritch.game;

import com.eldritch.ancientone.AncientOne;

import java.util.List;

public class GameState {

    private String ancientId;
    private List<Portal> portals; // List of portal tokens


    public List<Portal> getPortals() {
        return portals;
    }

    public void setPortals(List<Portal> portals) {
        this.portals = portals;
    }

    public String getAncientId() {
        return ancientId;
    }

    public void setAncientId(String ancientId) {
        this.ancientId = ancientId;
    }
}