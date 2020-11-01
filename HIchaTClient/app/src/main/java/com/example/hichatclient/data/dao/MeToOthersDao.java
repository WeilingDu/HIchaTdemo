package com.example.hichatclient.data.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hichatclient.data.entity.MeToOthers;
import com.example.hichatclient.data.entity.User;

import java.util.List;

@Dao
public interface MeToOthersDao {
    @Insert
    void insertMeToOthers(MeToOthers... meToOthers);

    @Delete
    void deleteMeToOthers(MeToOthers... meToOthers);

    @Update
    void updateMeToOthers(MeToOthers... meToOthers);

    @Query("SELECT * FROM MeToOthers WHERE user_id LIKE :userID")
    List<MeToOthers> getAllMeToOthers(String userID);
}
