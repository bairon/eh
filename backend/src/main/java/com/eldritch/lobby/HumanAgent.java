package com.eldritch.lobby;

import com.eldritch.logic.Interraction;
import com.eldritch.user.UserData;

import java.util.concurrent.CompletableFuture;

public class HumanAgent extends EhAgent {

    private final UserData userData;

    private CompletableFuture<String> pendingAnswer;

    public HumanAgent(UserData  userData) {
        super(userData.getId());
        this.userData = userData;
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

    public UserData getUserData() {
        return userData;
    }

    @Override
    public String getNickname() {
        return userData.getNickname();
    }
}
