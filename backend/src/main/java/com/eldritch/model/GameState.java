package com.eldritch.model;

import java.util.List;

public class GameState {

    private AncientOne ancientOne;
    private List<Portal> portals; // List of portal tokens


    public List<Portal> getPortals() {
        return portals;
    }

    public void setPortals(List<Portal> portals) {
        this.portals = portals;
    }
    public AncientOne getAncientOne() {
        return ancientOne;
    }

    public void setAncientOne(AncientOne ancientOne) {
        this.ancientOne = ancientOne;
    }
}