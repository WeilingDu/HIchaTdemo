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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    private List<ChattingContent> allMessage = new ArrayList<>();
    private Friend friend;
    private User user;
    private boolean flag;
    private String userShortToken;
    private String friendID;

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
        userShortToken = applicationUtil.getUserShortToken();



        // 获取Share Preferences中的数据
        sharedPreferences = getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID", "fail");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("deleteFlag", "false");
        editor.apply();

        final SimpleDateFormat newSimpleDateFormat = new SimpleDateFormat(
                "yyyy年MM月dd日HH时mm分", Locale.getDefault());

        // 接收FriendInfoActivity传来的参数
        friendID = getIntent().getStringExtra("friendID");

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        recyclerView = findViewById(R.id.recyclerViewChatContent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter();
        recyclerView.setAdapter(messageAdapter);

        friend = chatViewModel.getFriendInfo(userID, friendID).getValue();
        user = chatViewModel.getUserInfoByUserID(userID).getValue();




        allMessage = chatViewModel.getAllMessageLive(userID, friendID).getValue();
        if (allMessage != null){
            System.out.println("ChatActivity: there are messages!!!");
            messageAdapter.setAllMsg(allMessage);
        }


        // 当数据库中的聊天记录有变化时
        chatViewModel.getAllMessageLive(userID, friendID).observe(this, new Observer<List<ChattingContent>>() {
            @Override
            public void onChanged(List<ChattingContent> chattingContents) {
                if (chattingContents.size() > 0){
                    ChattingContent msg = chattingContents.get(chattingContents.size() - 1);
                    // 更新数据库中的ChattingFriend信息
//                    assert userID != null;
//                    ChattingFriend chattingFriend = new ChattingFriend(userID, friend.getFriendID(), friend.getFriendName(), friend.getFriendProfile(), msg.getMsgContent(), msg.getMsgTime());
//                    chatViewModel.updateChattingFriendIntoSQL(chattingFriend);

                    messageAdapter.setAllMsg(chattingContents);
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(chattingContents.size()-1);  // 将RecyclerView定位在最后一行

                    if (msg.getMsgType().equals("receive")){
                        final long time = System.currentTimeMillis();
                        Thread t2 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 当用户打开与某个好友的对话框时，向服务器发送已读提示
                                try {
                                    System.out.println("call this function: sendReadMsgToServer2");
                                    chatViewModel.sendReadMsgToServer(userShortToken, friendID, time, socket);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        t2.start();
                    }

                }
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editTextSendMsg.getText().toString();
                if(!"".equals(content)){
                    //如果字符串不为空，则创建ChattingContent对象
                    final ChattingContent msg = new ChattingContent(userID, friendID, "send", System.currentTimeMillis(), content, false);
                    System.out.println("ChatActivity time: " + msg.getMsgTime());
                    String LogTime = newSimpleDateFormat.format(msg.getMsgTime());
                    System.out.println("ChatActivity format time: " + LogTime);
                    System.out.println("ChatActivity content: " + content);
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println("ChatActivity call this function");
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
                        chatViewModel.insertOneMessageIntoSQL(msg); // 将该消息插入数据库中
                        editTextSendMsg.setText("");  // 清空输入框的内容
                        editTextSendMsg.requestFocus();  // 输入光标回到输入框中
                    } else {
                        Toast.makeText(v.getContext().getApplicationContext(), "发送失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        final long time = System.currentTimeMillis();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // 当用户打开与某个好友的对话框时，向服务器发送已读提示
                try {
                    System.out.println("call this function: sendReadMsgToServer1");
                    chatViewModel.sendReadMsgToServer(userShortToken, friendID, time, socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();

    }
    //    public void initMessage(){
//        allMessage = new ArrayList<>();
//        ChattingContent msg1 = new ChattingContent("10048", "10001", "receive", "1", "hello!");
//        ChattingContent msg2 = new ChattingContent("10048", "10001", "send", "1", "hi");
//        ChattingContent msg3 = new ChattingContent("10048", "10001", "receive", "1", "Nice to meet u!");
//        ChattingContent msg4 = new ChattingContent("10048", "10001", "send", "1", "me too!");
//        ChattingContent msg5 = new ChattingContent("10048", "10001", "receive", "1", "hiahiahia!");
//        allMessage.add(msg1);
//        allMessage.add(msg2);
//        allMessage.add(msg3);
//        allMessage.add(msg4);
//        allMessage.add(msg5);
//    }





}