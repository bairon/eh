package com.eldritch.game;

import java.util.ArrayList;
import java.util.List;

public class Artifact {
    private String id;
    private String name;
    private List<String> classification;
    private List<String> actions;
    private List<String> bonuses;
    private List<String> immediates;
    private String test;

    public Artifact(String id, String name, String classification, String action,
                    String bonus, String immediate, String test) {
        this.id = id;
        this.name = name;
        this.classification = splitString(classification);
        this.actions = splitString(action);
        this.bonuses = splitString(bonus);
        this.immediates = splitString(immediate);
        this.test = test;
    }

    private List<String> splitString(String input) {
        List<String> result = new ArrayList<>();
        if (input != null && !input.isEmpty()) {
            for (String part : input.split(",")) {
                result.add(part.trim());
            }
        }
        return result;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getClassification() {
        return classification;
    }

    public void setClassification(List<String> classification) {
        this.classification = classification;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public List<String> getBonuses() {
        return bonuses;
    }

    public void setBonuses(List<String> bonuses) {
        this.bonuses = bonuses;
    }

    public List<String> getImmediates() {
        return immediates;
    }

    public void setImmediates(List<String> immediates) {
        this.immediates = immediates;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "Artifact{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", classification=" + classification +
                ", actions=" + actions +
                ", bonuses=" + bonuses +
                ", immediates=" + immediates +
                ", test='" + test + '\'' +
                '}';
    }
}