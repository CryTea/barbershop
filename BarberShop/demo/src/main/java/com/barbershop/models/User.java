package com.barbershop.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private SimpleIntegerProperty userId;
    private StringProperty fullName;
    private StringProperty username;
    private StringProperty password;
    private StringProperty role;

    public User(int userId, String fullName, String username, String password, String role) {
        this.userId = new SimpleIntegerProperty(userId);
        this.fullName = new SimpleStringProperty(fullName);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
    }

    public User(int userId, String fullName, String username) {
        this(userId, fullName, username, "", "manager");
    }

    public int getUserId() { return userId.get(); }
    public String getFullName() { return fullName.get(); }
    public String getUsername() { return username.get(); }
    public String getPassword() { return password.get(); }
    public String getRole() { return role.get(); }

    public StringProperty fullNameProperty() { return fullName; }
    public StringProperty usernameProperty() { return username; }
}
