package com.example.hichatclient.dataResource;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.hichatclient.data.ChatDatabase;
import com.example.hichatclient.data.dao.MeToOthersDao;
import com.example.hichatclient.data.dao.OthersToMeDao;
import com.example.hichatclient.data.entity.MeToOthers;
import com.example.hichatclient.data.entity.OthersToMe;

import java.util.List;

public class NewFriendsRepository {
    private MeToOthersDao meToOthersDao;
    private OthersToMeDao othersToMeDao;

    public NewFriendsRepository(Context context){
        ChatDatabase chatDatabase = ChatDatabase.getDatabase(context.getApplicationContext());
        meToOthersDao = chatDatabase.getMeToOthersDao();
        othersToMeDao = chatDatabase.getOthersToMeDao();
    }

    public LiveData<List<MeToOthers>> getAllMeToOthersFromSQL(String userID){
        return meToOthersDao.getAllMeToOthers(userID);
    }

    public LiveData<List<OthersToMe>> getAllOthersToMeFromSQL(String userID){
        return othersToMeDao.getAllOthersToMe(userID);
    }

    public void updateMeToOthers(List<MeToOthers> meToOthers){
        meToOthersDao.insertAllMeToOthers(meToOthers);
    }

    public void updateOthersToMe(List<OthersToMe> othersToMes){
        othersToMeDao.insertAllOthersToMe(othersToMes);
    }

}
