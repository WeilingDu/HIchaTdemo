package com.example.hichatclient.data.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hichatclient.data.entity.MeToOthers;
import com.example.hichatclient.data.entity.OthersToMe;

import java.util.List;

@Dao
public interface OthersToMeDao {
    @Insert
    void insertOthersToMe(OthersToMe... othersToMes);

    @Delete
    void deleteOthersToMe(OthersToMe... othersToMes);

    @Update
    void updateOthersToMe(OthersToMe... othersToMes);

    @Query("SELECT * FROM OthersToMe WHERE user_id LIKE :userID ")
    List<OthersToMe> getAllOthersToMe(String userID);
}
