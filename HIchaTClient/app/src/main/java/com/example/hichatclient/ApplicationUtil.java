package com.example.hichatclient;

import android.app.Application;

import java.io.IOException;
import java.net.Socket;

public class ApplicationUtil extends Application {
    private Socket socket;

    public Socket getSocket() {
        return socket;
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
        this.socket = connectThread.socket;
    }

    static class ConnectThread extends Thread {
        private Socket socket;
        @Override
        public void run() {
            super.run();
            try {
                System.out.println("************************* init socket ***********************");
                this.socket = new Socket("49.234.105.69", 20001);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
