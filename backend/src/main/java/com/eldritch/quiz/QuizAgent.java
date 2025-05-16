package com.eldritch.quiz;

import java.util.concurrent.CompletableFuture;

public interface QuizAgent extends QuizAnswerListener {
    CompletableFuture<Integer> handleQuestion(QuizQuestion question);
}
