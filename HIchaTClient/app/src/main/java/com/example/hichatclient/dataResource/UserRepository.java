package com.example.hichatclient.dataResource;

import android.content.Context;

import com.example.hichatclient.data.ChatDatabase;
import com.example.hichatclient.data.entity.User;
import com.example.hichatclient.data.dao.UserDao;

public class UserRepository {
    private UserDao userDao;

    public UserRepository(Context context) {
        ChatDatabase chatDatabase = ChatDatabase.getDatabase(context.getApplicationContext());
        userDao = chatDatabase.getUserDao();
    }

    public User sendIDAndLogIn(String userID, String userPassword){
        int isLogIn = 1;

        if (isLogIn == 1){
            User user = new User(userID, userPassword, "jane", "111", "123");
            return user;
        } else {
            return null;
        }
    }

    public String signUp(String userName, String userPassword){
        String userID = "666";
        return userID;
    }



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
