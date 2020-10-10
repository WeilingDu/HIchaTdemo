package com.example.hichatclient;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignUpViewModel extends ViewModel {
    private MutableLiveData<String> userName;
    private MutableLiveData<String> userPassword;
    private MutableLiveData<String> getUserPassword2;
    private MutableLiveData<String> userID;
}
