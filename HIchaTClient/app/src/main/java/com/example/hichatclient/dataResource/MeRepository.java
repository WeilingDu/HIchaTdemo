package com.example.hichatclient.dataResource;

import android.content.Context;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import com.example.hichatclient.data.ChatDatabase;
import com.example.hichatclient.data.dao.UserDao;
import com.example.hichatclient.data.entity.User;

import java.util.List;

public class MeRepository {
    private UserDao userDao;

    public MeRepository(Context context) {
        ChatDatabase chatDatabase = ChatDatabase.getDatabase(context.getApplicationContext());
        userDao = chatDatabase.getUserDao();
    }

    public LiveData<List<User>> getUserInfo(String userID){
        return userDao.getUserInfo(userID);
    }

    // 向服务器发送更新用户昵称的请求
    public int updateUserNameToServer(String userShortToken, String userNewName){
        int flag = 1;

        return flag;
    }

    // 向服务器发送更新用户密码的请求
    public int updateUserPasswordToServer(String userShortToken, String userNewPassword){
        int flag = 1;
        return flag;
    }

    // 更新数据库中的用户信息
    public void updateUserInfoInSQL(User user){
        new UpdateUserInfoSQLThread(userDao, user).start();
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

    //通过用户ID获取数据库中的的用户信息
    public User getUserInfoByUserID(String userID) throws InterruptedException {
        GetUserInfoByUserIDThread getUserInfoByUserIDThread = new GetUserInfoByUserIDThread(userDao, userID);
        getUserInfoByUserIDThread.start();
        getUserInfoByUserIDThread.join();
        return getUserInfoByUserIDThread.user;
    }

    static class GetUserInfoByUserIDThread extends Thread {
        UserDao userDao;
        String userID;
        User user;
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
