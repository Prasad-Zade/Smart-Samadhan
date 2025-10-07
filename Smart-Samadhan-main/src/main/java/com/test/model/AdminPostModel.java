package com.test.model;

public class AdminPostModel {
    private String username;
    private String description;
    private String postImageUrl;
    private String location;
    private String timestamp;
    private String profileImage;

    public AdminPostModel(String username, String description, String postImageUrl, String location, String timestamp, String profileImage) {
        this.username = username;
        this.description = description;
        this.postImageUrl = postImageUrl;
        this.location = location;
        this.timestamp = timestamp;
        this.profileImage = profileImage;
    }

    public String getUsername() { return username; }
    public String getDescription() { return description; }
    public String getPostImageUrl() { return postImageUrl; }
    public String getLocation() { return location; }
    public String getTimestamp() { return timestamp; }
    public String getProfileImage() { return profileImage; }

    public void setUsername(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUsername'");
    }

    public void setDescription(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDescription'");
    }

    public void setImageUrl(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setImageUrl'");
    }

    public void setProfileImage(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setProfileImage'");
    }

    public void setStatus(String status) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStatus'");
    }

    public void setTimestamp(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setTimestamp'");
    }
}