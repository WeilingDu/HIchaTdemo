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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hichatclient.ApplicationUtil;
import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.ChattingContent;
import com.example.hichatclient.data.entity.ChattingFriend;
import com.example.hichatclient.data.entity.Friend;
import com.example.hichatclient.data.entity.User;
import com.example.hichatclient.newFriendsActivity.AddNewFriendActivity;
import com.example.hichatclient.service.ChatService;
import com.example.hichatclient.viewModel.ChatViewModel;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private ApplicationUtil applicationUtil;
    private Socket socket;
    private ChatViewModel chatViewModel;

    // UI控件
    private Button buttonSend;
    private EditText editTextSendMsg;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;

    // 用户和某好友的聊天信息
    private List<ChattingContent> allMessage;
    private Friend friend;
    private User user;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        buttonSend = findViewById(R.id.buttonSend2);
        editTextSendMsg = findViewById(R.id.editTextSendContent);

        // 获取applicationUtil中的数据
        applicationUtil = (ApplicationUtil) ChatActivity.this.getApplication();
        if (!applicationUtil.staticIsConnected()) {
            try {
                applicationUtil.initSocketStatic();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket = applicationUtil.getSocketStatic();
        final String userShortToken = applicationUtil.getUserShortToken();

        // 获取Share Preferences中的数据
        sharedPreferences = getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID", "fail");

        // 接收FriendInfoActivity传来的参数
        final String friendID = getIntent().getStringExtra("friendID");

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        recyclerView = findViewById(R.id.recyclerViewChatContent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter();
        recyclerView.setAdapter(messageAdapter);

        friend = chatViewModel.getFriendInfo(userID, friendID).getValue();
        user = chatViewModel.getUserInfoByUserID(userID).getValue();


        // 刚点开与好友的聊天框时
        allMessage = chatViewModel.getAllMessageLive(userID, friendID).getValue();
        messageAdapter.setAllMsg(allMessage);
        recyclerView.scrollToPosition(allMessage.size()-1);  // 将RecyclerView定位在最后一行

        // 当数据库中的聊天记录有变化时
        chatViewModel.getAllMessageLive(userID, friendID).observe(this, new Observer<List<ChattingContent>>() {
            @Override
            public void onChanged(List<ChattingContent> chattingContents) {
                ChattingContent msg = chattingContents.get(chattingContents.size() - 1);
                // 更新数据库中的ChattingFriend信息
                ChattingFriend chattingFriend = new ChattingFriend(user.getUserID(), friend.getFriendID(), friend.getFriendName(), friend.getFriendProfile(), msg.getMsgContent(), msg.getMsgTime());
                chatViewModel.updateChattingFriendIntoSQL(chattingFriend);
                // 当收到好友的信息时
                if (msg.getMsgType().equals("receive")){
                    allMessage.add(msg);
                    messageAdapter.notifyItemInserted(allMessage.size()-1);  // 当有新消息是刷新RecyclerView中的显示
                    recyclerView.scrollToPosition(allMessage.size()-1);  // 将RecyclerView定位在最后一行
                }

            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editTextSendMsg.getText().toString();
                if(!"".equals(content)){
                    //如果字符串不为空，则创建ChattingContent对象
                    final ChattingContent msg = new ChattingContent(userID, friendID, "send", "1", content);
                    flag = false;
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                flag = chatViewModel.sendMessageToServer(msg, userShortToken, socket);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (flag){
                        allMessage.add(msg);
                        chatViewModel.insertOneMessageIntoSQL(msg); // 将该消息插入数据库中
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageAdapter.notifyItemInserted(allMessage.size()-1);  // 当有新消息是刷新RecyclerView中的显示
                                recyclerView.scrollToPosition(allMessage.size()-1);  // 将RecyclerView定位在最后一行
                                editTextSendMsg.setText("");  // 清空输入框的内容
                                editTextSendMsg.requestFocus();  // 输入光标回到输入框中
                            }
                        });

                    } else {
                        Toast.makeText(v.getContext().getApplicationContext(), "发送失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


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