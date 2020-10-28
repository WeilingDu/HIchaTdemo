package com.example.hichatclient.baseActivity;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.User;
import com.example.hichatclient.viewModel.MeViewModel;

import java.util.List;
import java.util.Objects;

public class MeFragment extends Fragment {
    private MeViewModel meViewModel;
    private FragmentActivity activity;
    private String userID;
    private TextView textViewUserID;
    private TextView textViewUserName;
    private Button buttonChangePassword;
    private LiveData<List<User>> users;
    private User user;

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
        users = meViewModel.getUserInfo(meViewModel.getUserID());
        users.observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                user = users.get(users.size()-1);
                textViewUserID.setText(user.getUserID());
                textViewUserName.setText(user.getUserName());
            }
        });

        textViewUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                Bundle bundle = new Bundle();
                bundle.putString("userID", user.getUserID());
                bundle.putString("userName", user.getUserName());
                bundle.putString("userShortToken", user.getUserShortToken());
                navController.navigate(R.id.action_meFragment_to_changNameFragment, bundle);
            }
        });

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                Bundle bundle = new Bundle();
                bundle.putString("userID", user.getUserID());
                bundle.putString("userPassword", user.getUserPassword());
                bundle.putString("userShortToken", user.getUserShortToken());
                navController.navigate(R.id.action_meFragment_to_changePasswordFragment, bundle);
            }
        });





    }

}