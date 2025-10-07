package com.test.model;

public class Post {

    private String username;
    private String description;
    private String postImageUrl;
    private String location;
    private String timestamp;
    private String profileImage;
    private String postId;
    private int likeCount;
    private boolean isLikedByCurrentUser;

    public Post(String postId, String username, String description, String postImageUrl, String location, String timestamp, String profileImage, int likeCount) {
        this.postId = postId;
        this.username = username;
        this.description = description;
        this.postImageUrl = postImageUrl;
        this.location = location;
        this.timestamp = timestamp;
        this.profileImage = profileImage;
        this.likeCount = likeCount;
        this.isLikedByCurrentUser = false;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public String getLocation() {
        return location;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getProfileUrl() {
        return profileImage;
    }

    public String getPostId() {
        return postId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLikedByCurrentUser() {
        return isLikedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean liked) {
        this.isLikedByCurrentUser = liked;
    }

     private boolean isSavedByCurrentUser;

    public boolean isSavedByCurrentUser() {
        return isSavedByCurrentUser;
    }

    public void setSavedByCurrentUser(boolean savedByCurrentUser) {
        isSavedByCurrentUser = savedByCurrentUser;
    }
}