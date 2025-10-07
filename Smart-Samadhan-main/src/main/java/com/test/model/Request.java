package com.test.model;

import javafx.beans.property.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Request {
    
    private final StringProperty date = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty imageUrl = new SimpleStringProperty();
    private final StringProperty issueType = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty location = new SimpleStringProperty();
    private final StringProperty phone = new SimpleStringProperty();
    private final StringProperty reportId = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty userEmail = new SimpleStringProperty();
    private final LongProperty timestamp = new SimpleLongProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty priority = new SimpleStringProperty();

    private final ReadOnlyStringWrapper applicant = new ReadOnlyStringWrapper();

    public Request() {
        applicant.bind(firstName.concat(" ").concat(lastName));
    }

    // --- Property Accessors for TableView ---
    public StringProperty titleProperty() { return title; }
    public ReadOnlyStringProperty applicantProperty() { return applicant.getReadOnlyProperty(); }
    
    // ADD THIS METHOD
    public StringProperty locationProperty() { return location; }
    
    public StringProperty priorityProperty() { return priority; }
    public StringProperty statusProperty() { return status; }
    
    public StringProperty submittedDateProperty() {
        if (getTimestamp() > 0) {
            Date dateFromTimestamp = new Date(getTimestamp()); 
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            sdf.setTimeZone(TimeZone.getDefault());
            return new SimpleStringProperty(sdf.format(dateFromTimestamp));
        }
        return date;
    }

    // --- Standard Getters and Setters ---
    public String getDate() { return date.get(); }
    public void setDate(String date) { this.date.set(date); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }

    public String getFirstName() { return firstName.get(); }
    public void setFirstName(String firstName) { this.firstName.set(firstName); }

    public String getImageUrl() { return imageUrl.get(); }
    public void setImageUrl(String imageUrl) { this.imageUrl.set(imageUrl); }

    public String getIssueType() { return issueType.get(); }
    public void setIssueType(String issueType) { this.issueType.set(issueType); }

    public String getLastName() { return lastName.get(); }
    public void setLastName(String lastName) { this.lastName.set(lastName); }

    public String getLocation() { return location.get(); }
    public void setLocation(String location) { this.location.set(location); }

    public String getPhone() { return phone.get(); }
    public void setPhone(String phone) { this.phone.set(phone); }

    public String getReportId() { return reportId.get(); }
    public void setReportId(String reportId) { this.reportId.set(reportId); }

    public String getTitle() { return title.get(); }
    public void setTitle(String title) { this.title.set(title); }

    public String getUserEmail() { return userEmail.get(); }
    public void setUserEmail(String userEmail) { this.userEmail.set(userEmail); }

    public long getTimestamp() { return timestamp.get(); }
    public void setTimestamp(long timestamp) { this.timestamp.set(timestamp); }

    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }

    public String getPriority() { return priority.get(); }
    public void setPriority(String priority) { this.priority.set(priority); }
}
