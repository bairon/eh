package com.eldritch.game;

public class Asset {
    private String id;
    private String name;
    private String classification;
    private String bonus;
    private String action;
    private String test;
    private String immediate;
    private int cost;

    public Asset(String id, String name, String classification, String bonus,
                 String action, String test, String immediate, int cost) {
        this.id = id;
        this.name = name;
        this.classification = classification;
        this.bonus = bonus;
        this.action = action;
        this.test = test;
        this.immediate = immediate;
        this.cost = cost;
    }

    // Getters and setters
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

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getImmediate() {
        return immediate;
    }

    public void setImmediate(String immediate) {
        this.immediate = immediate;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}