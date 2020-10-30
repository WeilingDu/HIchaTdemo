package com.example.hichatclient.dataResource;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.hichatclient.data.ChatDatabase;
import com.example.hichatclient.data.dao.ChattingContentDao;
import com.example.hichatclient.data.dao.FriendDao;
import com.example.hichatclient.data.entity.ChattingContent;
import com.example.hichatclient.data.entity.Friend;

import java.util.List;

public class MessageRepository {
    private ChattingContentDao chattingContentDao;

    public MessageRepository(Context context) {
        ChatDatabase chatDatabase = ChatDatabase.getDatabase(context.getApplicationContext());
        chattingContentDao = chatDatabase.getChattingContentDao();
    }

    // 通过服务器发给好友消息
    public boolean sendMessageToServer(ChattingContent chattingContent, String userShortToken){
        boolean flag = true;
        return flag;
    }


    // 从数据库中获取用户和某好友的聊天记录
    public LiveData<List<ChattingContent>> getChattingContentFromSQL(String userID, String friendID) {
        LiveData<List<ChattingContent>> chattingContent;
        chattingContent = chattingContentDao.findAllContent(userID, friendID);
        return chattingContent;
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
