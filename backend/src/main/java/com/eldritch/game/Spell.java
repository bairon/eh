package com.eldritch.game;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Spell {
    private String id;
    private String name;
    private String test; // e.g. "Will-1"
    private Map<String, String> successEffects = new HashMap<>();
    private Map<String, String> failureEffects = new HashMap<>();
    private boolean requiresTarget;
    private List<String> validTargetTypes; // e.g. ["monster", "location"]
    private String aiPriority; // Combat/Support/Utility
    private int aiWeight; // 1-100

    // Constructor
    public Spell(String id, String name, String test,
                 Map<String, String> successEffects,
                 Map<String, String> failureEffects,
                 boolean requiresTarget,
                 List<String> validTargetTypes,
                 String aiPriority,
                 int aiWeight) {
        this.id = id;
        this.name = name;
        this.test = test;
        this.successEffects = successEffects;
        this.failureEffects = failureEffects;
        this.requiresTarget = requiresTarget;
        this.validTargetTypes = validTargetTypes;
        this.aiPriority = aiPriority;
        this.aiWeight = aiWeight;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTest() {
        return test;
    }

    public Map<String, String> getSuccessEffects() {
        return successEffects;
    }

    public Map<String, String> getFailureEffects() {
        return failureEffects;
    }

    public boolean isRequiresTarget() {
        return requiresTarget;
    }

    public List<String> getValidTargetTypes() {
        return validTargetTypes;
    }

    public String getAiPriority() {
        return aiPriority;
    }

    public int getAiWeight() {
        return aiWeight;
    }

    public int getRequiredSuccesses() {
        return 0;
    }

    public boolean requiresTarget() {
        return false;
    }
}