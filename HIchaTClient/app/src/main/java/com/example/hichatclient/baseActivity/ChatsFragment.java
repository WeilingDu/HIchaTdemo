package com.example.hichatclient.baseActivity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hichatclient.ApplicationUtil;
import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.Friend;
import com.example.hichatclient.viewModel.ChatsViewModel;

import java.util.List;

public class ChatsFragment extends Fragment {
    private ChatsViewModel chatsViewModel;
    private FragmentActivity activity;
    private SharedPreferences sharedPreferences;
    private ApplicationUtil applicationUtil;

    private List<Friend> allChattingFriend;

    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;

    public static ChatsFragment newInstance() {
        return new ChatsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chats_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        activity = requireActivity();
//        chatsViewModel = new ViewModelProvider(activity).get(ChatsViewModel.class);
//        applicationUtil = (ApplicationUtil) activity.getApplication();
//
//        // 获取Share Preferences中的数据
//        sharedPreferences = activity.getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
//        final String userID = sharedPreferences.getString("userID", "fail");

//        recyclerView = activity.findViewById(R.id.recyclerViewChats);
//        chatAdapter = new ChatAdapter();
//        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
//        recyclerView.setAdapter(chatAdapter);

//        allChattingFriend = chatsViewModel.getAllChattingFriendFromSQL(userID).getValue();
//        chatAdapter.setChattingFriends(allChattingFriend);
//
//        chatsViewModel.getAllChattingFriendFromSQL(userID).observe(activity, new Observer<List<Friend>>() {
//            @Override
//            public void onChanged(List<Friend> friends) {
//                int temp = chatAdapter.getItemCount();
//                chatAdapter.setChattingFriends(friends);
//                if(temp != friends.size()){
//                    chatAdapter.notifyDataSetChanged();
//                }
//            }
//        });

    }

}