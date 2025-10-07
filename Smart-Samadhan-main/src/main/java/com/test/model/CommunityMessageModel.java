package com.test.model;

import com.google.cloud.firestore.annotation.DocumentId;

public class CommunityMessageModel {
    @DocumentId
    private String messageId;
    private String communityId;
    private String senderId;
    private String senderName; // Store sender's display name for easier UI rendering
    private String message;
    private long timestamp;

    // Default constructor required for Firestore
    public CommunityMessageModel() {}

    public CommunityMessageModel(String communityId, String senderId, String senderName, String message) {
        this.communityId = communityId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public CommunityMessageModel(String communityId, String senderId, String senderName, String message, long timestamp) {
        this.communityId = communityId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters
    public String getMessageId() { return messageId; }
    public String getCommunityId() { return communityId; }
    public String getSenderId() { return senderId; }
    public String getSenderName() { return senderName; }
    public String getMessage() { return message; }
    public long getTimestamp() { return timestamp; }

    // Setters
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public void setCommunityId(String communityId) { this.communityId = communityId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public void setMessage(String message) { this.message = message; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "CommunityMessageModel{" +
                "messageId='" + messageId + '\'' +
                ", communityId='" + communityId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}