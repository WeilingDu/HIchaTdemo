package com.example.hichatclient.baseActivity;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.User;
import com.example.hichatclient.viewModel.MeViewModel;

public class MeFragment extends Fragment {
    private MeViewModel meViewModel;
    private FragmentActivity activity;
    private String userID;
    private TextView textViewUserID;
    private TextView textViewUserName;
    private Button buttonChangePassword;

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel

        activity = requireActivity();
        meViewModel = new ViewModelProvider(activity).get(MeViewModel.class);

        textViewUserID = activity.findViewById(R.id.textViewUserID3);
        textViewUserName = activity.findViewById(R.id.textViewUserName3);
        buttonChangePassword = activity.findViewById(R.id.buttonChangePassword);

        // 获取BaseActivity传递过来的参数
        if (isAdded()){
            assert getArguments() != null;
            userID = activity.getIntent().getStringExtra("userID");
            meViewModel.setUserID(userID);
        }

        // 从数据库中获取用户信息
//        userInfo = meViewModel.getUserInfo(meViewModel.getUserID());
//        userInfo.observe(activity, new Observer<User>() {
//            @Override
//            public void onChanged(User user) {
//                textViewUserID.setText(user.getId());
//                textViewUserName.setText(user.getUserName());
//            }
//        });

    }

}