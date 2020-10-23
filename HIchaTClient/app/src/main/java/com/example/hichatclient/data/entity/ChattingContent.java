package com.example.hichatclient.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChattingContent {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private String userID;
    @ColumnInfo(name = "friend_id")
    private String friendID;
    @ColumnInfo(name = "send_person")
    private String sendPerson;
    @ColumnInfo(name = "send_time")
    private String sendTime;
    @ColumnInfo(name = "send_content")
    private String sendContent;

    public ChattingContent(String userID, String friendID, String sendPerson, String sendTime, String sendContent) {
        this.userID = userID;
        this.friendID = friendID;
        this.sendPerson = sendPerson;
        this.sendTime = sendTime;
        this.sendContent = sendContent;
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

    public String getFriendID() {
        return friendID;
    }

    public void setFriendID(String friendID) {
        this.friendID = friendID;
    }

    public String getSendPerson() {
        return sendPerson;
    }

    public void setSendPerson(String sendPerson) {
        this.sendPerson = sendPerson;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }
}
