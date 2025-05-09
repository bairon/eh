package com.eldritch.quiz;

import org.springframework.context.ApplicationEvent;

public class QuizEvent extends ApplicationEvent {
    private final QuizMessage message;

    public QuizEvent(Object source, QuizMessage message) {
        super(source);
        this.message = message;
    }

    public QuizMessage getMessage() {
        return message;
    }
}