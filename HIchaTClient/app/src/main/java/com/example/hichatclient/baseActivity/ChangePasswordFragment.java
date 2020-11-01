package com.example.hichatclient.baseActivity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.hichatclient.ApplicationUtil;
import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.User;
import com.example.hichatclient.mainActivity.MainActivity;
import com.example.hichatclient.viewModel.ChangePasswordViewModel;

import java.io.IOException;
import java.net.Socket;

public class ChangePasswordFragment extends Fragment {
    private ChangePasswordViewModel changePasswordViewModel;
    private SharedPreferences sharedPreferences;
    private FragmentActivity activity;
    private ApplicationUtil applicationUtil;
    private Socket socket;

    private String userOldPassword;
    private String userNewPassword;
    private String userNewPasswordCheck;

    private EditText editTextUserOldPassword;
    private EditText editTextUserNewPassword;
    private EditText editTextUserNewPasswordCheck;
    private Button buttonChangePassword;


    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.change_password_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = requireActivity();
        changePasswordViewModel = new ViewModelProvider(activity).get(ChangePasswordViewModel.class);
        applicationUtil = (ApplicationUtil) activity.getApplication();
        if (!applicationUtil.staticIsConnected()) {
            try {
                applicationUtil.initSocketStatic();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket = applicationUtil.getSocketStatic();

        editTextUserOldPassword = activity.findViewById(R.id.editTextOldPassword);
        editTextUserNewPassword = activity.findViewById(R.id.editTextNewPassword);
        editTextUserNewPasswordCheck = activity.findViewById(R.id.editTextNewPasswordCheck);
        buttonChangePassword = activity.findViewById(R.id.buttonChangePassword2);


        // 获取Share Preferences中的数据
        sharedPreferences = activity.getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID", "fail");
        final String userShortToken = sharedPreferences.getString("userShortToken", "fail");

        buttonChangePassword.setEnabled(false);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userOldPassword = editTextUserOldPassword.getText().toString().trim();
                userNewPassword = editTextUserNewPassword.getText().toString().trim();
                userNewPasswordCheck = editTextUserNewPasswordCheck.getText().toString().trim();
                buttonChangePassword.setEnabled(!userOldPassword.isEmpty() & !userNewPassword.isEmpty() & !userNewPasswordCheck.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        editTextUserOldPassword.addTextChangedListener(textWatcher);
        editTextUserNewPassword.addTextChangedListener(textWatcher);
        editTextUserNewPasswordCheck.addTextChangedListener(textWatcher);

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userOldPassword = editTextUserOldPassword.getText().toString().trim();
                userNewPassword = editTextUserNewPassword.getText().toString().trim();
                userNewPasswordCheck = editTextUserNewPasswordCheck.getText().toString().trim();
                try {
                    if (!changePasswordViewModel.checkOldPasswordIsRight(userID, userOldPassword)){
                        Toast.makeText(getActivity(), "旧密码输入错误！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!userNewPassword.equals(userNewPasswordCheck)){
                            Toast.makeText(getActivity(), "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
                        } else {
                            int flag = changePasswordViewModel.updateUserPasswordToServer(userShortToken, userNewPassword, socket);
                            if(flag == 1){
                                User user;
                                user = changePasswordViewModel.getUserInfoByUserID(userID);
                                user.setUserPassword(userNewPassword);
                                changePasswordViewModel.updateUserInfoInSQL(user);
                                Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activity, MainActivity.class);
                                startActivity(intent);

                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }

}