package com.eldritch.game;

public class Condition {
    private String id;
    private String name;
    private String action;
    private String basetest;
    private String basesuccess;
    private String basefailure;
    private String reckoningtest;
    private String reckoningsuccess;
    private String reckoningfailure;
    private String reckoningfinal;
    private String debuff;
    private String buff;
    private String contact;
    private String reckoningchoice;

    public Condition(String id, String name, String action, String basetest,
                     String basesuccess, String basefailure, String reckoningtest,
                     String reckoningsuccess, String reckoningfailure, String reckoningfinal,
                     String debuff, String buff, String contact, String reckoningchoice) {
        this.id = id;
        this.name = name;
        this.action = action;
        this.basetest = basetest;
        this.basesuccess = basesuccess;
        this.basefailure = basefailure;
        this.reckoningtest = reckoningtest;
        this.reckoningsuccess = reckoningsuccess;
        this.reckoningfailure = reckoningfailure;
        this.reckoningfinal = reckoningfinal;
        this.debuff = debuff;
        this.buff = buff;
        this.contact = contact;
        this.reckoningchoice = reckoningchoice;
    }

    // Getters and Setters for all fields
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    // ... (similar getters/setters for all other fields) ...

    @Override
    public String toString() {
        return "Condition{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", action='" + action + '\'' +
                ", basetest='" + basetest + '\'' +
                ", basesuccess='" + basesuccess + '\'' +
                ", basefailure='" + basefailure + '\'' +
                ", reckoningtest='" + reckoningtest + '\'' +
                ", reckoningsuccess='" + reckoningsuccess + '\'' +
                ", reckoningfailure='" + reckoningfailure + '\'' +
                ", reckoningfinal='" + reckoningfinal + '\'' +
                '}';
    }
}