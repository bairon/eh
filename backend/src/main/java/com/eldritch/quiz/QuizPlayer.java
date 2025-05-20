package com.eldritch.quiz;

import java.util.HashMap;
import java.util.Map;

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

    public void incrementScore() {
        score++;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("nickname", nickname);
        map.put("score", score);
        map.put("active", active);
        return map;
    }

    public static QuizPlayer fromMap(Map<String, Object> map) {
        if (map == null) return null;
        return new QuizPlayer(
                (String) map.get("id"),
                (String) map.get("nickname"),
                ((Number) map.get("score")).intValue(),
                (Boolean) map.get("active")
        );
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



