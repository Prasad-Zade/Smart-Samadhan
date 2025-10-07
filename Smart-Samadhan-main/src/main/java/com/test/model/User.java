package com.test.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

    private final StringProperty userId;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty role;

    // Default constructor for Firestore
    public User() {
        this("", "", "", "");
    }

    // Parameterized constructor
    public User(String userId, String name, String email, String role) {
        this.userId = new SimpleStringProperty(userId);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.role = new SimpleStringProperty(role);
    }

    // Getters
    public String getUserId() { return userId.get(); }
    public String getName() { return name.get(); }
    public String getEmail() { return email.get(); }
    public String getRole() { return role.get(); }

    // Setters
    public void setUserId(String userId) { this.userId.set(userId); }
    public void setName(String name) { this.name.set(name); }
    public void setEmail(String email) { this.email.set(email); }
    public void setRole(String role) { this.role.set(role); }

    // Property getters
    public StringProperty userIdProperty() { return userId; }
    public StringProperty nameProperty() { return name; }
    public StringProperty emailProperty() { return email; }
    public StringProperty roleProperty() { return role; }

    @Override
    public String toString() {
        return this.getName();
    }
}