package com.eldritch.common;

import com.eldritch.engine.Attribute;
import com.eldritch.engine.Effect;
import com.eldritch.engine.GamePhase;

import java.util.Map;
import java.util.UUID;

public class SpellCard implements Card {
    private final UUID id;
    private final String name;
    private final Attribute testAttribute;
    private final int testModifier;
    private final GamePhase validPhase;
    private final Effect successEffect;
    private final Map<Integer, Effect> flipEffects;

    public SpellCard(String name, Attribute testAttribute, int testModifier,
                     GamePhase validPhase, Effect successEffect,
                     Map<Integer, Effect> flipEffects) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.testAttribute = testAttribute;
        this.testModifier = testModifier;
        this.validPhase = validPhase;
        this.successEffect = successEffect;
        this.flipEffects = flipEffects;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CardType getType() {
        return CardType.SPELL;
    }

    @Override
    public boolean canPlay(GameState state) {
        return state.getCurrentPhase() == validPhase;
    }

    @Override
    public GameEvent play(UUID investigatorId) {
        return new SpellCastEvent(id, investigatorId, testAttribute, testModifier);
    }

    @Override
    public GameEvent resolve() {
        // Default resolution - can be overridden by specific spells
        return new SpellResolvedEvent(id);
    }

    @Override
    public void discard() {
        // Handle any discard effects or cleanup
    }

    // Spell-specific methods
    public Attribute getTestAttribute() {
        return testAttribute;
    }

    public int getTestModifier() {
        return testModifier;
    }

    public Effect getSuccessEffect() {
        return successEffect;
    }

    public Effect getFlipEffect(int successLevel) {
        return flipEffects.getOrDefault(successLevel, null);
    }
}