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
import com.example.hichatclient.data.entity.MeToOthers;
import com.example.hichatclient.data.entity.OthersToMe;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatService extends LifecycleService {
    private final IBinder binder = new ChatBinder();
    private ApplicationUtil applicationUtil;
    private Socket socket;

    private String userID;
    private String userShortToken;
    private String userLongToken;

    private MutableLiveData<Integer> meToOthersFlag = new MutableLiveData<>(0);
    private MutableLiveData<Integer> othersToMeFlag = new MutableLiveData<>(0);


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

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        super.onBind(intent);
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("here");
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
        this.socket = applicationUtil.getSocketDynamic();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //




//    public List<ChattingContent> getFriendMessagesByFriendID(String friendID){
//        List<ChattingContent> msgs = new ArrayList<>();
//        for(int i=0; i<allChattingContents.size(); i++){
//            if (allChattingContents.get(i).getFriendID().equals(friendID)){
//                msgs.add(allChattingContents.get(i));
//            }
//        }
//        return msgs;
//    }

}

