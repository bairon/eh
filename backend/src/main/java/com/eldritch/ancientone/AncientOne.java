package com.eldritch.ancientone;

public class AncientOne {
    private String id;
    private String image;
    private String imageback;
    private String description;
    private int doom;

    public AncientOne() {
    }

    public AncientOne(String id, String image, String imageback, String description, int doom) {
        this.id = id;
        this.image = image;
        this.imageback = imageback;
        this.description = description;
        this.doom = doom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageback() {
        return imageback;
    }

    public void setImageback(String imageback) {
        this.imageback = imageback;
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
}