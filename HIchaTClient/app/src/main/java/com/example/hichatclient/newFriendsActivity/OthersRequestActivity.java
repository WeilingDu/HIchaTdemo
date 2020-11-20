package com.example.hichatclient.newFriendsActivity;

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
import com.example.hichatclient.baseActivity.BaseActivity;
import com.example.hichatclient.data.entity.Friend;
import com.example.hichatclient.data.entity.OthersToMe;
import com.example.hichatclient.viewModel.OthersRequestViewModel;

import java.io.IOException;
import java.net.Socket;

public class OthersRequestActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private OthersRequestViewModel othersRequestViewModel;
    private ApplicationUtil applicationUtil;
    private Socket socket;

    private OthersToMe othersToMe;
    private String objectID;

    // UI控件
    private TextView textViewObjectID;
    private TextView textViewObjectName;
    private Button buttonRefuse;
    private Button buttonAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_request);

        applicationUtil = (ApplicationUtil) OthersRequestActivity.this.getApplication();
        othersRequestViewModel = new ViewModelProvider(this).get(OthersRequestViewModel.class);

        textViewObjectID = findViewById(R.id.textViewObjectID3);
        textViewObjectName = findViewById(R.id.textViewObjectName3);
        buttonAgree = findViewById(R.id.buttonAgree);
        buttonRefuse = findViewById(R.id.buttonRefuse);

        // 获取Share Preferences中的数据
        sharedPreferences = getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID", "fail");

        // 获取applicationUtil中的数据
        final String userShortToken = applicationUtil.getUserShortToken();
        socket = applicationUtil.getSocketStatic();

        // 获取NewFriendActivity传来的参数
        objectID = getIntent().getStringExtra("objectID");

        try {
            othersToMe = othersRequestViewModel.getOthersToMeByObjectID(userID, objectID);  // 通过objectID获取OtherToMe的具体信息
            textViewObjectID.setText(othersToMe.getObjectID());
            textViewObjectName.setText(othersToMe.getObjectName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 当用户拒绝别人的好友请求时
        buttonRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    othersRequestViewModel.othersToMeResponseToServer(userShortToken, objectID, true, socket);  // 告诉服务器用户的回应
                } catch (IOException e) {
                    e.printStackTrace();
                }
                othersToMe.setUserResponse("refuse");
                othersRequestViewModel.updateOthersToMeResponse(othersToMe);  // 更新数据库中OthersToMe的信息

                Intent intent = new Intent();
                intent.setClass(v.getContext(), NewFriendsActivity.class);
                startActivity(intent);
            }
        });

        // 当用户同意别人的好友请求时
        buttonAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("OthersRequestActivity userShortToken: " + userShortToken);
                            othersRequestViewModel.othersToMeResponseToServer(userShortToken, objectID, false, socket);  // 告诉服务器用户的回应
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
                othersToMe.setUserResponse("agree");
                System.out.println("othersRequestActivity: " + othersToMe.getUserResponse());
                othersRequestViewModel.updateOthersToMeResponse(othersToMe);  // 更新数据库中的OthersToMe信息
                Friend friend = new Friend(userID, othersToMe.getObjectID(), othersToMe.getObjectName(), othersToMe.getObjectProfile(), "null", "null", "New Friend", true);
                othersRequestViewModel.insertNewFriendIntoSQL(friend);  // 更新数据库中的Friend信息

                Intent intent = new Intent();
                intent.setClass(v.getContext(), NewFriendsActivity.class);
                startActivity(intent);

            }
        });

    }
}