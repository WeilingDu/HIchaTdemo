package com.example.hichatclient.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hichatclient.data.entity.User;
import com.example.hichatclient.dataResource.MeRepository;
import com.example.hichatclient.dataResource.UserRepository;

import java.io.IOException;
import java.net.Socket;

public class ChangeProfileViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private MeRepository meRepository;

    public ChangeProfileViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }


    public int updateUserProfileToServer(String userShortToken, byte[] userNewProfile, Socket socket) throws IOException {
        return userRepository.updateUserProfileToServer(userShortToken, userNewProfile, socket);
    }

    public LiveData<User> getUserInfo(String userID){
        return meRepository.getUserInfo(userID);
    }

    public void insertUser(User user) throws InterruptedException {
        userRepository.insertUser(user);
    }

    public User getUserInfoByUserID(String userID) throws InterruptedException {
        return meRepository.getUserInfoByUserID(userID);
    }
}
