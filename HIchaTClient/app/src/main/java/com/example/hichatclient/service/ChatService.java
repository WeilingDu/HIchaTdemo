package com.example.hichatclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hichatclient.ApplicationUtil;
import com.example.hichatclient.Test;
import com.example.hichatclient.data.entity.ChattingContent;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatService extends LifecycleService {
    private final IBinder binder = new ChatBinder();
    public MutableLiveData<Integer> isFriendMessage = new MutableLiveData<>(0);
    public List<ChattingContent> allChattingContents = new ArrayList<>();
    private ApplicationUtil applicationUtil;
    private Socket socket;

    public void setAllChattingContents(List<ChattingContent> allChattingContents) {
        this.allChattingContents = allChattingContents;
    }

    public class ChatBinder extends Binder {
        public ChatService getService(){
            return ChatService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        super.onBind(intent);
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                isFriendMessage.postValue(1);
            }
        }, 30000, 30000);
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        applicationUtil = (ApplicationUtil)getApplication();
        socket = applicationUtil.getSocket();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /** method for clients */

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



    public List<ChattingContent> getFriendMessagesByFriendID(String friendID){
        List<ChattingContent> msgs = new ArrayList<>();
        for(int i=0; i<allChattingContents.size(); i++){
            if (allChattingContents.get(i).getFriendID().equals(friendID)){
                msgs.add(allChattingContents.get(i));
            }
        }
        return msgs;
    }

}

