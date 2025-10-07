package com.test.model;

public class Profilepost {
    private final String username;
    private final String description;
    private final String postImageUrl;
    private final String location;
    private final String timestamp;

    public Profilepost(String username, String description, String postImageUrl, String location, String timestamp) {
        this.username = username;
        this.description = description;
        this.postImageUrl = postImageUrl;
        this.location = location;
        this.timestamp = timestamp;
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
}