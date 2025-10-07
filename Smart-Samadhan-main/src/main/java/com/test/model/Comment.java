package com.test.model;

import com.google.cloud.Timestamp;

public class Comment {
    private String username;
    private String text;
    private Timestamp timestamp;
    private String userId;
    private String profileImageUrl; // Optional: To show commenter's avatar

    // Firestore requires a no-arg constructor for deserialization
    public Comment() {}

    public Comment(String username, String text, String userId, Timestamp timestamp) {
        this.username = username;
        this.text = text;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    // Getters
    public String getUsername() { return username; }
    public String getText() { return text; }
    public Timestamp getTimestamp() { return timestamp; }
    public String getUserId() { return userId; }
    public String getProfileImageUrl() { return profileImageUrl; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setText(String text) { this.text = text; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}