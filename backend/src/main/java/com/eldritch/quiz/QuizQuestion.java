package com.eldritch.quiz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizQuestion {
    private String text;
    private List<String> options;
    private int correctOption;

    public QuizQuestion(String text, List<String> options, int correctOption) {
        this.text = text;
        this.options = options;
        this.correctOption = correctOption;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("text", text);
        map.put("options", options);
        map.put("correctOption", correctOption);
        return map;
    }

    public static QuizQuestion fromMap(Map<String, Object> map) {
        if (map == null) return null;
        return new QuizQuestion(
                (String) map.get("text"),
                (List<String>) map.get("options"),
                ((Number) map.get("correctOption")).intValue()
        );
    }
    @Override
    public String toString() {
        return "QuizQuestion{" +
                "text='" + text + '\'' +
                ", options=" + options +
                ", correctOption=" + correctOption +
                '}';
    }
}

