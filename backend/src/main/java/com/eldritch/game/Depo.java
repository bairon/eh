package com.eldritch.game;

import java.util.*;

/**
 * Everything that should not be passed to the client (secret sequences) should be kept only on the server side
 * inside this object
 */
public class Depo {
    private final Queue<Portal> portals;
    private final Queue<Clue> clues;
    private final Queue<Asset> assets;
    private final Queue<Spell> spells;

    public Depo(List<Portal> portals, List<Clue> clues, List<Asset> assets, List<Spell> spells) {
        this.portals = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(portals);
        this.portals.addAll(portals);

        this.clues = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(clues);
        this.clues.addAll(clues);

        this.assets = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(assets);
        this.assets.addAll(assets);

        this.spells = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(spells);
        this.spells.addAll(spells);

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

    public Asset acquireAsset() {
        return assets.poll();
    }

    public void returnAsset(Asset asset) {
        assets.add(asset);
    }

    public Asset acuireAsset(String id) {
        Iterator<Asset> iterator = assets.iterator();
        while (iterator.hasNext()) {
            Asset asset = iterator.next();
            if (asset.getId().equals(id)) {
                iterator.remove();
                return asset;
            }
        }
        return null;
    }

    public Spell acquireSpell() {
        return spells.poll();
    }

    public void returnSpell(Spell spell) {
        spells.add(spell);
    }

    public Spell acuireSpell(String id) {
        Iterator<Spell> iterator = spells.iterator();
        while (iterator.hasNext()) {
            Spell spell = iterator.next();
            if (spell.getId().equals(id)) {
                iterator.remove();
                return spell;
            }
        }
        return null;
    }

}