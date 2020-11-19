package com.example.hichatclient.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"userID", "friendID"})
public class Friend {

    @NonNull
    private String userID;
    @NonNull
    private String friendID;

    @ColumnInfo(name = "friend_name")
    private String friendName;
    @ColumnInfo(name = "friend_profile")
    private String friendProfile;
    @ColumnInfo(name = "friend_ip")
    private String friendIP;
    @ColumnInfo(name = "friend_port")
    private String friendPort;
    @ColumnInfo(name = "the_last_msg")
    private String theLastMsg;   // 用户与该好友的最后一条消息
    @ColumnInfo(name = "is_chatting")
    private boolean isChatting;  // 是否在聊天列表中

    public Friend(String userID, String friendID, String friendName, String friendProfile, String friendIP, String friendPort, String theLastMsg, boolean isChatting) {
        this.userID = userID;
        this.friendID = friendID;
        this.friendName = friendName;
        this.friendProfile = friendProfile;
        this.friendIP = friendIP;
        this.friendPort = friendPort;
        this.theLastMsg = theLastMsg;
        this.isChatting = isChatting;
    }

    public String getTheLastMsg() {
        return theLastMsg;
    }

    public void setTheLastMsg(String theLastMsg) {
        this.theLastMsg = theLastMsg;
    }

    public boolean isChatting() {
        return isChatting;
    }

    public void setChatting(boolean chatting) {
        isChatting = chatting;
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

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendProfile() {
        return friendProfile;
    }

    public void setFriendProfile(String friendProfile) {
        this.friendProfile = friendProfile;
    }

    public String getFriendIP() {
        return friendIP;
    }

    public void setFriendIP(String friendIP) {
        this.friendIP = friendIP;
    }

    public String getFriendPort() {
        return friendPort;
    }

    public void setFriendPort(String friendPort) {
        this.friendPort = friendPort;
    }

}
