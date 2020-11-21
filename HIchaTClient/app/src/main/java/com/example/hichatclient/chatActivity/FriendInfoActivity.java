package com.example.hichatclient.chatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hichatclient.ApplicationUtil;
import com.example.hichatclient.R;
import com.example.hichatclient.baseActivity.BaseActivity;
import com.example.hichatclient.data.entity.Friend;
import com.example.hichatclient.newFriendsActivity.AddNewFriendActivity;
import com.example.hichatclient.viewModel.FriendInfoViewModel;

import java.io.IOException;
import java.net.Socket;

public class FriendInfoActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private ApplicationUtil applicationUtil;
    private FriendInfoViewModel friendInfoViewModel;
    private String userShortToken;
    private Socket socket;
    private Friend friend;



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
        userShortToken = applicationUtil.getUserShortToken();

        // 获取Share Preferences中的数据
        sharedPreferences = getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID", "fail");

        // 获取ContactsFragment传来的参数
        final String friendID = getIntent().getStringExtra("friendID");
        System.out.println("FriendInfoActivity friendID: " + friendID);

        textViewFriendID.setText(friendID);

        friendInfoViewModel.getFriendInfo(userID, friendID).observe(this, new Observer<Friend>() {
            @Override
            public void onChanged(Friend friend) {
                System.out.println("FriendInfoActivity: " + friend.getFriendName());
                textViewFriendName.setText(friend.getFriendName());
            }
        });

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
            public void onClick(final View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(FriendInfoActivity.this);
                builder.setTitle("删除该联系人？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // 向服务器发送删好友请求
                                    friendInfoViewModel.deleteFriendToServer(friendID, userShortToken, socket);

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
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 从本地数据库中删掉该好友
                                friendInfoViewModel.deleteFriendInSQL(userID, friendID);
                            }
                        });
                        thread.start();
                        try {
                            t.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent();
                        intent.setClass(v.getContext(), BaseActivity.class);
                        intent.putExtra("isLogIn", "-1");
                        intent.putExtra("FragmentId", "1");
                        startActivity(intent);

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();

            }
        });


    }
}