package com.eldritch.game;

public class Clue {
    private String id;
    private String cityId;
    private String faceImage;
    private String backImage;

    public Clue(String id, String cityId, String faceImage, String backImage) {
        this.id = id;
        this.cityId = cityId;
        this.faceImage = faceImage;
        this.backImage = backImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    @Override
    public String toString() {
        return "Clue{" +
                "id='" + id + '\'' +
                ", cityId='" + cityId + '\'' +
                '}';
    }
}