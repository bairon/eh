package com.eldritch.common;

import java.util.UUID;

public class MythosCard implements Card {
    private final UUID id;
    private final String name;
    private final MythosType mythosType;
    private final MythosEffect effect;
    private final int doomThreshold;
    private final boolean immediateEffect;

    public MythosCard(String name, MythosType mythosType, MythosEffect effect,
                      int doomThreshold, boolean immediateEffect) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.mythosType = mythosType;
        this.effect = effect;
        this.doomThreshold = doomThreshold;
        this.immediateEffect = immediateEffect;
    }

    @Override
    public UUID getId() { return id; }

    @Override
    public String getName() { return name; }

    @Override
    public CardType getType() { return CardType.MYTHOS; }

    @Override
    public boolean canPlay(GameState state) {
        // Mythos cards are typically drawn, not played by investigators
        return false;
    }

    @Override
    public GameEvent play(UUID investigatorId) {
        // Mythos cards are usually resolved automatically
        return new MythosDrawnEvent(this.id, mythosType);
    }

    @Override
    public GameEvent resolve() {
        return new MythosResolvedEvent(
                this.id,
                this.mythosType,
                this.effect,
                this.doomThreshold,
                this.immediateEffect
        );
    }

    @Override
    public void discard() {
        // Handle any discard effects or cleanup
        if (this.effect != null) {
            this.effect.onDiscard();
        }
    }

    // Mythos-specific methods
    public MythosType getMythosType() { return mythosType; }
    public MythosEffect getEffect() { return effect; }
    public int getDoomThreshold() { return doomThreshold; }
    public boolean hasImmediateEffect() { return immediateEffect; }


}