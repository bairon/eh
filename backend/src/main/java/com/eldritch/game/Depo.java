package com.eldritch.game;

import java.util.*;

/**
 * Everything that should not be passed to the client (secret sequences) should be kept only on the server side
 * inside this object
 */
public class Depo {
    private final Queue<Artifact> artifacts;
    private final Queue<Asset> assets;
    private final Queue<Clue> clues;
    private final Queue<Condition> conditions;
    private final Queue<Location> locations;
    private final Queue<Monster> monsters;
    private final Queue<Monster> epicMonsters;
    private final Queue<Mystery> mysteries;
    private final Queue<Mythos> yellowMythos;
    private final Queue<Mythos> greenMythos;
    private final Queue<Mythos> blueMythos;
    private final Queue<Portal> portals;
    private final Queue<Spell> spells;

    public Depo(
            List<Artifact> artifacts,
            List<Asset> assets,
            List<Clue> clues,
            List<Condition> conditions,
            List<Location> locations,
            List<Monster> monsters,
            List<Monster> epicMonsters,
            List<Mystery> mysteries,
            List<Mythos> yellowMythos,
            List<Mythos> greenMythos,
            List<Mythos> blueMythos,
            List<Portal> portals,
            List<Spell> spells
    ) {
        this.artifacts = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(artifacts);
        this.artifacts.addAll(artifacts);

        this.assets = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(assets);
        this.assets.addAll(assets);

        this.clues = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(clues);
        this.clues.addAll(clues);

        this.conditions = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(conditions);
        this.conditions.addAll(conditions);

        this.locations = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(locations);
        this.locations.addAll(locations);

        this.monsters = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(monsters);
        this.monsters.addAll(monsters);

        this.epicMonsters = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(epicMonsters);
        this.epicMonsters.addAll(epicMonsters);

        this.mysteries = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(mysteries);
        this.mysteries.addAll(mysteries);

        this.mythos = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(mythos);
        this.mythos.addAll(mythos);

        this.portals = new ArrayDeque<>(); // Use ArrayDeque instead of SynchronousQueue
        Collections.shuffle(portals);
        this.portals.addAll(portals);

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