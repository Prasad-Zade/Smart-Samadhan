package com.test.model;

public class FeedbackModel {
    private String name;
    private String feedback;
    private int rating;
    private long timestamp;


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
