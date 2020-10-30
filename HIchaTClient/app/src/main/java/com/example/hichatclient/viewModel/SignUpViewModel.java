package com.example.hichatclient.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.hichatclient.dataResource.UserRepository;

import java.io.IOException;

public class SignUpViewModel extends AndroidViewModel {
    private UserRepository userRepository;


    public SignUpViewModel(@NonNull Application application){
        super(application);
        userRepository = new UserRepository(application);
    }

    public String signUp(String userName, String userPassword) throws InterruptedException {
        return userRepository.signUp(userName, userPassword);
    }
}
