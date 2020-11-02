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
//        applicationUtil = (ApplicationUtil)getApplication();
//        socket = applicationUtil.getSocket();
//        try {
//            getFriendMessagesFromServer();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        heartBeatTest();

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


        //return chattingContent;
    }



}
