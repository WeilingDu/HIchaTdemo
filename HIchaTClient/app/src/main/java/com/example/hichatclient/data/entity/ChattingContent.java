package com.example.hichatclient.data.entity;

import androidx.annotation.NonNull;
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
    @ColumnInfo(name = "msg_type")
    private String msgType;
    @ColumnInfo(name = "msg_time")
    private long msgTime;
    @ColumnInfo(name = "msg_content")
    private String msgContent;
    @ColumnInfo(name = "is_read")
    private boolean isRead;

    public ChattingContent(String userID, String friendID, String msgType, long msgTime, String msgContent, boolean isRead) {
        this.userID = userID;
        this.friendID = friendID;
        this.msgType = msgType;
        this.msgTime = msgTime;
        this.msgContent = msgContent;
        this.isRead = isRead;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
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

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long sendTime) {
        this.msgTime = sendTime;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String sendContent) {
        this.msgContent = sendContent;
    }
}
