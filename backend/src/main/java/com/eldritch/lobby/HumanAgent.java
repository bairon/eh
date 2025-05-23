package com.eldritch.lobby;

import com.eldritch.logic.Interraction;
import com.eldritch.user.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HumanAgent extends EhAgent {

    private UserData userData;

    private CompletableFuture<String> pendingAnswer;


    public HumanAgent(String id) {
        super(id);
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
        return map;
    }

    public static HumanAgent fromMap(Map<String, Object> map) {
        if (map == null) return null;
        return new HumanAgent(
                (String) map.get("id")
        );
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
