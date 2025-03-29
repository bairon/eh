package com.eldritch.investigator;

public class InvestigatorTemplate {
    private String id;
    private String image;
    private String imageback;
    private String description;
    private String location;
    private String property;
    private String bonus;

    public InvestigatorTemplate() {
    }

    // Constructor, getters, and setters
    public InvestigatorTemplate(String id, String image, String imageback, String description, String location, String property, String bonus) {
        this.id = id;
        this.image = image;
        this.imageback = imageback;
        this.description = description;
        this.location = location;
        this.property = property;
        this.bonus = bonus;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }
}