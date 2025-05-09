package com.eldritch.quiz;

import java.util.ArrayList;

public class QuizMessage {
    private MessageType type;
    private String content;
    private QuizPlayer player;
    private QuizQuestion question;
    private Integer selectedAnswer;
    private Boolean correct;
    private QuizPlayer winner;
    private ArrayList<QuizPlayer> players;

    public enum MessageType {
        JOIN, START, QUESTION, ANSWER, WIN, ERROR, RECONNECT, UPDATE
    }

    public void setPlayers(ArrayList<QuizPlayer> players) {
        this.players = players;
    }

    public ArrayList<QuizPlayer> getPlayers() {
        return players;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public QuizPlayer getPlayer() {
        return player;
    }

    public void setPlayer(QuizPlayer player) {
        this.player = player;
    }

    public QuizQuestion getQuestion() {
        return question;
    }

    public void setQuestion(QuizQuestion question) {
        this.question = question;
    }

    public Integer getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(Integer selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public QuizPlayer getWinner() {
        return winner;
    }

    public void setWinner(QuizPlayer winner) {
        this.winner = winner;
    }
}
