package com.example.hichatclient.newFriendsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hichatclient.ApplicationUtil;
import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.MeToOthers;
import com.example.hichatclient.viewModel.SearchFriendViewModel;

import java.io.IOException;
import java.net.Socket;

public class AddNewFriendActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private ApplicationUtil applicationUtil;
    private Socket socket;
    private SearchFriendViewModel searchFriendViewModel;


    private TextView textViewResultID;
    private TextView textViewResultName;
    private Button buttonAddFriend;

    private int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_friend);

        textViewResultID = findViewById(R.id.textViewSearchID5);
        textViewResultName = findViewById(R.id.textViewSearchName5);
        buttonAddFriend = findViewById(R.id.buttonSendAddFriend);

        searchFriendViewModel = new ViewModelProvider(this).get(SearchFriendViewModel.class);

        // 获取applicationUtil中的数据
        applicationUtil = (ApplicationUtil) AddNewFriendActivity.this.getApplication();
        socket = applicationUtil.getSocketStatic();
        final String userShortToken = applicationUtil.getUserShortToken();

        // 获取Share Preferences中的数据
        sharedPreferences = getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID", "fail");

        // 获取SearchFriendActivity传来的参数
        final String resultID = getIntent().getStringExtra("resultID");
        final String resultName = getIntent().getStringExtra("resultName");
        textViewResultID.setText(resultID);
        textViewResultName.setText(resultName);

        buttonAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                System.out.println("AddNewFriendActivity: click");
                AlertDialog.Builder builder= new AlertDialog.Builder(AddNewFriendActivity.this);
                builder.setTitle("您是否要向对方发送好友请求？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        flag = searchFriendViewModel.addFriend(resultID, userShortToken, socket);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            t.start();
                            t.join();
                            if (flag == 1){
                                Toast.makeText(v.getContext(), "好友请求已发送！", Toast.LENGTH_SHORT).show();
                                // 当用户发送好友请求时，更新数据库中的MeToOthers信息
                                MeToOthers meToOthers = new MeToOthers(userID, resultID, resultName, "null", "wait");
                                searchFriendViewModel.updateMeToOthersSend(meToOthers);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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