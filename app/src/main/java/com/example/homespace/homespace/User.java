package com.example.homespace.homespace;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class User {
    private String username;
    private String userUID;
    private String userID;
    private String homespaceID;
    private @ServerTimestamp
    Date timeCreated;

    public User(String username, String userUID, String userID, String homespaceID, Date timeCreated) {
        this.username = username;
        this.userUID = userUID;
        this.userID = userID;
        this.homespaceID = homespaceID;
        this.timeCreated = timeCreated;
    }

    public User() {
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

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

    public Date getTimeCreated() {
        return timeCreated;
    }
}
