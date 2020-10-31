package com.example.hichatclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.hichatclient.ApplicationUtil;
import com.example.hichatclient.data.entity.ChattingContent;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HeartbeatService extends Service{

    private ApplicationUtil applicationUtil;
    private Socket socket;


    public HeartbeatService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        applicationUtil = (ApplicationUtil)getApplication();
        socket = applicationUtil.getSocket();
        try {
            getFriendMessagesFromServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //heartBeatTest();
        
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void heartBeatTest(){
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("hello world");
            }
        }, 2, 600);

    }

    private void senHeartbeatToServer(String ip, String port, String shortToken){

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

    public void getFriendMessagesFromServer() throws IOException {
//        List<ChattingContent> chattingContents = new ArrayList<>();
//        ChattingContent chattingContent1 = new ChattingContent("10048", "10013", "receive", "1", "My name is 10012.");
//        ChattingContent chattingContent2 = new ChattingContent("10048", "10014", "receive", "1", "My name is 10013.");
//        ChattingContent chattingContent3 = new ChattingContent("10048", "10015", "receive", "1", "My name is 10014.");
//        ChattingContent chattingContent4 = new ChattingContent("10048", "10013", "receive", "1", "My name is 10012.");
//        ChattingContent chattingContent5 = new ChattingContent("10048", "10014", "receive", "1", "My name is 10013.");
//        chattingContents.add(chattingContent1);
//        chattingContents.add(chattingContent2);
//        chattingContents.add(chattingContent3);
//        chattingContents.add(chattingContent4);
//        chattingContents.add(chattingContent5);
//        setAllChattingContents(chattingContents);
//        isFriendMessage.setValue(0);
        String userID = "10014";
        List<ChattingContent> message = new ArrayList<>();
        List<String> friendID = new ArrayList<>();

        System.out.println(socket.isConnected());

        byte[] bytes = new byte[0];
        while(socket.isConnected()){
            InputStream is = socket.getInputStream();
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
                byte[] body = new byte[bodylength + PACKET_HEAD_LENGTH - bytes.length];//剩下应该读的字节(凑一个包)
                int couter = is.read(body);
                if (couter < 0) {
                    continue;
                }
                bytes = mergebyte(bytes, body, 0, couter);
                if (couter < body.length) {
                    continue;
                }
            }
            byte[] body = new byte[0];
            body = mergebyte(body, bytes, PACKET_HEAD_LENGTH, bytes.length);
            bytes = null;
            Test.RspToClient response = Test.RspToClient.parseFrom(body);
            Test.RspToClient.RspCase type = response.getRspCase();
            switch (type) {
                case CHAT_WITH_SERVER_RELAY:
                    Test.ChatWithServer.Relay chatRelay = response.getChatWithServerRelay();
                    ChattingContent chattingContent = new ChattingContent(userID,Integer.toString(chatRelay.getSrcId()),"receive",Long.toString(chatRelay.getTime()),chatRelay.getContent());
                    message.add(chattingContent);
                    System.out.println(chattingContent.getFriendID() + " " + chattingContent.getMsgContent());
                    if(!friendID.contains(Integer.toString(chatRelay.getSrcId()))){
                        friendID.add(Integer.toString(chatRelay.getSrcId()));
                    }
                    break;
                case ADD_FRIEND_SERVER_RSP_TO_A:
                    Test.AddFriend.ServerRspToA addFriendRsp = response.getAddFriendServerRspToA();
//                    String friendID = Integer.toString(addFriendRsp.getBId());
//                    boolean result = addFriendRsp.getRefuse();
                    break;
                case ADD_FRIEND_SERVER_RELAY_TO_B:
                    Test.AddFriend.ServerRelayToB addFriendRelay = response.getAddFriendServerRelayToB();
//                    String peopleID = Integer.toString(addFriendRelay.getAInfo().getId());
//                    String peopleName = addFriendRelay.getAInfo().getName();
//                    byte[] peopleHeadPic = addFriendRelay.getAInfo().getHeadpic().toByteArray();
//                    Long time = addFriendRelay.getATime();
                    break;
                case ERROR:
                    System.out.println("Fail!!!!");
                    break;
            }
        }

        //return chattingContent;
    }



}
