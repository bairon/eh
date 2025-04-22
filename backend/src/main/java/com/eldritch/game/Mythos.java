package com.eldritch.game;

import java.util.List;
import java.util.ArrayList;

public class Mythos {
    private String id;
    private String color;
    private List<String> actions = new ArrayList<>();
    private String onPlay;
    private String reckoning;
    private String test;
    private String onSuccess;
    private String onTokenEnds;
    private String rumor;
    private String contact;
    private String discard;

    public Mythos(String id, String color, List<String> actions, String onPlay,
                  String reckoning, String test, String onSuccess, String onTokenEnds,
                  String rumor, String contact, String discard) {
        this.id = id;
        this.color = color;
        this.actions = actions;
        this.onPlay = onPlay;
        this.reckoning = reckoning;
        this.test = test;
        this.onSuccess = onSuccess;
        this.onTokenEnds = onTokenEnds;
        this.rumor = rumor;
        this.contact = contact;
        this.discard = discard;
    }

    // Getters
    public String getId() { return id; }
    public String getColor() { return color; }
    public List<String> getActions() { return actions; }
    public String getOnPlay() { return onPlay; }
    public String getReckoning() { return reckoning; }
    public String getTest() { return test; }
    public String getOnSuccess() { return onSuccess; }
    public String getOnTokenEnds() { return onTokenEnds; }
    public String getRumor() { return rumor; }
    public String getContact() { return contact; }
    public String getDiscard() { return discard; }

    @Override
    public String toString() {
        return "Mythos{" +
                "id='" + id + '\'' +
                ", color='" + color + '\'' +
                ", actions=" + actions +
                '}';
    }
}