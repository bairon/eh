package com.eldritch.game;

public class Portal {
    private String cityId;
    private String faceImage;
    private String backImage;

    public Portal(String cityId, String faceImage, String backImage) {
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