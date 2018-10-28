package com.example.homespace.homespace;

public class Homespace {
    private String homespaceID;
    private String homespaceCreatorID;
    private String homespaceName;

    public Homespace(String homespaceID, String homespaceCreatorID, String homespaceName) {
        this.homespaceID = homespaceID;
        this.homespaceCreatorID = homespaceCreatorID;
        this.homespaceName = homespaceName;
    }

    public Homespace(){}

    public void setHomespaceID(String homespaceID) {
        this.homespaceID = homespaceID;
    }

    public void setHomespaceCreatorID(String homespaceCreatorID) {
        this.homespaceCreatorID = homespaceCreatorID;
    }

    public void setHomespaceName(String homespaceName) {
        this.homespaceName = homespaceName;
    }

    public String getHomespaceID() {
        return homespaceID;
    }

    public String getHomespaceCreatorID() {
        return homespaceCreatorID;
    }

    public String getHomespaceName() {
        return homespaceName;
    }
}
