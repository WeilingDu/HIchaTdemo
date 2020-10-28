package com.example.hichatclient.dataResource;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.hichatclient.data.ChatDatabase;
import com.example.hichatclient.data.dao.ChattingContentDao;
import com.example.hichatclient.data.entity.ChattingContent;

import java.util.List;

public class MessageRepository {
    private ChattingContentDao chattingContentDao;

    public MessageRepository(Context context) {
        ChatDatabase chatDatabase = ChatDatabase.getDatabase(context.getApplicationContext());
        chattingContentDao = chatDatabase.getChattingContentDao();
    }


    // 从数据库中获取用户和某好友的聊天记录
    public LiveData<List<ChattingContent>> getChattingContentFromSQL(String userID, String friendID) {
        LiveData<List<ChattingContent>> chattingContent;
        chattingContent = chattingContentDao.findAllContent(userID, friendID);
        return chattingContent;
    }


}
