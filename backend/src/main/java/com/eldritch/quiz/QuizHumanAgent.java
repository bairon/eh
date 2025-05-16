package com.eldritch.quiz;

import java.util.concurrent.CompletableFuture;

public class QuizHumanAgent implements QuizAgent {
    private final String playerId;
    private CompletableFuture<Integer> pendingAnswer;

    public QuizHumanAgent(String playerId) {
        this.playerId = playerId;
    }

    @Override
    public CompletableFuture<Integer> handleQuestion(QuizQuestion question) {
        this.pendingAnswer = new CompletableFuture<>();
        return pendingAnswer;
    }

    public void onAnswerReceived(String playerId, int answerIndex) {
        if (playerId.equals(this.playerId) && pendingAnswer != null) {
            pendingAnswer.complete(answerIndex);
            pendingAnswer = null;
        }
    }
}
