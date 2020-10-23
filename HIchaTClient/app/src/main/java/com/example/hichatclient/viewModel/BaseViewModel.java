package com.example.hichatclient.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.hichatclient.dataResource.FriendsRepository;



public class BaseViewModel extends AndroidViewModel {
    private FriendsRepository friendsRepository;
    private String userID;
    private String userName;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    private String userToken;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        friendsRepository = new FriendsRepository(application);
    }


    public void getUserFriendsFromServer(String userID, String userToken){
        friendsRepository.getUserFriendsFromServer(userToken, userID);
    }
}
