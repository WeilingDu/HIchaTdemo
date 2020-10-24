package com.example.hichatclient.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private String userID;
    @ColumnInfo(name = "user_password")
    private String userPassword;
    @ColumnInfo(name = "user_name")
    private String userName;
    @ColumnInfo(name = "user_profile")
    private String userProfile;
    @ColumnInfo(name = "user_short_token")
    private String userShortToken;
    @ColumnInfo(name = "user_long_token")
    private String userLongToken;


    public User(String userID, String userPassword, String userName, String userProfile, String userShortToken, String userLongToken) {
        this.userID = userID;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userProfile = userProfile;
        this.userShortToken = userShortToken;
        this.userLongToken = userLongToken;
    }

    public String getUserShortToken() {
        return userShortToken;
    }

    public void setUserShortToken(String userShortToken) {
        this.userShortToken = userShortToken;
    }

    public String getUserLongToken() {
        return userLongToken;
    }

    public void setUserLongToken(String userLongToken) {
        this.userLongToken = userLongToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }


}
