package com.example.hichatclient.newFriendsActivity;

import androidx.appcompat.app.AppCompatActivity;
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

import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.MeToOthers;
import com.example.hichatclient.data.entity.OthersToMe;
import com.example.hichatclient.service.ChatService;
import com.example.hichatclient.viewModel.NewFriendsViewModel;

import java.util.List;

public class NewFriendsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMeToOthers;
    private RecyclerView recyclerViewOtherToMe;
    private MeToOthersAdapter meToOthersAdapter;
    private OthersToMeAdapter othersToMeAdapter;
    private NewFriendsViewModel newFriendsViewModel;
    private SharedPreferences sharedPreferences;
    // Service
    private ChatService chatService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends);

        newFriendsViewModel = new ViewModelProvider(this).get(NewFriendsViewModel.class);
        recyclerViewMeToOthers = findViewById(R.id.recyclerViewMeToOthers);
        recyclerViewMeToOthers.setLayoutManager(new LinearLayoutManager(this));
        meToOthersAdapter = new MeToOthersAdapter();
        recyclerViewMeToOthers.setAdapter(meToOthersAdapter);
        recyclerViewOtherToMe = findViewById(R.id.recyclerViewOthersToMe);
        recyclerViewOtherToMe.setLayoutManager(new LinearLayoutManager(this));
        othersToMeAdapter = new OthersToMeAdapter();
        recyclerViewOtherToMe.setAdapter(othersToMeAdapter);

        // 获取Share Preferences中的数据
        sharedPreferences = getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID", "fail");
        final String userShortToken = sharedPreferences.getString("userShortToken", "fail");

        // 观察数据库中MeToOthers的变化
        newFriendsViewModel.getAllMeToOthersFromSQL(userID).observe(this, new Observer<List<MeToOthers>>() {
            @Override
            public void onChanged(List<MeToOthers> meToOthers) {
                meToOthersAdapter.setAllMeToOthers(meToOthers);
                meToOthersAdapter.notifyDataSetChanged();
            }
        });

        // 观察数据库中OthersToMe的变化
        newFriendsViewModel.getAllOthersToMeFromSQL(userID).observe(this, new Observer<List<OthersToMe>>() {
            @Override
            public void onChanged(List<OthersToMe> othersToMes) {
                othersToMeAdapter.setAllOthersToMe(othersToMes);
                othersToMeAdapter.notifyDataSetChanged();
            }
        });

        // bind to service
        Intent bindIntent = new Intent(this, ChatService.class);
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ChatService.ChatBinder binder = (ChatService.ChatBinder) service;
                chatService = binder.getService();
                // 当服务器接收新的MeToOthers
                chatService.getMeToOthersFlag().observe(NewFriendsActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        if (integer == 1){
                            List<MeToOthers> meToOthers = chatService.getMeToOthersNew();
                            newFriendsViewModel.updateMeToOthers(meToOthers);
                        }
                    }
                });
                // 当服务器接收新的OtherToMe
                chatService.getOthersToMeFlag().observe(NewFriendsActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        if (integer == 1){
                            List<OthersToMe> othersToMes = chatService.getOthersToMesNew();
                            newFriendsViewModel.updateOthersToMe(othersToMes);
                        }
                    }
                });

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);


    }
}