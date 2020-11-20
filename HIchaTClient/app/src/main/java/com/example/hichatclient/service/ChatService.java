package com.example.hichatclient.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hichatclient.ApplicationUtil;
import com.example.hichatclient.Test;
import com.example.hichatclient.data.ChatDatabase;
import com.example.hichatclient.data.dao.ChattingContentDao;
import com.example.hichatclient.data.dao.FriendDao;
import com.example.hichatclient.data.dao.OthersToMeDao;
import com.example.hichatclient.data.entity.ChattingContent;
import com.example.hichatclient.data.entity.Friend;
import com.example.hichatclient.data.entity.MeToOthers;
import com.example.hichatclient.data.entity.OthersToMe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatService extends LifecycleService {
    private final IBinder binder = new ChatBinder();
    private ApplicationUtil applicationUtil;
    private Socket socket;
    private ChattingContentDao chattingContentDao;
    private FriendDao friendDao;
    private OthersToMeDao othersToMeDao;

    public ApplicationUtil getApplicationUtil() {
        return applicationUtil;
    }

    public void setApplicationUtil(ApplicationUtil applicationUtil) {
        this.applicationUtil = applicationUtil;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ChattingContentDao getChattingContentDao() {
        return chattingContentDao;
    }

    public void setChattingContentDao(ChattingContentDao chattingContentDao) {
        this.chattingContentDao = chattingContentDao;
    }

    public FriendDao getFriendDao() {
        return friendDao;
    }

    public void setFriendDao(FriendDao friendDao) {
        this.friendDao = friendDao;
    }

    private String userID;
    private String userShortToken;
    private String userLongToken;

    private MutableLiveData<Integer> meToOthersFlag = new MutableLiveData<>(0);
    private MutableLiveData<Integer> othersToMeFlag = new MutableLiveData<>(0);
    private MutableLiveData<Integer> longTokenFlag = new MutableLiveData<>(0);

    private List<MeToOthers> meToOthersNew = new ArrayList<>();
    private List<OthersToMe> othersToMesNew = new ArrayList<>();

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public MutableLiveData<Integer> getMeToOthersFlag() {
        return meToOthersFlag;
    }

    public void setMeToOthersFlag(MutableLiveData<Integer> meToOthersFlag) {
        this.meToOthersFlag = meToOthersFlag;
    }

    public MutableLiveData<Integer> getOthersToMeFlag() {
        return othersToMeFlag;
    }

    public void setOthersToMeFlag(MutableLiveData<Integer> othersToMeFlag) {
        this.othersToMeFlag = othersToMeFlag;
    }

    public List<MeToOthers> getMeToOthersNew() {
        return meToOthersNew;
    }

    public void setMeToOthersNew(List<MeToOthers> meToOthersNew) {
        this.meToOthersNew = meToOthersNew;
    }

    public List<OthersToMe> getOthersToMesNew() {
        return othersToMesNew;
    }

    public void setOthersToMesNew(List<OthersToMe> othersToMesNew) {
        this.othersToMesNew = othersToMesNew;
    }

    public class ChatBinder extends Binder {
        public ChatService getService(){
            return ChatService.this;
        }
    }

    public MutableLiveData<Integer> getLongTokenFlag() {
        return longTokenFlag;
    }

    public void setLongTokenFlag(MutableLiveData<Integer> longTokenFlag) {
        this.longTokenFlag = longTokenFlag;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        super.onBind(intent);
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.setApplicationUtil((ApplicationUtil)getApplication());
        setUserShortToken(applicationUtil.getUserShortToken());
        setUserID(applicationUtil.getUserID());
        System.out.println("ChatService: " + userShortToken);
        System.out.println("ChatService: " + userID);
        ChatDatabase chatDatabase = ChatDatabase.getDatabase(this.getApplicationContext());
        this.othersToMeDao = chatDatabase.getOthersToMeDao();
//        this.chattingContentDao = chatDatabase.getChattingContentDao();
        //this.friendDao = chatDatabase.getFriendDao();

        System.out.println("hello world 1!");
        this.setSocket(applicationUtil.getSocketDynamic());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // ****** test *******
//                senHeartbeatToServer();
//                Timer t = new Timer();
//                t.scheduleAtFixedRate(new TimerTask() {
//                    @Override
//                    public void run() {
//                        System.out.println("hello world 2!");
//                    }
//                }, 1000, 1000);

                // ******* 服务器 ******
                try {
//                    testInsert();
                    senHeartbeatToServer();
                    getMessagesFromServer();
                    System.out.println("hello world 2!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static final int PACKET_HEAD_LENGTH = 4;//从服务器接收的数据包头长度
    //处理粘包、半包问题使用的数组合并函数
    public static byte[] mergebyte(byte[] a, byte[] b, int begin, int end) {
        byte[] add = new byte[a.length + end - begin];
        int i = 0;
        for (i = 0; i < a.length; i++) {
            add[i] = a[i];
        }
        for (int k = begin; k < end; k++, i++) {
            add[i] = b[k];
        }
        return add;
    }

    private int updateShortTokenFlag = 0;

    public void testInsert(){
        ChattingContent chattingContent = new ChattingContent("123", "123", "send", "111", "hello");
        chattingContentDao.insertContent(chattingContent);
//        Friend friend1 = new Friend("10012", "123", "jane", "111", "111", "2", "hello", true);
//        Friend friend2 = new Friend("10012", "124", "jane2", "111", "111", "2", "hello", true);
//        friendDao.insertFriend(friend1);
//        friendDao.insertFriend(friend2);
    }



    //发送心跳
    public void senHeartbeatToServer(){
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (userShortToken != null){
                        if(updateShortTokenFlag == 1){
                            getNewShortToken();
                        }
                        sendHeartbeat();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 10000, 10000);
    }

    //监听服务器发来的所有消息
    public void getMessagesFromServer() throws IOException {
        byte[] bytes = new byte[0];
        while(socket.isConnected()){
            InputStream is = socket.getInputStream();
            assert bytes != null;
            if (bytes.length < PACKET_HEAD_LENGTH) {
                byte[] head = new byte[PACKET_HEAD_LENGTH - bytes.length];
                int couter = is.read(head);
                if (couter < 0) {
                    continue;
                }
                bytes = mergebyte(bytes, head, 0, couter);
                if (couter < PACKET_HEAD_LENGTH) {
                    continue;
                }
            }
            // 下面这个值请注意，一定要取4长度的字节子数组作为报文长度
            byte[] temp = new byte[0];
            temp = mergebyte(temp, bytes, 0, PACKET_HEAD_LENGTH);
            int bodylength = 0; //包体长度
            for(int i=0;i<temp.length;i++){
                bodylength += (temp[i] & 0xff) << ((3-i)*8);
            }
            if (bytes.length - PACKET_HEAD_LENGTH < bodylength) {//不够一个包
                byte[] body;
                if((bodylength + PACKET_HEAD_LENGTH - bytes.length) > 2530686){
                    body = new byte[2530686];
                }
                else{
                    body = new byte[bodylength + PACKET_HEAD_LENGTH - bytes.length];//剩下应该读的字节(凑一个包)
                }
                int couter = is.read(body);
                if (couter < 0) {
                    continue;
                }
                bytes = mergebyte(bytes, body, 0, couter);
                if (couter < bodylength + PACKET_HEAD_LENGTH - bytes.length) {
                    continue;
                }
            }
            byte[] body = new byte[0];
            body = mergebyte(body, bytes, PACKET_HEAD_LENGTH, bytes.length);
            bytes = new byte[0];
            Test.RspToClient response = Test.RspToClient.parseFrom(body);
            Test.RspToClient.RspCase type = response.getRspCase();
            switch (type) {
                case ADD_FRIEND_FROM_OTHER_RSP:
                    System.out.println("ChatService: add_friend_from_other");
                    addFriendReqOther(response);
                    break;
                case ADD_FRIEND_FROM_SELF_RSP:
                    addFriendReqSelf(response);
                    break;
                case UNRECEIVED_MSG_RES:
                    unreceivedMessage(response);
                    break;
                case GET_TOKEN_RES:
                    updateShortToken(response);
                    break;
                case CHAT_WITH_SERVER_RELAY:
                    chatMessage(response);
                    break;
                case DELETE_FRIEND_SERVER_TO_B:
                    beDeleted(response);
                    break;
                case SEEN_SERVER_TO_B:
                    messageRead(response);
                    break;
                case ERROR:
                    switch(response.getError().getErrorType()){
                        case UNRECOGNIZE_SHORT_TOKEN:
                            updateShortTokenFlag = 1;
                            break;
                        case UNRECOGNIZE_LONG_TOKEN:
                            //通知前面
                            longTokenFlag.setValue(1);
                            break;
                    }
                    System.out.println("Fail!!!!");
                    break;
            }
        }
    }

    //向服务器请求新的shortToken
    public void getNewShortToken() throws IOException {
        Test.GetToken.Req.Builder getTokenReq = Test.GetToken.Req.newBuilder();
        getTokenReq.setLongToken(userLongToken);
        Test.ReqToServer.Builder reqToServer = Test.ReqToServer.newBuilder();
        reqToServer.setGetTokenReq(getTokenReq);

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

    //向服务器发送心跳包
    public void sendHeartbeat() throws IOException {
        Test.HeartBeat.Req.Builder heartBeatReq = Test.HeartBeat.Req.newBuilder();
        heartBeatReq.setShortToken(userShortToken);
        int port = socket.getLocalPort();
        heartBeatReq.setInPort(port);
        int ip = 1234567;
        heartBeatReq.setInIp(ip);
        Test.ReqToServer.Builder reqToServer = Test.ReqToServer.newBuilder();
        reqToServer.setHeartBeatReq(heartBeatReq);

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

    //监听别人发来的好友请求
    public void addFriendReqOther(Test.RspToClient response){
        Test.AddFriendFromOther.Rsp addFriendFromOtherRsp = response.getAddFriendFromOtherRsp();
        int num = addFriendFromOtherRsp.getUserCount();
        for(int i = 0;i < num; i++){
            Test.People reqi = addFriendFromOtherRsp.getUser(i);
            OthersToMe othersToMe = new OthersToMe(userID,Integer.toString(reqi.getId()),reqi.getName(),"reqi.getHeadpic()","wait");
            System.out.println(reqi.getId() + reqi.getName());

            othersToMeDao.insertOthersToMe(othersToMe);
            // othersToMesNew.add(othersToMe);
        }
//        othersToMeFlag.setValue(1);
    }

    //监听自己给别人发送的好友请求的状态
    public void addFriendReqSelf(Test.RspToClient response){
        Test.AddFriendFromSelf.Rsp addFriendFromSelfRsp = response.getAddFriendFromSelfRsp();
        int num = addFriendFromSelfRsp.getRequestsCount();
        for(int i = 0; i < num; i++){
            Test.AddFriendFromSelf.Rsp.RequestFromSelf reqi = addFriendFromSelfRsp.getRequests(i);
            if(reqi.getStatus().toString().equals("00")){
                MeToOthers meToOthers = new MeToOthers(userID,Integer.toString(reqi.getObjUser().getId()),reqi.getObjUser().getName(),"reqi.getObjUser().getHeadpic()","wait");
                meToOthersNew.add(meToOthers);
            }
            else if((reqi.getStatus().toString().equals("01"))){
                MeToOthers meToOthers = new MeToOthers(userID,Integer.toString(reqi.getObjUser().getId()),reqi.getObjUser().getName(),"reqi.getObjUser().getHeadpic()","agree");
                meToOthersNew.add(meToOthers);
            }
            else if((reqi.getStatus().toString().equals("10"))){
                MeToOthers meToOthers = new MeToOthers(userID,Integer.toString(reqi.getObjUser().getId()),reqi.getObjUser().getName(),"reqi.getObjUser().getHeadpic()","refuse");
                meToOthersNew.add(meToOthers);
            }
        }
//        meToOthersFlag.setValue(1);
    }

    //监听掉线/离线期间没有接收到的消息
    public void unreceivedMessage(Test.RspToClient response){

    }

    //更新shortToken
    public void updateShortToken(Test.RspToClient response){
        userShortToken = response.getGetTokenRes().getShortToken();
        applicationUtil.setUserShortToken(userShortToken);
    }

    //实时接收别人发来的聊天消息
    public void chatMessage(Test.RspToClient response){

    }

    //接收被好友删除的通知
    public void beDeleted(Test.RspToClient response){

    }

    //发出的消息已被好友阅读
    public void messageRead(Test.RspToClient response){

    }

}

