package com.example.hichatclient.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"userID", "friendID"})
public class ChattingContent {

    private String userID;
    private String friendID;

    @ColumnInfo(name = "msg_type")
    private String msgType;
    @ColumnInfo(name = "msg_time")
    private String msgTime;
    @ColumnInfo(name = "msg_content")
    private String msgContent;

    public ChattingContent(String userID, String friendID, String msgType, String msgTime, String msgContent) {
        this.userID = userID;
        this.friendID = friendID;
        this.msgType = msgType;
        this.msgTime = msgTime;
        this.msgContent = msgContent;
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

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String sendTime) {
        this.msgTime = sendTime;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String sendContent) {
        this.msgContent = sendContent;
    }
}
