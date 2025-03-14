package com.eldritch.model;

public class AncientOne {
    private String name;
    private String image1;
    private String image2;
    private String description;
    private int doom;

    // Constructor, getters, and setters
    public AncientOne(String name, String image1, String image2, String description) {
        this.name = name;
        this.image1 = image1;
        this.image2 = image2;
        this.description = description;
    }

    public AncientOne(String name) {
        this.name = name;
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

    public int getDoom() {
        return doom;
    }

    public void setDoom(int doom) {
        this.doom = doom;
    }
    // Getters and Setters
}