package com.example.hichatclient.dataResource;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.hichatclient.data.ChatDatabase;
import com.example.hichatclient.data.dao.UserDao;
import com.example.hichatclient.data.entity.User;

public class MeRepository {
    private UserDao userDao;

    public MeRepository(Context context) {
        ChatDatabase chatDatabase = ChatDatabase.getDatabase(context.getApplicationContext());
        userDao = chatDatabase.getUserDao();
    }

    public LiveData<User> getUserInfo(String userID){
        return userDao.getUserInfo(userID);
    }
}
