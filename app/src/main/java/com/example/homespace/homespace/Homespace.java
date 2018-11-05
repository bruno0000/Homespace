package com.example.homespace.homespace;

import java.util.List;

public class Homespace {
    private String homespaceID;
    private String homespaceCreatorUID;
    private String homespaceName;
    private List<String> userList;

    public Homespace(String homespaceID, String homespaceCreatorUID, String homespaceName,
                     List<String> userList) {
        this.homespaceID = homespaceID;
        this.homespaceCreatorUID = homespaceCreatorUID;
        this.homespaceName = homespaceName;
        this.userList = userList;
    }

    public Homespace(){}

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

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
