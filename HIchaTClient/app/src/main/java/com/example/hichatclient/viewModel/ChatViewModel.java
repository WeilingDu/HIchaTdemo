package com.example.hichatclient.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hichatclient.data.entity.ChattingContent;
import com.example.hichatclient.dataResource.MessageRepository;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private MessageRepository messageRepository;

    public List<ChattingContent> getAllNewChatMessages() {
        return allNewChatMessages;
    }

    public void setAllNewChatMessages(List<ChattingContent> allNewChatMessages) {
        this.allNewChatMessages = allNewChatMessages;
    }

    private List<ChattingContent> allNewChatMessages = new ArrayList<>();

    public ChatViewModel(@NonNull Application application) {
        super(application);
        messageRepository = new MessageRepository(application);
    }

    public LiveData<List<ChattingContent>> getChattingContent(String userID, String friendID){
        return messageRepository.getChattingContentFromSQL(userID, friendID);
    }


    public boolean sendMessageToServer(ChattingContent chattingContent,  String userShortToken){
        return messageRepository.sendMessageToServer(chattingContent, userShortToken);
    }

}
