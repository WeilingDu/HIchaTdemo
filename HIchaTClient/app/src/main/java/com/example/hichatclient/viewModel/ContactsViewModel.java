package com.example.hichatclient.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hichatclient.data.entity.Friend;
import com.example.hichatclient.dataResource.FriendsRepository;

import java.util.List;

public class ContactsViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private FriendsRepository friendsRepository;


    public ContactsViewModel(@NonNull Application application) {
        super(application);
        friendsRepository = new FriendsRepository(application);
    }

    public LiveData<List<Friend>> getUserFriendsFromSQL(String userID) {
        return friendsRepository.getUserFriendsFromSQL(userID);
    }

    public LiveData<List<Friend>> findFriendsWithPatten(String patten){
        return friendsRepository.findFriendsWithPatten(patten);
    }

}