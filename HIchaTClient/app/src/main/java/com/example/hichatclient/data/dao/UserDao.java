package com.example.hichatclient.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.hichatclient.data.entity.User;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User... users);

}
