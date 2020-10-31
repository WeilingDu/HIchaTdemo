package com.example.hichatclient.baseActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.hichatclient.ApplicationUtil;
import com.example.hichatclient.R;
import com.example.hichatclient.viewModel.BaseViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.net.Socket;

public class BaseActivity extends AppCompatActivity {
    private BaseViewModel baseViewModel;
    private SharedPreferences sharedPreferences;
    private ApplicationUtil applicationUtil;
    private Socket socket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        baseViewModel = new ViewModelProvider(this).get(BaseViewModel.class);
        applicationUtil = (ApplicationUtil) BaseActivity.this.getApplication();
        socket = applicationUtil.getSocket();

        // 设置底部导航栏
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewBase);
        NavController navController = Navigation.findNavController(this, R.id.fragment2);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, configuration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // 获取Share Preferences中的数据
        sharedPreferences = getSharedPreferences("MY_DATA", MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID", "fail");
        final String userShortToken = sharedPreferences.getString("userShortToken", "fail");


        // 从服务器获取好友列表并存入数据库中
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    baseViewModel.getUserFriendsFromServer(userID, userShortToken, socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
}