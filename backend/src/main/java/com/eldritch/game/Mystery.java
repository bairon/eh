package com.eldritch.game;

import java.util.List;
import java.util.ArrayList;

public class Mystery {
    private String id;
    private String ancientOne; // Derived from id prefix (azathoth_, cthulhu_, etc.)
    private String onPlay;
    private String when;
    private String contact;
    private String test;
    private String onSuccess;
    private String onDefeat;
    private String solvedCondition;
    private String onFinal;

    public Mystery(String id, String onPlay, String when, String contact,
                   String test, String onSuccess, String onDefeat,
                   String solvedCondition, String onFinal) {
        this.id = id;
        this.ancientOne = parseAncientOneFromId(id);
        this.onPlay = onPlay;
        this.when = when;
        this.contact = contact;
        this.test = test;
        this.onSuccess = onSuccess;
        this.onDefeat = onDefeat;
        this.solvedCondition = solvedCondition;
        this.onFinal = onFinal;
    }

    private String parseAncientOneFromId(String id) {
        if (id.startsWith("azathoth")) return "azathoth";
        if (id.startsWith("cthulhu")) return "cthulhu";
        if (id.startsWith("shub")) return "shub";
        if (id.startsWith("yog")) return "yog";
        return null;
    }

    // Getters
    public String getId() { return id; }
    public String getAncientOne() { return ancientOne; }
    public String getOnPlay() { return onPlay; }
    public String getWhen() { return when; }
    public String getContact() { return contact; }
    public String getTest() { return test; }
    public String getOnSuccess() { return onSuccess; }
    public String getOnDefeat() { return onDefeat; }
    public String getSolvedCondition() { return solvedCondition; }
    public String getOnFinal() { return onFinal; }

    @Override
    public String toString() {
        return "Mystery{" +
                "id='" + id + '\'' +
                ", ancientOne='" + ancientOne + '\'' +
                ", onPlay='" + onPlay + '\'' +
                '}';
    }
}