package com.example.hichatclient.viewModel;


import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.hichatclient.data.User;
import com.example.hichatclient.dataResource.UserRepository;


public class LogInViewModel extends AndroidViewModel {
    private UserRepository userRepository;


    public LogInViewModel(@NonNull Application application){
        super(application);
        userRepository = new UserRepository(application);
    }


    public User sendIDAndPassword(String userID, String userPassword){
        return userRepository.sendIDAndLogIn(userID, userPassword);
    }

    public void insertUser(User user){
        userRepository.insertUser(user);
    }


}
