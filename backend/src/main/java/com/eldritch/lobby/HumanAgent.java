package com.eldritch.lobby;

import com.eldritch.logic.Interraction;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HumanAgent extends EhAgent {

    private CompletableFuture<String> pendingAnswer;


    public HumanAgent(String id, String nickname) {
        super(id, nickname);
    }

    @Override
    public CompletableFuture<String> interract(Interraction interraction) {
        this.pendingAnswer = new CompletableFuture<>();
        return pendingAnswer;
    }

    @Override
    public void onAnswerReceived(String id, String answer) {
        if (id.equals(this.id) && pendingAnswer != null) {
            pendingAnswer.complete(answer);
            pendingAnswer = null;
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("nickname", nickname);
        return map;
    }

    public static HumanAgent fromMap(Map<String, Object> map) {
        if (map == null) return null;
        return new HumanAgent(
                (String) map.get("id"),
                (String) map.get("nickname")
        );
    }


}
