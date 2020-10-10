package com.example.hichatclient;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogInViewModel extends ViewModel {
    private MutableLiveData<String> userID;
    private MutableLiveData<String> userPassword;

    public MutableLiveData<String> getUserID() {
        return userID;
    }

    public MutableLiveData<String> getUserPassword() {
        return userPassword;
    }


}
