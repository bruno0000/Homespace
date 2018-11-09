package com.example.homespace.homespace;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class Finance {
    private String title;
    private String description;
    private double amount;
    private @ServerTimestamp
    Date timeCreated;
    private List<Integer> dueDate;
    private String financeID;
    private String userUID;
    private int imageResource;

    public Finance(String title, String description, double amount, Date timeCreated, List<Integer>dueDate, String financeID, String userUID, int imageResource) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.timeCreated = timeCreated;
        this.dueDate = dueDate;
        this.financeID = financeID;
        this.userUID = userUID;
        this.imageResource = imageResource;
    }

    public Finance(){}

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public List<Integer> getDueDate() {
        return dueDate;
    }

    public String getFinanceID() {
        return financeID;
    }

    public String getUserUID() {
        return userUID;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public void setDueDate(List<Integer> dueDate) {
        this.dueDate = dueDate;
    }

    public void setFinanceID(String financeID) {
        this.financeID = financeID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
