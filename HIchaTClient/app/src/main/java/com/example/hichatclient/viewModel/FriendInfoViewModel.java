package com.example.hichatclient.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hichatclient.data.dao.FriendDao;
import com.example.hichatclient.data.entity.Friend;
import com.example.hichatclient.dataResource.FriendsRepository;

import java.net.Socket;

public class FriendInfoViewModel extends AndroidViewModel {
    private FriendsRepository friendsRepository;

    public FriendInfoViewModel(@NonNull Application application) {
        super(application);
        friendsRepository = new FriendsRepository(application);
    }

    public LiveData<Friend> getFriendInfo(String userID, String friendID){
        return friendsRepository.getFriendInfo(userID, friendID);
    }

    public void deleteFriendToServer(String userShortToken, String friendID, Socket socket){
        friendsRepository.deleteFriendToServer(userShortToken, friendID, socket);
    }


}
