package com.example.hichatclient.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.hichatclient.data.entity.User;
import com.example.hichatclient.dataResource.MeRepository;

public class ChangeNameViewModel extends AndroidViewModel {
    private MeRepository meRepository;


    public ChangeNameViewModel(@NonNull Application application) {
        super(application);
        meRepository = new MeRepository(application);
    }

    public int updateUserNameToServer(String shortToken, String userNewName){
        return meRepository.updateUserNameToServer(shortToken, userNewName);
    }

    public void updateUserInfoInSQL(User user){
        meRepository.updateUserInfoInSQL(user);
    }

    public User getUserInfoByUserID(String userID) throws InterruptedException {
        return meRepository.getUserInfoByUserID(userID);
    }
}