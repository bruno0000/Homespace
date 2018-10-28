package com.example.homespace.homespace;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Task {

    private String title;
    private String description;
    private @ServerTimestamp
    Date timeCreated;
    private Date dueDate;
    private String taskID;
    private String userID;

    public Task(String title, String description, Date timeCreated, Date dueDate, String taskID, String userID) {
        this.title = title;
        this.description = description;
        this.timeCreated = timeCreated;
        this.dueDate = dueDate;
        this.taskID = taskID;
        this.userID = userID;
    }

    public Task() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
