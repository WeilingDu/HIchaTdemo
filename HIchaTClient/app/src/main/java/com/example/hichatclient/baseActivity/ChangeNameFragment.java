package com.example.hichatclient.baseActivity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hichatclient.ApplicationUtil;
import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.User;
import com.example.hichatclient.viewModel.ChangeNameViewModel;

import java.io.IOException;
import java.net.Socket;

public class ChangeNameFragment extends Fragment {
    private TextView textViewUserName;
    private EditText editTextUserNewName;
    private Button buttonChangeName;

    private String userName;
    private String userNewName;

    private FragmentActivity activity;
    private SharedPreferences sharedPreferences;
    private ChangeNameViewModel changeNameViewModel;
    private ApplicationUtil applicationUtil;
    private Socket socket;

    public static ChangeNameFragment newInstance() {
        return new ChangeNameFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_change_name, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = requireActivity();
        changeNameViewModel = new ViewModelProvider(activity).get(ChangeNameViewModel.class);
        applicationUtil = (ApplicationUtil) activity.getApplication();
        if (!applicationUtil.staticIsConnected()) {
            try {
                applicationUtil.initSocketStatic();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket = applicationUtil.getSocketStatic();

        textViewUserName = activity.findViewById(R.id.textView53);
        editTextUserNewName = activity.findViewById(R.id.userNewName);
        buttonChangeName = activity.findViewById(R.id.buttonChangeName);

        // 获取MeFragment传来的参数
        assert getArguments() != null;
        userName = getArguments().getString("userName");

        // 获取Share Preferences中的数据
        sharedPreferences = activity.getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID", "fail");

        // 获取applicationUtil中的数据
        final String userShortToken = applicationUtil.getUserShortToken();


        buttonChangeName.setEnabled(false);
        textViewUserName.setText(userName);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userNewName = editTextUserNewName.getText().toString().trim();
                buttonChangeName.setEnabled(!userID.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editTextUserNewName.addTextChangedListener(textWatcher);

        buttonChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNewName = editTextUserNewName.getText().toString().trim();
                int flag = changeNameViewModel.updateUserNameToServer(userShortToken, userNewName, socket);
                if(flag == 1){
                    try {
                        User user;
                        user = changeNameViewModel.getUserInfoByUserID(userID);
                        user.setUserName(userNewName);
                        changeNameViewModel.updateUserInfoInSQL(user);
                        Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_SHORT).show();
                        NavController navController = Navigation.findNavController(v);
                        navController.navigate(R.id.action_changeNameFragment_to_meFragment);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), "修改失败！", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}