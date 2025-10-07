package com.test.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.ServerTimestamp;

/**
 * Represents a single notification for a user.
 * This class is designed to be used with Firestore's automatic data mapping.
 */
public class Notification {

    private String notificationId; // To store the document ID
    private String message;        // The content of the notification (e.g., "User X liked your post")
    private String senderId;       // The UID of the user who triggered the notification
    private String postId;         // Optional: The ID of the relevant post, complaint, etc.
    private boolean isRead;        // To track if the user has seen the notification
    
    @ServerTimestamp // Firestore will automatically populate this field with the server's timestamp
    private Timestamp timestamp;   // The time the notification was created

    public Notification() {}

    public Notification(String message, String senderId, String postId) {
        this.message = message;
        this.senderId = senderId;
        this.postId = postId;
        this.isRead = false; // Notifications are unread by default
    }

    // --- Getters and Setters ---

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "message='" + message + '\'' +
                ", isRead=" + isRead +
                ", timestamp=" + timestamp +
                '}';
    }
}