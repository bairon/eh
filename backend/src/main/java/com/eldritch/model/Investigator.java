package com.eldritch.model;

public class Investigator {
    private String name;
    private String image1;
    private String image2;
    private String description;

    public Investigator() {
    }

    // Constructor, getters, and setters
    public Investigator(String name, String image1, String image2, String description) {
        this.name = name;
        this.image1 = image1;
        this.image2 = image2;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}