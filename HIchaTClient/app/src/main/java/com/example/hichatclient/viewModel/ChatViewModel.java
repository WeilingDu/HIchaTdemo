package com.example.hichatclient.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hichatclient.data.entity.ChattingContent;
import com.example.hichatclient.dataResource.MessageRepository;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private MessageRepository messageRepository;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        messageRepository = new MessageRepository(application);
    }

    public boolean sendMessageToServer(ChattingContent chattingContent, String userShortToken, Socket socket){
        return messageRepository.sendMessageToServer(chattingContent, userShortToken, socket);
    }

    public LiveData<List<ChattingContent>> getAllMessageLive(String userID, String friendID){
        return messageRepository.getChattingContentFromSQL(userID, friendID);
    }


    public void insertOneMessageIntoSQL(ChattingContent chattingContent){
        messageRepository.insertOneMessageIntoSQL(chattingContent);
    }
}
