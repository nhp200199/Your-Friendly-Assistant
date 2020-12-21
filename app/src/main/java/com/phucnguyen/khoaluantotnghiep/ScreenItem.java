package com.phucnguyen.khoaluantotnghiep;

public class ScreenItem {
    private String title;
    private int imageRsc;

    public ScreenItem(String title, int imageRsc) {
        this.title = title;
        this.imageRsc = imageRsc;
    }
    public ScreenItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageRsc() {
        return imageRsc;
    }

    public void setImageRsc(int imageRsc) {
        this.imageRsc = imageRsc;
    }
}
