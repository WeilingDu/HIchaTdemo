package com.example.hichatclient.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.hichatclient.data.entity.OthersToMe;
import com.example.hichatclient.dataResource.NewFriendsRepository;

public class OthersRequestViewModel extends AndroidViewModel {
    NewFriendsRepository newFriendsRepository;

    public OthersRequestViewModel(@NonNull Application application) {
        super(application);
        newFriendsRepository = new NewFriendsRepository(application);
    }

    public OthersToMe getOthersToMeByObjectID(String userID, String objectID) throws InterruptedException {
        return newFriendsRepository.getOthersToMeByObjectID(userID, objectID);
    }

    public void othersToMeResponseToServer(String userShortToken, String objectID, boolean refuse){
        newFriendsRepository.othersToMeResponseToServer(userShortToken, objectID, refuse);
    }

    public void updateOthersToMeResponse(OthersToMe othersToMe){
        newFriendsRepository.updateOthersToMeResponse(othersToMe);
    }
}
