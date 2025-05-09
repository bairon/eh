package com.eldritch.quiz;

public class QuizPlayer {
    private String id;
    private String nickname;
    private int score;
    private boolean active;

    public QuizPlayer() {
    }

    public QuizPlayer(String id, String nickname, int score, boolean active) {
        this.id = id;
        this.nickname = nickname;
        this.score = score;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    // Add this to prevent serialization issues
    @Override
    public String toString() {
        return String.format("Player[id=%s, nickname=%s, score=%d, active=%b]",
                id, nickname, score, active);
    }

}



