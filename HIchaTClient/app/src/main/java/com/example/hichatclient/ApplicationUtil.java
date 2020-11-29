package com.example.hichatclient;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.net.Socket;

import jackmego.com.jieba_android.JiebaSegmenter;

public class ApplicationUtil extends Application {

    private Socket socketDynamic;
    private Socket socketStatic;
    private String userShortToken;
    private String userLongToken;
    private String userID;
    private String accessToken;  // 百度情感分析api的accessToken
    private String textAccessToken;  //  百度文本审核api的accessToken

    private long receive;

    public long getReceive() {
        return receive;
    }

    public void setReceive(long receive) {
        this.receive = receive;
    }


    public String getTextAccessToken() {
        return textAccessToken;
    }

    public void setTextAccessToken(String textAccessToken) {
        this.textAccessToken = textAccessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

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

    // ******** methods for socketDynamic ********
    public Socket getSocketDynamic() {
        return socketDynamic;
    }

    public void setSocketDynamic(Socket socketDynamic) {
        this.socketDynamic = socketDynamic;
    }

    public boolean dynamicIsConnected(){
        return this.socketDynamic.isConnected();
    }

    public void initSocketDynamic() throws IOException {
        System.out.println("***************init dynamic socket****************");
        this.socketDynamic = new Socket("49.234.105.69", 20001);
    }

    // ******** methods for socketStatic ********
    public Socket getSocketStatic() {
        return socketStatic;
    }

    public void setSocketStatic(Socket socketStatic) {
        this.socketStatic = socketStatic;
    }
    public boolean staticIsConnected(){
        return this.socketStatic.isConnected();
    }

    public void initSocketStatic() throws IOException {
        this.socketStatic = new Socket("49.234.105.69", 20001);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ConnectThread connectThread = new ConnectThread();
        connectThread.start();
        try {
            connectThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.socketDynamic = connectThread.socketDynamic;
        this.socketStatic = connectThread.socketStatic;

        // 异步初始化
        JiebaSegmenter.init(getApplicationContext());
    }

    static class ConnectThread extends Thread {
        private Socket socketDynamic;
        private Socket socketStatic;
        @Override
        public void run() {
            super.run();
            try {
                System.out.println("************************* init socket ***********************");
                this.socketDynamic = new Socket("49.234.105.69", 20001);
                this.socketStatic = new Socket("49.234.105.69", 20001);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
