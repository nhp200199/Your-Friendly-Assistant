package com.phucnguyen.khoaluantotnghiep.model;

public class Seller {
    private String id;
    private String name;
    private boolean isOfficialShop;
    private String platform;
    private boolean isVerrified;
    private float rating;
    private long created;
    private int follower;
    private int responseRate;
    private int totalItem;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOfficialShop() {
        return isOfficialShop;
    }

    public void setOfficialShop(boolean officialShop) {
        isOfficialShop = officialShop;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public boolean isVerrified() {
        return isVerrified;
    }

    public void setVerrified(boolean verrified) {
        isVerrified = verrified;
    }

    public int getResponseRate() {
        return responseRate;
    }

    public void setResponseRate(int responseRate) {
        this.responseRate = responseRate;
    }
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }
}
