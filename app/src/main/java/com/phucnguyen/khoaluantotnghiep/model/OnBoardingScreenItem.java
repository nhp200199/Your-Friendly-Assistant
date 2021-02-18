package com.phucnguyen.khoaluantotnghiep.model;

public class OnBoardingScreenItem {
    String description;
    int imageRsc;

    public OnBoardingScreenItem(String description, int imageRsc) {
        this.description = description;
        this.imageRsc = imageRsc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageRsc() {
        return imageRsc;
    }

    public void setImageRsc(int imageRsc) {
        this.imageRsc = imageRsc;
    }
}
