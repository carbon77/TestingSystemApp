package com.example.testsys.models.user;

public class User {
    private String id;
    private String email;
    private String displayName;
    private String username;

    public User() {

    }

    public User(String id, String email, String displayName, String username) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
