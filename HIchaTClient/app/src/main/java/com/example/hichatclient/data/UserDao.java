package com.example.hichatclient.data;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User... users);

}
