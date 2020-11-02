package com.example.hichatclient.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.hichatclient.data.entity.MeToOthers;
import com.example.hichatclient.data.entity.SearchResult;
import com.example.hichatclient.dataResource.NewFriendsRepository;

import java.io.IOException;
import java.net.Socket;

public class SearchFriendViewModel extends AndroidViewModel {
    NewFriendsRepository newFriendsRepository;

    public SearchFriendViewModel(@NonNull Application application) {
        super(application);
        newFriendsRepository = new NewFriendsRepository(application);
    }

    public SearchResult searchPeopleFromID (String personID, String userShortToken, Socket socket) throws IOException {
        return newFriendsRepository.searchPeopleFromID(personID, userShortToken, socket);
    }

    public int addFriend (String personID, String userShortToken, Socket socket) throws IOException {
        return newFriendsRepository.addFriend(personID, userShortToken, socket);
    }

    public void updateMeToOthersSend(MeToOthers meToOthers){
        newFriendsRepository.updateMeToOthersSend(meToOthers);
    }


}
