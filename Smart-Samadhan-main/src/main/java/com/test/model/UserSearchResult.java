package com.test.model;

public class UserSearchResult {
    private final String userId;
    private final String fullName;
    private final String username;
    private final String profileImageUrl;

    public UserSearchResult(String userId, String fullName, String username, String profileImageUrl) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getProfileImageUrl() { return profileImageUrl; }
}