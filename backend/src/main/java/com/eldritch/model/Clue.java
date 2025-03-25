package com.eldritch.model;

public class Clue {
    private String cityId;
    private String faceImage;
    private String backImage;

    public Clue(String cityId, String faceImage, String backImage) {
        this.cityId = cityId;
        this.faceImage = faceImage;
        this.backImage = backImage;
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
}
