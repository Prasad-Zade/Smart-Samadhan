package com.test.model;

import java.util.Date;

public class ChatMessage {
    private String senderId;
    private String receiverId;
    private String message;
    private Date timestamp;

    public ChatMessage() {} // Required for Firestore

    public ChatMessage(String senderId, String receiverId, String message, Date timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getMessage() { return message; }
    public Date getTimestamp() { return timestamp; }
}
