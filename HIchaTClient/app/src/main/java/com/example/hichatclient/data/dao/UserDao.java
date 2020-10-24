package com.example.hichatclient.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hichatclient.data.entity.Friend;
import com.example.hichatclient.data.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User... users);

    @Delete
    void deleteFriend(Friend... friends);

    @Query("SELECT * FROM User Where user_id LIKE :userID")
    LiveData<List<User>> getUserTokens(String userID);

    @Query("SELECT * FROM User Where user_id LIKE :userID")
    LiveData<User> getUserInfo(String userID);

}
