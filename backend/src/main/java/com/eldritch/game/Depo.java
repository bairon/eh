package com.eldritch.game;

import java.util.*;

/**
 * Everything that should not be passed to the client (secret sequences) should be kept only on the server side
 * inside this object
 */
public class Depo {
    private final Queue<Portal> portals;
    private final Queue<Clue> clues;

    public Depo(List<Portal> portals, List<Clue> clues) {
        this.portals = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(portals);
        this.portals.addAll(portals);

        this.clues = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(clues);
        this.clues.addAll(clues);

    }

    public Portal acquirePortal() {
        return portals.poll(); // Retrieve and remove the head of the queue
    }

    public void returnPortal(Portal portal) {
        portals.add(portal); // Add the portal back to the queue
    }

    public Clue acquireClue() {
        return clues.poll();
    }
    public void returnClue(Clue clue) {
        clues.add(clue);
    }

}