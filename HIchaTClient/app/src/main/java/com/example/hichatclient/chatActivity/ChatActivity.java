package com.example.hichatclient.chatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.ChattingContent;
import com.example.hichatclient.service.ChatService;
import com.example.hichatclient.viewModel.ChatViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatActivity extends AppCompatActivity {
    // UI控件
    private Button buttonSend;
    private EditText editTextSendMsg;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;

    private List<ChattingContent> allMessage;
    private String friendID;
    private String userID;
    private String userShortToken;
    private ChatViewModel chatViewModel;

    // Service
    private ChatService chatService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        buttonSend = findViewById(R.id.buttonSend2);
        editTextSendMsg = findViewById(R.id.editTextSendContent);

        // 接收FriendAdapter传来的参数
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        friendID = bundle.getString("friendID");
        userID = bundle.getString("userID");
        userShortToken = bundle.getString("userShortToken");


        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        recyclerView = findViewById(R.id.recyclerViewChatContent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter();
        recyclerView.setAdapter(messageAdapter);


        initMessage();  // 假装是历史聊天记录，待实现
        messageAdapter.setAllMsg(allMessage);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editTextSendMsg.getText().toString();
                if(!"".equals(content)){
                    //如果字符串不为空，则创建ChattingContent对象
                    ChattingContent msg = new ChattingContent(userID, friendID, "send", "1", content);
                    if (chatViewModel.sendMessageToServer(msg, userShortToken)){
                        allMessage.add(msg);
                        messageAdapter.notifyItemInserted(allMessage.size()-1);  // 当有新消息是刷新RecyclerView中的显示
                        recyclerView.scrollToPosition(allMessage.size()-1);  // 将RecyclerView定位在最后一行
                        editTextSendMsg.setText("");  // 清空输入框的内容
                        editTextSendMsg.requestFocus();  // 输入光标回到输入框中
                    } else {
                        Toast.makeText(v.getContext().getApplicationContext(), "发送失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Bind to ChatService
        Intent bindIntent = new Intent(this, ChatService.class);
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ChatService.ChatBinder binder = (ChatService.ChatBinder) service;
                chatService = binder.getService();
//                chatService.isFriendMessage.observe(ChatActivity.this, new Observer<Integer>() {
//                    @Override
//                    public void onChanged(Integer integer) {
//                        if (integer == 1){
//                            List<ChattingContent> allNewChatMessages = chatService.getFriendMessagesByFriendID(friendID);
//                            chatViewModel.setAllNewChatMessages(allNewChatMessages);
//
//                            //allMessage.addAll(chattingContent);
//                            messageAdapter.notifyItemInserted(allMessage.size()-1);  // 当有新消息是刷新RecyclerView中的显示
//                            recyclerView.scrollToPosition(allMessage.size()-1);  // 将RecyclerView定位在最后一行
//                        }
//                    }
//                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);



    }

    public void initMessage(){
        allMessage = new ArrayList<>();
        ChattingContent msg1 = new ChattingContent("10048", "10001", "receive", "1", "hello!");
        ChattingContent msg2 = new ChattingContent("10048", "10001", "send", "1", "hi");
        ChattingContent msg3 = new ChattingContent("10048", "10001", "receive", "1", "Nice to meet u!");
        ChattingContent msg4 = new ChattingContent("10048", "10001", "send", "1", "me too!");
        ChattingContent msg5 = new ChattingContent("10048", "10001", "receive", "1", "hiahiahia!");
        allMessage.add(msg1);
        allMessage.add(msg2);
        allMessage.add(msg3);
        allMessage.add(msg4);
        allMessage.add(msg5);
    }





}