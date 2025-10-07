package com.test.model;

import com.google.cloud.firestore.annotation.DocumentId;

import java.util.ArrayList;
import java.util.List;

public class Community {
    @DocumentId
    private String communityId;
    private String name;
    private String description;
    private String creatorId;
    private long timestamp;
    private List<String> members; // List of user IDs who are members
    private List<String> pendingMembers; // List of user IDs requesting to join

    // Default constructor is required for Firestore's automatic data mapping
    public Community() {
        this.members = new ArrayList<>();
        this.pendingMembers = new ArrayList<>();
    }

    public Community(String name, String description, String creatorId) {
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.timestamp = System.currentTimeMillis();
        this.members = new ArrayList<>();
        this.members.add(creatorId); // The creator is the first member and admin
        this.pendingMembers = new ArrayList<>();
    }

    // --- Getters ---
    public String getCommunityId() { return communityId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCreatorId() { return creatorId; }
    public long getTimestamp() { return timestamp; }
    public List<String> getMembers() { return members; }
    public List<String> getPendingMembers() { return pendingMembers; }

    // --- Setters ---
    public void setCommunityId(String communityId) { this.communityId = communityId; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setMembers(List<String> members) { this.members = members; }
    public void setPendingMembers(List<String> pendingMembers) { this.pendingMembers = pendingMembers; }
}