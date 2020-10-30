package com.example.hichatclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hichatclient.data.entity.ChattingContent;

import java.util.Timer;
import java.util.TimerTask;

public class ChatService extends LifecycleService {
    private final IBinder binder = new ChatBinder();
    public MutableLiveData<Integer> isFriendMessage = new MutableLiveData<>(0);



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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /** method for clients */


    public ChattingContent getFriendMessage(){
        ChattingContent chattingContent = new ChattingContent("10048", "10001", "receive", "1", "My name is Vincent.");
        isFriendMessage.setValue(0);
        return chattingContent;
    }

}
