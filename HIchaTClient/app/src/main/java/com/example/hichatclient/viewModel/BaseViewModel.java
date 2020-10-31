package com.example.hichatclient.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hichatclient.dataResource.FriendsRepository;
import com.example.hichatclient.dataResource.UserRepository;

import java.io.IOException;
import java.net.Socket;
import java.util.List;


public class BaseViewModel extends AndroidViewModel {
    private FriendsRepository friendsRepository;
    private String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }




    public BaseViewModel(@NonNull Application application) {
        super(application);
        friendsRepository = new FriendsRepository(application);
    }


    public void getUserFriendsFromServer(String userID, String userShortToken, Socket socket) throws IOException {
        friendsRepository.getUserFriendsFromServer(userID, userShortToken, socket);
    }
}
