package com.example.homespace.homespace;

public class Homespace {
    private String homespaceID;
    private String homespaceCreatorUID;
    private String homespaceName;

    public Homespace(String homespaceID, String homespaceCreatorUID, String homespaceName) {
        this.homespaceID = homespaceID;
        this.homespaceCreatorUID = homespaceCreatorUID;
        this.homespaceName = homespaceName;
    }


    public Homespace(){}

    public void setHomespaceID(String homespaceID) {
        this.homespaceID = homespaceID;
    }

    public void setHomespaceCreatorUID(String homespaceCreatorUID) {
        this.homespaceCreatorUID = homespaceCreatorUID;
    }

    public void setHomespaceName(String homespaceName) {
        this.homespaceName = homespaceName;
    }

    public String getHomespaceID() {
        return homespaceID;
    }

    public String getHomespaceCreatorUID() {
        return homespaceCreatorUID;
    }

    public String getHomespaceName() {
        return homespaceName;
    }
}
