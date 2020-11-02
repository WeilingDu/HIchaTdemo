package com.example.hichatclient.dataResource;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.hichatclient.data.ChatDatabase;
import com.example.hichatclient.data.dao.MeToOthersDao;
import com.example.hichatclient.data.dao.OthersToMeDao;
import com.example.hichatclient.data.dao.UserDao;
import com.example.hichatclient.data.entity.MeToOthers;
import com.example.hichatclient.data.entity.OthersToMe;
import com.example.hichatclient.data.entity.User;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class NewFriendsRepository {
    private MeToOthersDao meToOthersDao;
    private OthersToMeDao othersToMeDao;

    public NewFriendsRepository(Context context){
        ChatDatabase chatDatabase = ChatDatabase.getDatabase(context.getApplicationContext());
        meToOthersDao = chatDatabase.getMeToOthersDao();
        othersToMeDao = chatDatabase.getOthersToMeDao();
    }

    // 向服务器提交对别人好友请求的回应
    public void othersToMeResponseToServer(String userShortToken, String objectID, boolean refuse, Socket socket) throws IOException {
        Test.AddFriend.BToServer.Builder othersToMeRsp = Test.AddFriend.BToServer.newBuilder();
        othersToMeRsp.setAId(Integer.parseInt(objectID));
        othersToMeRsp.setBShortToken(userShortToken);
        othersToMeRsp.setRefuse(refuse);
        Test.ReqToServer.Builder reqToServer = Test.ReqToServer.newBuilder();
        reqToServer.setAddFriendBToServer(othersToMeRsp);
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


    // 当用户回应别人的好友请求时，更新数据库中的信息
    public void updateOthersToMeResponse(OthersToMe othersToMe){
        new updateOthersToMeResponseThread(othersToMeDao, othersToMe).start();
    }


    public LiveData<List<MeToOthers>> getAllMeToOthersFromSQL(String userID){
        return meToOthersDao.getAllMeToOthers(userID);
    }

    public LiveData<List<OthersToMe>> getAllOthersToMeFromSQL(String userID){
        return othersToMeDao.getAllOthersToMe(userID);
    }

    public void updateMeToOthers(List<MeToOthers> meToOthers){
        new updateMeToOtherThread(meToOthersDao, meToOthers).start();
    }

    public void updateOthersToMe(List<OthersToMe> othersToMes){
        new updateOthersToMeThread(othersToMeDao, othersToMes).start();
    }

    public OthersToMe getOthersToMeByObjectID(String userID, String objectID) throws InterruptedException {
        GetOthersToMeByObjectIDThread getOthersToMeByObjectIDThread = new GetOthersToMeByObjectIDThread(othersToMeDao, userID, objectID);
        getOthersToMeByObjectIDThread.start();
        getOthersToMeByObjectIDThread.join();
        return getOthersToMeByObjectIDThread.othersToMe;
    }


    static class updateOthersToMeResponseThread extends Thread{
        OthersToMeDao othersToMeDao;
        OthersToMe othersToMe;

        public updateOthersToMeResponseThread(OthersToMeDao othersToMeDao, OthersToMe othersToMe) {
            this.othersToMeDao = othersToMeDao;
            this.othersToMe = othersToMe;
        }

        @Override
        public void run() {
            super.run();
            othersToMeDao.updateOthersToMe(othersToMe);
        }
    }


    static class GetOthersToMeByObjectIDThread extends Thread{
        OthersToMeDao othersToMeDao;
        String userID;
        String objectID;
        OthersToMe othersToMe;

        public GetOthersToMeByObjectIDThread(OthersToMeDao othersToMeDao, String userID, String objectID) {
            this.othersToMeDao = othersToMeDao;
            this.userID = userID;
            this.objectID = objectID;
        }

        @Override
        public void run() {
            super.run();
            this.othersToMe = othersToMeDao.getOthersToMeByObjectID(userID, objectID);
        }
    }

    static class updateMeToOtherThread extends Thread{
        MeToOthersDao meToOthersDao;
        List<MeToOthers> meToOthers;

        public updateMeToOtherThread(MeToOthersDao meToOthersDao, List<MeToOthers> meToOthers){
            this.meToOthersDao = meToOthersDao;
            this.meToOthers = meToOthers;
        }

        @Override
        public void run() {
            super.run();
            meToOthersDao.insertAllMeToOthers(meToOthers);
        }
    }

    static class updateOthersToMeThread extends Thread{
        OthersToMeDao othersToMeDao;
        List<OthersToMe> othersToMes;

        public updateOthersToMeThread(OthersToMeDao othersToMeDao, List<OthersToMe> othersToMes){
            this.othersToMeDao = othersToMeDao;
            this.othersToMes = othersToMes;
        }

        @Override
        public void run() {
            super.run();
            othersToMeDao.insertAllOthersToMe(othersToMes);
        }
    }


}
