package com.example.hichatclient;

import android.app.Application;

import java.io.IOException;
import java.net.Socket;

public class ApplicationUtil extends Application {
    private Socket socketDynamic;
    private Socket socketStatic;
    private String userShortToken;
    private String userLongToken;

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
        this.socketStatic = connectThread.socketDynamic;
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
