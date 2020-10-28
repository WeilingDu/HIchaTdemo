package com.example.hichatclient.mainActivity;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
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

import com.example.hichatclient.R;
import com.example.hichatclient.baseActivity.BaseActivity;
import com.example.hichatclient.viewModel.LogInViewModel;
import com.example.hichatclient.data.entity.User;

import java.io.IOException;


public class LogInFragment extends Fragment {
    private Button buttonLogIn;
    private Button buttonToSignUp;
    private EditText editTextUserID;
    private EditText editTextUserPassword;
    private LogInViewModel logInViewModel;
    private FragmentActivity activity;



    public LogInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = requireActivity();
        logInViewModel = new ViewModelProvider(activity).get(LogInViewModel.class);

        buttonLogIn = activity.findViewById(R.id.buttonLogIn);
        buttonToSignUp = activity.findViewById(R.id.buttonToSignUp);
        editTextUserID = activity.findViewById(R.id.userID);
        editTextUserPassword = activity.findViewById(R.id.userPassword1);

        buttonLogIn.setEnabled(false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userID = editTextUserID.getText().toString().trim();
                String userPassword = editTextUserPassword.getText().toString().trim();
                buttonLogIn.setEnabled(!userID.isEmpty() && !userPassword.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        editTextUserID.addTextChangedListener(textWatcher);
        editTextUserPassword.addTextChangedListener(textWatcher);

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = editTextUserID.getText().toString().trim();
                String userPassword = editTextUserPassword.getText().toString().trim();
                try {
                    User user;
                    user = logInViewModel.sendIDAndPassword(userID, userPassword);
                    //user = logInViewModel.sendIDAndPasswordTest(userID, userPassword); // 用于本地测试
                    if (user == null) {
                        Toast.makeText(getActivity(), "登录失败！", Toast.LENGTH_SHORT).show();
                    } else {
                        logInViewModel.insertUser(user);
                        Toast.makeText(getActivity(), "登录成功！", Toast.LENGTH_SHORT).show();


                        // 跳转至BaseActivity的MeFragment
                        Intent intent = new Intent();
                        intent.setClass(activity, BaseActivity.class);
                        intent.putExtra("userID", user.getUserID());
                        intent.putExtra("userShortToken", user.getUserShortToken());
                        intent.putExtra("userLongToken", user.getUserLongToken());
                        System.out.println("mainActivity-userID: " + user.getUserID());
                        System.out.println("mainActivity-userShortToken: " + user.getUserShortToken());
                        System.out.println("mainActivity-userLongToke: " + user.getUserLongToken());
                        startActivity(intent);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });

        buttonToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_logInFragment_to_signUpFragment);
            }
        });
    }
}