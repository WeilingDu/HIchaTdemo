package com.example.hichatclient.baseActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.example.hichatclient.R;
import com.example.hichatclient.viewModel.BaseViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {
    private BaseViewModel baseViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        baseViewModel = new ViewModelProvider(this).get(BaseViewModel.class);

        // 设置底部导航栏
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewBase);
        NavController navController = Navigation.findNavController(this, R.id.fragment2);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, configuration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // 获取MainActivity传来的参数
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        String userID;
        String userName;
        String userToken;
        userID = bundle.getString("userID");
        userName = bundle.getString("userName");
        userToken = bundle.getString("userToken");
        System.out.println(userID + userName + userToken);
        baseViewModel.setUserID(userID);
        baseViewModel.setUserName(userName);
        baseViewModel.setUserToken(userToken);
        baseViewModel.getUserFriendsFromServer(userID, userToken);  // 从服务器获取好友列表并存入数据库中

        // 将BaseActivity中的userID、userName、userToken传给其他Fragment;
        MeFragment meFragment = new MeFragment();
        ContactsFragment contactsFragment = new ContactsFragment();

        Bundle bundleMyInfo = new Bundle();
        bundleMyInfo.putString("userID", userID);
        bundleMyInfo.putString("userName", userName);
        bundleMyInfo.putString("userToken", userToken);
        meFragment.setArguments(bundle);
        contactsFragment.setArguments(bundle);

    }
}