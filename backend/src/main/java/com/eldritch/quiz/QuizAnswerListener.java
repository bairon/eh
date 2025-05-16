package com.eldritch.quiz;

public interface QuizAnswerListener {

    void onAnswerReceived(String playerId, int answerIndex);

}

