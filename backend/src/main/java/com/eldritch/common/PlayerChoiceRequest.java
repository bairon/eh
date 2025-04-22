package com.eldritch.common;

import com.eldritch.engine.GameAction;

import java.util.List;

public class PlayerChoiceRequest {
    private final String prompt;
    private final List<GameAction> availableActions;

    public PlayerChoiceRequest(String prompt, List<GameAction> availableActions) {
        this.prompt = prompt;
        this.availableActions = availableActions;
    }

    public String getPrompt() {
        return prompt;
    }

    public List<GameAction> getAvailableActions() {
        return availableActions;
    }
}