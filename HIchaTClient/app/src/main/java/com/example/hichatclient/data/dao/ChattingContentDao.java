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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContent(ChattingContent... chattingContents);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllContent(List<ChattingContent> chattingContents);


    @Query("SELECT * FROM chattingcontent WHERE userID LIKE :userID AND friendID LIKE :friendID")
    LiveData<List<ChattingContent>> findAllContent(String userID, String friendID);



}
