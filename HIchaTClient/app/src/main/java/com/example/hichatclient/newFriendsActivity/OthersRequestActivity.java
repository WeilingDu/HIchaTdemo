package com.example.hichatclient.newFriendsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hichatclient.ApplicationUtil;
import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.OthersToMe;
import com.example.hichatclient.viewModel.OthersRequestViewModel;

public class OthersRequestActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private OthersRequestViewModel othersRequestViewModel;
    private ApplicationUtil applicationUtil;

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

        objectID = getIntent().getStringExtra("objectID");

        try {
            othersToMe = othersRequestViewModel.getOthersToMeByObjectID(userID, objectID);
            textViewObjectID.setText(othersToMe.getObjectID());
            textViewObjectName.setText(othersToMe.getObjectName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        buttonRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                othersRequestViewModel.othersToMeResponseToServer(userShortToken, objectID, true);
                othersToMe.setUserResponse("refuse");
                othersRequestViewModel.updateOthersToMeResponse(othersToMe);
            }
        });

        buttonAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                othersRequestViewModel.othersToMeResponseToServer(userShortToken, objectID, false);
                othersToMe.setUserResponse("agree");
                othersRequestViewModel.updateOthersToMeResponse(othersToMe);
            }
        });

    }
}