package com.example.hichatclient.chatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hichatclient.ApplicationUtil;
import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.Friend;
import com.example.hichatclient.newFriendsActivity.AddNewFriendActivity;
import com.example.hichatclient.viewModel.FriendInfoViewModel;

import java.net.Socket;

public class FriendInfoActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private ApplicationUtil applicationUtil;
    private FriendInfoViewModel friendInfoViewModel;
    private Socket socket;



    // UI控件
    TextView textViewFriendID;
    TextView textViewFriendName;
    Button buttonSendMessage;
    Button buttonDeleteFriend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

        textViewFriendID = findViewById(R.id.textViewFriendID9);
        textViewFriendName = findViewById(R.id.textViewFriendName9);
        buttonSendMessage = findViewById(R.id.buttonSendMessageTo);
        buttonDeleteFriend = findViewById(R.id.buttonDeleteFriend);

        friendInfoViewModel = new ViewModelProvider(this).get(FriendInfoViewModel.class);

        // 获取applicationUtil中的数据
        applicationUtil = (ApplicationUtil) FriendInfoActivity.this.getApplication();
        socket = applicationUtil.getSocketStatic();
        final String userShortToken = applicationUtil.getUserShortToken();

        // 获取Share Preferences中的数据
        sharedPreferences = getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID", "fail");

        // 获取ContactsFragment传来的参数
        final String friendID = getIntent().getStringExtra("friendID");
        System.out.println("FriendInfoActivity friendID: " + friendID);

        Friend friend;
        friend = friendInfoViewModel.getFriendInfo(userID, friendID).getValue();
        textViewFriendID.setText(friendID);
        textViewFriendName.setText(friend.getFriendName());

        // 点击发信息，跳转和好友的聊天界面
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("friendID", friendID);
                startActivity(intent);
            }
        });

        buttonDeleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}