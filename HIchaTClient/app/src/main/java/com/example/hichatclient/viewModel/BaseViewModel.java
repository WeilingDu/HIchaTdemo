package com.example.hichatclient.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hichatclient.dataResource.FriendsRepository;
import com.example.hichatclient.dataResource.UserRepository;

import java.util.List;


public class BaseViewModel extends AndroidViewModel {
    private FriendsRepository friendsRepository;
    private UserRepository userRepository;
    private String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public List<String> getTokens(String userID){
        return userRepository.getUserTokens(userID);
    }

    public BaseViewModel(@NonNull Application application) {
        super(application);
        friendsRepository = new FriendsRepository(application);
        userRepository = new UserRepository(application);
    }


    public void getUserFriendsFromServer(String userID, String userShortToken, String userLongToken){
        friendsRepository.getUserFriendsFromServer(userID, userShortToken, userLongToken);
    }
}
