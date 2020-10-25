package com.example.hichatclient.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.hichatclient.data.entity.User;
import com.example.hichatclient.dataResource.MeRepository;
import com.example.hichatclient.dataResource.UserRepository;

import java.util.List;

public class MeViewModel extends AndroidViewModel {
    private String userID;
    private MeRepository meRepository;

    public MeViewModel(@NonNull Application application) {
        super(application);
        meRepository = new MeRepository(application);
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public LiveData<List<User>> getUserInfo(String userID){
        return meRepository.getUserInfo(userID);
    }


}
