package com.example.hichatclient.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hichatclient.data.entity.ChattingContent;

import java.util.List;

@Dao
public interface ChattingContentDao {
    @Insert
    void insertContent(ChattingContent... chattingContents);

    @Insert
    void insertAllContent(List<ChattingContent> chattingContents);


    @Query("SELECT * FROM chattingcontent WHERE user_id LIKE :userID AND friend_id LIKE :friendID")
    LiveData<List<ChattingContent>> findAllContent(String userID, String friendID);



}
