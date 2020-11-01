package com.example.hichatclient.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hichatclient.data.entity.MeToOthers;
import com.example.hichatclient.data.entity.OthersToMe;
import com.example.hichatclient.dataResource.NewFriendsRepository;

import java.util.List;

public class NewFriendsViewModel extends AndroidViewModel {
    private NewFriendsRepository newFriendsRepository;


    public NewFriendsViewModel(@NonNull Application application) {
        super(application);
        newFriendsRepository = new NewFriendsRepository(application);
    }


    public LiveData<List<MeToOthers>> getAllMeToOthersFromSQL(String userID){
        return newFriendsRepository.getAllMeToOthersFromSQL(userID);
    }

    public LiveData<List<OthersToMe>> getAllOthersToMeFromSQL(String userID){
        return newFriendsRepository.getAllOthersToMeFromSQL(userID);
    }

    public void updateMeToOthers(List<MeToOthers> meToOthers){
        newFriendsRepository.updateMeToOthers(meToOthers);
    }

    public void updateOthersToMe(List<OthersToMe> othersToMes){
        newFriendsRepository.updateOthersToMe(othersToMes);
    }

}
