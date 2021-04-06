package com.phucnguyen.khoaluantotnghiep.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Review {
    private String id;
    private String content;
    private int rating;
    private long createdAt;
    private String[] images;
    private List<Video> videos;
    private User user;

    public Review(String id, String content, int rating, long createdAt, String[] images, List<Video> videos, User user) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
        this.images = images;
        this.videos = videos;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public class Video {
        private String id;
        private String cover;
        @SerializedName("upload_time")
        private String uploadTime;
        private long duration;
        private String url;

        public Video(String cover, long duration, String url) {
            this.cover = cover;
            this.duration = duration;
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getUploadTime() {
            return uploadTime;
        }

        public void setUploadTime(String uploadTime) {
            this.uploadTime = uploadTime;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class User {
        private String name;
        private String fullName;

        public User(String name, String fullName) {
            this.name = name;
            this.fullName = fullName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
    }

    public class Image {
        private String id;
        @SerializedName("full_path")
        private String fullPath;
        private String status;
    }
}


