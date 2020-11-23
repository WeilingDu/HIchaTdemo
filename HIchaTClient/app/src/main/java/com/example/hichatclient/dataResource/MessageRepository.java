package com.example.hichatclient.dataResource;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.hichatclient.Test;
import com.example.hichatclient.data.ChatDatabase;
import com.example.hichatclient.data.dao.ChattingContentDao;
import com.example.hichatclient.data.dao.ChattingFriendDao;
import com.example.hichatclient.data.dao.FriendDao;
import com.example.hichatclient.data.entity.ChattingContent;
import com.example.hichatclient.data.entity.ChattingFriend;
import com.example.hichatclient.data.entity.Friend;

import java.io.IOException;
import java.io.OutputStream;
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
    public boolean sendMessageToServer(ChattingContent chattingContent, String userShortToken, Socket socket) throws IOException {
        boolean flag = true;
        System.out.println("MessageRepository userShortToken: " + userShortToken);
        System.out.println("MessageRepository content: " + chattingContent.getMsgTime() + chattingContent.getMsgType() + chattingContent.getMsgContent());
        Test.ChatWithServer.Req.Builder chatWithServerReq = Test.ChatWithServer.Req.newBuilder();
        chatWithServerReq.setShortToken(userShortToken);
        chatWithServerReq.setObjId(Integer.parseInt(chattingContent.getFriendID()));
        chatWithServerReq.setTime(chattingContent.getMsgTime());

        chatWithServerReq.setContent(chattingContent.getMsgContent());

        Test.ReqToServer.Builder reqToServer = Test.ReqToServer.newBuilder();
        reqToServer.setChatWithServerReq(chatWithServerReq);
        byte[] request = reqToServer.build().toByteArray();
        byte[] len = new byte[4];
        for (int i = 0;  i < 4;  i++)
        {
            len[3-i] = (byte)((request.length >> (8 * i)) & 0xFF);
        }
        byte[] send_data = new byte[request.length + len.length];
        System.arraycopy(len, 0, send_data, 0, len.length);
        System.arraycopy(request, 0, send_data, len.length, request.length);

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(send_data);
        outputStream.flush();
        return flag;
    }

    // 给服务器发送已读消息
    public void sendReadMsgToServer(String userShortToken, String friendID, long time, Socket socket) throws IOException {
        Test.Seen.AToServer.Builder seenAToServer = Test.Seen.AToServer.newBuilder();
        seenAToServer.setShortToken(userShortToken);
        seenAToServer.setObjId(Integer.parseInt(friendID));
        seenAToServer.setTime(time);

        Test.ReqToServer.Builder reqToServer = Test.ReqToServer.newBuilder();
        reqToServer.setSeenAToServer(seenAToServer);
        byte[] request = reqToServer.build().toByteArray();
        byte[] len = new byte[4];
        for (int i = 0;  i < 4;  i++)
        {
            len[3-i] = (byte)((request.length >> (8 * i)) & 0xFF);
        }
        byte[] send_data = new byte[request.length + len.length];
        System.arraycopy(len, 0, send_data, 0, len.length);
        System.arraycopy(request, 0, send_data, len.length, request.length);

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(send_data);
        outputStream.flush();

    }

    // 从数据库中获取用户和某好友的聊天记录
    public LiveData<List<ChattingContent>> getChattingContentFromSQL(String userID, String friendID) {
        return chattingContentDao.findAllContent(userID, friendID);
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
        new insertOneMessageIntoSQLThread(chattingContentDao, chattingContent).start();
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
