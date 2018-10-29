package com.example.homespace.homespace;

public class User {
    private String username;
    private String userUID;
    private String userID;
    private String homespaceID;

    public User(String username, String userUID, String userID, String homespaceID) {
        this.username = username;
        this.userUID = userUID;
        this.userID = userID;
        this.homespaceID = homespaceID;
    }

    public User(){}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setHomespaceID(String homespaceID) {
        this.homespaceID = homespaceID;
    }

    public String getUsername() {
        return username;
    }

    public String getUserUID() {
        return userUID;
    }

    public String getUserID() {
        return userID;
    }

    public String getHomespaceID() {
        return homespaceID;
    }
}
