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

public class UserRepository {
    private UserDao userDao;

    public UserRepository(Context context) {
        ChatDatabase chatDatabase = ChatDatabase.getDatabase(context.getApplicationContext());
        userDao = chatDatabase.getUserDao();
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
    public User sendIDAndLogIn(String userID, String userPassword, Socket socket) throws InterruptedException {
        SendIDAndLogInThread sendIDAndLogInThread = new SendIDAndLogInThread(userID, userPassword, socket);
        sendIDAndLogInThread.start();
        sendIDAndLogInThread.join();
        return sendIDAndLogInThread.user;
    }
    static class SendIDAndLogInThread extends Thread {
        private String userID;
        private String userPassword;
        private User user;
        private Socket socket;


        public SendIDAndLogInThread(String userID, String userPassword, Socket socket){
            this.userID = userID;
            this.userPassword = userPassword;
            this.socket = socket;

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


            System.out.println(socket.isConnected());
            // **********发送"用户名和密码"***********cc
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
            while(socket.isConnected()){
                InputStream is = null;
                try {
                    is = socket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] bytes = new byte[0];
                if (bytes.length < PACKET_HEAD_LENGTH) {
                    byte[] head = new byte[PACKET_HEAD_LENGTH - bytes.length];
                    int couter = 0;
                    try {
                        couter = is.read(head);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                    int couter = 0;
                    try {
                        couter = is.read(body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                Test.RspToClient response = null;
                try {
                    response = Test.RspToClient.parseFrom(body);
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
    public String signUp(String userName, String userPassword, Socket socket) throws InterruptedException {
        SignUpThread signUpThread = new SignUpThread(userName, userPassword, socket);
//        System.out.println("repository");
        signUpThread.start();
        signUpThread.join();
        return signUpThread.userID;
    }
    static class SignUpThread extends Thread {
        private String userName;
        private String userPassword;
        private String userID;
        private Socket socket;


        public SignUpThread(String userName, String userPassword, Socket socket){
            this.userName = userName;
            this.userPassword = userPassword;
            this.socket = socket;
        }



        @Override
        public void run() {
            super.run();
            System.out.println("Thread-run");
            String userID = null;
            System.out.println(socket.isConnected());
            // **********发送"昵称和密码"***********
            Test.Register.Req.Builder registerRequest = Test.Register.Req.newBuilder();
            registerRequest.setName(userName);
            registerRequest.setPassword(userPassword);
//        int imageBytes = userHeadPic.getByteCount();
//        ByteBuffer buffer = ByteBuffer.allocate(imageBytes);
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
            while(socket.isConnected())
            {
                InputStream is = null;
                try {
                    is = socket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] bytes = new byte[0];
                if (bytes.length < PACKET_HEAD_LENGTH) {
                    byte[] head = new byte[PACKET_HEAD_LENGTH - bytes.length];
                    int couter = 0;
                    try {
                        couter = is.read(head);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                    int couter = 0;
                    try {
                        couter = is.read(body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                Test.RspToClient response = null;
                try {
                    response = Test.RspToClient.parseFrom(body);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                Test.RspToClient.RspCase type = response.getRspCase();
                switch (type){
                    case REGISTER_RES:
                        userID = Integer.toString(response.getRegisterRes().getId());
                        break;
                    case ERROR:
                        System.out.println("Fail!!!!");
                        break;
                }
                break;
            }
            System.out.println(userID);
            this.userID = userID;
        }
    }



    // 向数据库添加登录成功的用户信息
    public void insertUser(User user) throws InterruptedException {
        GetUserInfoByUserIDThread getUserInfoByUserIDThread = new GetUserInfoByUserIDThread(userDao, user.getUserID());
        getUserInfoByUserIDThread.start();
        getUserInfoByUserIDThread.join();
        if (getUserInfoByUserIDThread.user != null){
            UpdateUserInfoSQLThread updateUserInfoSQLThread = new UpdateUserInfoSQLThread(userDao, user);
            updateUserInfoSQLThread.start();
        } else {
            InsertUserThread insertUserThread = new InsertUserThread(userDao, user);
            insertUserThread.start();
        }
    }

    static class InsertUserThread extends Thread {
        UserDao userDao;
        User user;

        public InsertUserThread(UserDao userDao, User user) {
            this.userDao = userDao;
            this.user = user;
        }

        @Override
        public void run() {
            super.run();
            userDao.insertUser(user);
        }
    }

    static class UpdateUserInfoSQLThread extends Thread {
        UserDao userDao;
        User user;

        public UpdateUserInfoSQLThread(UserDao userDao, User user){
            this.userDao = userDao;
            this.user = user;
        }

        @Override
        public void run() {
            super.run();
            userDao.updateUser(user);
        }
    }

    static class GetUserInfoByUserIDThread extends Thread {
        UserDao userDao;
        String userID;
        User user = null;
        public GetUserInfoByUserIDThread(UserDao userDao, String userID){
            this.userDao = userDao;
            this.userID = userID;
        }

        @Override
        public void run() {
            super.run();
            user = userDao.getUserByUserID(userID);
        }
    }



}
