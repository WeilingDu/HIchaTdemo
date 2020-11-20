package com.example.hichatclient.newFriendsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hichatclient.ApplicationUtil;
import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.SearchResult;
import com.example.hichatclient.viewModel.SearchFriendViewModel;

import java.io.IOException;
import java.net.Socket;

public class SearchFriendActivity extends AppCompatActivity {
    private ApplicationUtil applicationUtil;
    private SharedPreferences sharedPreferences;
    private SearchFriendViewModel searchFriendViewModel;
    private Socket socket;

    private String userShortToken;
    private String userID;
    private String searchID;
    private SearchResult searchResult;


    private EditText editTextSearchID;
    private TextView textViewSearchResult;
    private Button buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        editTextSearchID = findViewById(R.id.editTextSearchID);
        textViewSearchResult = findViewById(R.id.textViewSearchResult);
        buttonSearch = findViewById(R.id.buttonSearch);

        searchFriendViewModel = new ViewModelProvider(this).get(SearchFriendViewModel.class);

        // 获取applicationUtil中的数据
        applicationUtil = (ApplicationUtil) SearchFriendActivity.this.getApplication();
        socket = applicationUtil.getSocketStatic();
        userShortToken = applicationUtil.getUserShortToken();

        // 获取Share Preferences中的数据
        sharedPreferences = getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("userID", "fail");


        buttonSearch.setEnabled(false);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchID = editTextSearchID.getText().toString().trim();
                buttonSearch.setEnabled(!searchID.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        editTextSearchID.addTextChangedListener(textWatcher);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                try {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println("SearchFriendActivity searchID: " + searchID);
                                searchResult = searchFriendViewModel.searchPeopleFromID(searchID, userShortToken, socket);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();
                    t.join();
                    if (searchResult == null){
                        textViewSearchResult.setText(R.string.searchResultChinese);
                    } else {
                        // 跳转至新的界面，显示搜索结果的信息
                        Intent intent = new Intent();
                        intent.setClass(SearchFriendActivity.this, AddNewFriendActivity.class);
                        intent.putExtra("resultID", searchResult.getResultID());
                        intent.putExtra("resultName", searchResult.getResultName());
                        startActivity(intent);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}