package com.example.hichatclient.dataResource;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.hichatclient.data.ChatDatabase;
import com.example.hichatclient.data.entity.User;
import com.example.hichatclient.data.dao.UserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepository {
    private UserDao userDao;

    public UserRepository(Context context) {
        ChatDatabase chatDatabase = ChatDatabase.getDatabase(context.getApplicationContext());
        userDao = chatDatabase.getUserDao();
    }

    public User sendIDAndLogIn(String userID, String userPassword){
        int isLogIn = 1;

        if (isLogIn == 1){
            User user = new User(userID, userPassword, "jane", "111", "123", "123");
            return user;
        } else {
            return null;
        }
    }

    public String signUp(String userName, String userPassword){
        String userID = "666";
        return userID;
    }

    // 从数据库获取用户Token
    public List<String> getUserTokens(String userID){
        List<String> tokens= new ArrayList<>();
        String shortToken = Objects.requireNonNull(userDao.getUserTokens(userID).getValue()).get(0).getUserShortToken();
        String longToken = Objects.requireNonNull(userDao.getUserTokens(userID).getValue()).get(1).getUserLongToken();
        tokens.add(shortToken);
        tokens.add(longToken);
        return tokens;
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
        }
    }

}
