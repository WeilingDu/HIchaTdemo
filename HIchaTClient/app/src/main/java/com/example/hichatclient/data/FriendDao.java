package com.example.hichatclient.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FriendDao {
    @Insert
    void insertFriend(Friend... friends);

    @Delete
    void deleteFriend(Friend... friends);

    @Update
    void updateFriend(Friend... friends);

    @Query("SELECT * FROM Friend Where user_id LIKE :userID")
    LiveData<List<Friend>> getAllUserFriend(String userID);

}
