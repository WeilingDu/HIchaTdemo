package com.example.hichatclient.dataResource;

import android.content.Context;

import com.example.hichatclient.Test;
import com.example.hichatclient.data.ChatDatabase;
import com.example.hichatclient.data.entity.User;
import com.example.hichatclient.data.dao.UserDao;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepository {
    private UserDao userDao;

    public UserRepository(Context context) {
        ChatDatabase chatDatabase = ChatDatabase.getDatabase(context.getApplicationContext());
        userDao = chatDatabase.getUserDao();
    }



    // 登录（用于本地测试）
    public User sendIDAndLogInTest(String userID, String userPassword){
        int isLogIn = 1;
        if (isLogIn == 1){
            User user = new User(userID, userPassword, "jane", "111", "123", "123");
            return user;
        }else {
            return null;
        }
    }
    // 登录
    public User sendIDAndLogIn(String userID, String userPassword) throws InterruptedException {
        SendIDAndLogInThread sendIDAndLogInThread = new SendIDAndLogInThread(userID, userPassword);
        sendIDAndLogInThread.start();
        sendIDAndLogInThread.join();
        return sendIDAndLogInThread.user;
    }
    static class SendIDAndLogInThread extends Thread {
        private String userID;
        private String userPassword;
        private User user;

        public SendIDAndLogInThread(String userID, String userPassword){
            this.userID = userID;
            this.userPassword = userPassword;
        }

        @Override
        public void run() {
            super.run();
            int isLogIn = 1;
            String userName = null;
//        byte[] picBytes = null;
//        Bitmap userHeadPic = null;
            String shortToken = null;
            String longToken = null;
            int ip = 0;
            int port = 0;

            Socket socket = null;
            try {
                socket = new Socket("49.234.105.69", 20001);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(socket.isConnected());
            // **********发送"用户名和密码"***********
            Test.Login.Req.Builder loginRequest = Test.Login.Req.newBuilder();
            loginRequest.setId(Integer.parseInt(userID));
            loginRequest.setPassword(userPassword);
            port = socket.getLocalPort();
            loginRequest.setInPort(port);
            ip = 1234567;
//        ip = Integer.parseInt(socket.getLocalAddress().getHostAddress().toString());
            loginRequest.setInIp(ip);
            Test.ReqToServer.Builder reqToServer = Test.ReqToServer.newBuilder();
            reqToServer.setLoginReq(loginRequest);
            byte[] request = reqToServer.build().toByteArray();
            byte[] len = new byte[4];
            for (int i = 0;  i < 4;  i++)
            {
                len[3-i] = (byte)((request.length >> (8 * i)) & 0xFF);
            }
            byte[] send_data = new byte[request.length + len.length];
            System.arraycopy(len, 0, send_data, 0, len.length);
            System.arraycopy(request, 0, send_data, len.length, request.length);

            OutputStream outputStream = null;
            try {
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.write(send_data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // **********接收"是否登录成功"***********
            InputStream is = null;
            try {
                is = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int count = 0;
            while (count == 0) {
                try {
                    count = is.available();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            byte[] b = new byte[count];
            try {
                is.read(b);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] result = new byte[count-4];
            System.arraycopy(b, 4, result, 0, count-4);
            Test.RspToClient response = null;
            try {
                response = Test.RspToClient.parseFrom(result);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            Test.RspToClient.RspCase type = response.getRspCase();
            switch (type){
                case LOGIN_RES:
                    isLogIn = 1;
                    userName = response.getLoginRes().getName();
//                picBytes = response.getLoginRes().getHeadpic().toByteArray();
//                if (picBytes.length != 0) {
//                    userHeadPic = BitmapFactory.decodeByteArray(picBytes, 0, b.length);
//                } else {
//                    return null;
//                }
                    shortToken = response.getLoginRes().getShortToken();
                    longToken = response.getLoginRes().getLongToken();
                    break;
                case ERROR:
                    isLogIn = 0;
                    System.out.println("Fail!!!!");
                    break;
            }

            if (isLogIn == 1){
                User user = new User(userID, userPassword, userName, "111", shortToken, longToken);
                this.user = user;
            } else {
                this.user = null;
            }

        }
    }

    // 注册
    public String signUp(String userName, String userPassword) throws InterruptedException {
        SignUpThread signUpThread = new SignUpThread(userName, userPassword);
//        System.out.println("repository");
        signUpThread.start();
        signUpThread.join();
        return signUpThread.userID;
    }
    static class SignUpThread extends Thread {
        private String userName;
        private String userPassword;
        private String userID;


        public SignUpThread(String userName, String userPassword){
            this.userName = userName;
            this.userPassword = userPassword;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("Thread-run");
            String userID = null;
            Socket socket = null;
            try {
                socket = new Socket("49.234.105.69", 20001);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(socket.isConnected());
            // **********发送"用户名和密码"***********
            Test.Register.Req.Builder registerRequest = Test.Register.Req.newBuilder();
            registerRequest.setName(userName);
            registerRequest.setPassword(userPassword);
//        int bytes = userHeadPic.getByteCount();
//        ByteBuffer buffer = ByteBuffer.allocate(bytes);
//        userHeadPic.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
//        registerRequest.setHeadpic(ByteString.copyFrom(buffer.array()));
            String headpic = "111";
            registerRequest.setHeadpic(ByteString.copyFrom(headpic.getBytes()));
            Test.ReqToServer.Builder reqToServer = Test.ReqToServer.newBuilder();
            reqToServer.setRegisterReq(registerRequest);
            byte[] request = reqToServer.build().toByteArray();
            byte[] len = new byte[4];
            for (int i = 0;  i < 4;  i++)
            {
                len[3-i] = (byte)((request.length >> (8 * i)) & 0xFF);
            }
            byte[] send_data = new byte[request.length + len.length];
            System.arraycopy(len, 0, send_data, 0, len.length);
            System.arraycopy(request, 0, send_data, len.length, request.length);
            OutputStream outputStream = null;
            try {
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.write(send_data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // **********接收"是否连接成功和用户ID"***********
            InputStream is = null;
            try {
                is = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int count = 0;
            while (count == 0) {
                try {
                    count = is.available();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            byte[] b = new byte[count];
            try {
                is.read(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] result = new byte[count-4];
            System.arraycopy(b, 4, result, 0, count-4);

            Test.RspToClient response = null;
            try {
                response = Test.RspToClient.parseFrom(result);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            Test.RspToClient.RspCase type = response.getRspCase();
            switch (type){
                case REGISTER_RES:
                    int tmp = response.getRegisterRes().getId();
                    userID = Integer.toString(tmp);
                    break;
                case ERROR:
                    System.out.println("Fail!!!!");
                    break;
            }
            System.out.println(userID);
            this.userID = userID;
        }
    }


    // 向数据库库添加登录成功的用户信息
    public void insertUser(User user){
        new insertUserThread(userDao, user).start();
    }

    static class insertUserThread extends Thread {
        UserDao userDao;
        User user;

        public insertUserThread(UserDao userDao, User user) {
            this.userDao = userDao;
            this.user = user;
        }

        @Override
        public void run() {
            super.run();
            userDao.insertUser(user);
            System.out.println("reposi");
        }
    }

}
