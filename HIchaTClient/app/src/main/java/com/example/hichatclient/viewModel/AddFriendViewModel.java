package com.example.hichatclient.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.hichatclient.data.entity.Friend;
import com.example.hichatclient.data.entity.SearchResult;
import com.example.hichatclient.dataResource.AddFriendRepository;

import java.util.List;

public class AddFriendViewModel extends AndroidViewModel {
    private AddFriendRepository addFriendRepository;

    public AddFriendViewModel(@NonNull Application application) {
        super(application);
        this.addFriendRepository = new AddFriendRepository();
    }

    public List<SearchResult> searchPeopleFromID (String personID){
        return addFriendRepository.searchPeopleFromID(personID);
    }
}
