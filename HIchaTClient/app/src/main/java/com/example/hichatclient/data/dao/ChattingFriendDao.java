package com.example.hichatclient.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hichatclient.data.entity.ChattingContent;
import com.example.hichatclient.data.entity.ChattingFriend;

import java.util.List;

@Dao
public interface ChattingFriendDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChattingFriend(ChattingFriend... chattingFriends);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllChattingFriend(List<ChattingFriend> chattingFriends);

    @Query("SELECT * FROM chattingfriend WHERE userID LIKE :userID")
    LiveData<List<ChattingFriend>> findAllChattingFriend(String userID);
}
