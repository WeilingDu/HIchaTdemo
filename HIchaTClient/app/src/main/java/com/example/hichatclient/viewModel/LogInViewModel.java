package com.example.hichatclient.viewModel;


import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.hichatclient.data.entity.User;
import com.example.hichatclient.dataResource.FriendsRepository;
import com.example.hichatclient.dataResource.UserRepository;

import java.io.IOException;
import java.net.Socket;


public class LogInViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private FriendsRepository friendsRepository;


    public LogInViewModel(@NonNull Application application){
        super(application);
        userRepository = new UserRepository(application);
        friendsRepository = new FriendsRepository(application);
    }



    public User sendIDAndPassword(String userID, String userPassword, Socket socket) throws InterruptedException {
        return userRepository.sendIDAndLogIn(userID, userPassword, socket);
    }

    public void insertUser(User user) throws InterruptedException {
        userRepository.insertUser(user);
    }

    public void getUserFriendsFromServer(String userID, String userShortToken, Socket socket) throws IOException {
        friendsRepository.getUserFriendsFromServer(userID, userShortToken, socket);
    }

    // 用于本地测试
    public User sendIDAndPasswordTest(String userID, String userPassword) {
        return userRepository.sendIDAndLogInTest(userID, userPassword);
    }

}
