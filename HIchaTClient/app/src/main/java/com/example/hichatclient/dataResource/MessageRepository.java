package com.example.hichatclient.dataResource;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.hichatclient.data.ChatDatabase;
import com.example.hichatclient.data.dao.ChattingContentDao;
import com.example.hichatclient.data.dao.ChattingFriendDao;
import com.example.hichatclient.data.dao.FriendDao;
import com.example.hichatclient.data.entity.ChattingContent;
import com.example.hichatclient.data.entity.ChattingFriend;
import com.example.hichatclient.data.entity.Friend;

import java.net.Socket;
import java.util.List;

public class MessageRepository {
    private ChattingContentDao chattingContentDao;
    private ChattingFriendDao chattingFriendDao;

    public MessageRepository(Context context) {
        ChatDatabase chatDatabase = ChatDatabase.getDatabase(context.getApplicationContext());
        chattingContentDao = chatDatabase.getChattingContentDao();
        chattingFriendDao = chatDatabase.getChattingFriendDao();
    }

    // 通过服务器发给好友消息
    public boolean sendMessageToServer(ChattingContent chattingContent, String userShortToken, Socket socket) {
        boolean flag = true;
        return flag;
    }


    // 从数据库中获取用户和某好友的聊天记录
    public LiveData<List<ChattingContent>> getChattingContentFromSQL(String userID, String friendID) {
        LiveData<List<ChattingContent>> chattingContent;
        chattingContent = chattingContentDao.findAllContent(userID, friendID);
        return chattingContent;
    }

    // 从数据库获取用户正在聊天的好友列表
    public LiveData<List<ChattingFriend>> getAllChattingFriendFromSQL(String userID){
        return chattingFriendDao.findAllChattingFriend(userID);
    }

    // 更新数据库中的聊天框
    public void updateChattingFriendIntoSQL(ChattingFriend chattingFriend){
        new updateChattingFriendIntoSQLThread(chattingFriendDao, chattingFriend).start();
    }
    static class updateChattingFriendIntoSQLThread extends Thread{
        ChattingFriendDao chattingFriendDao;
        ChattingFriend chattingFriend;

        public updateChattingFriendIntoSQLThread(ChattingFriendDao chattingFriendDao, ChattingFriend chattingFriend) {
            this.chattingFriendDao = chattingFriendDao;
            this.chattingFriend = chattingFriend;
        }

        @Override
        public void run() {
            super.run();
            chattingFriendDao.insertChattingFriend(chattingFriend);
        }
    }


    // 往数据库添加一条聊天信息
    public void insertOneMessageIntoSQL(ChattingContent chattingContent){
        new insertOneMessageIntoSQLThread(chattingContentDao, chattingContent);
    }

    static class insertOneMessageIntoSQLThread extends Thread{
        ChattingContentDao chattingContentDao;
        ChattingContent chattingContent;

        public insertOneMessageIntoSQLThread(ChattingContentDao chattingContentDao, ChattingContent chattingContent) {
            this.chattingContentDao = chattingContentDao;
            this.chattingContent = chattingContent;
        }

        @Override
        public void run() {
            super.run();
            chattingContentDao.insertContent(chattingContent);
        }
    }

}
