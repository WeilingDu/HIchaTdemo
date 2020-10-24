package com.example.hichatclient.mainActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

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
import com.example.hichatclient.viewModel.SignUpViewModel;


public class SignUpFragment extends Fragment {
    private EditText editTextUserName;
    private EditText editTextUserPassword;
    private EditText editTextUserPasswordCheck;
    private Button buttonSignUp;
    private Button buttonToLogIn;
    private SignUpViewModel signUpViewModel;
    private FragmentActivity activity;
    private View view;



    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = requireActivity();
        signUpViewModel = new ViewModelProvider(activity).get(SignUpViewModel.class);
        editTextUserName = activity.findViewById(R.id.userName1);
        editTextUserPassword = activity.findViewById(R.id.userPasswordSign);
        editTextUserPasswordCheck = activity.findViewById(R.id.userPasswordCheck1);
        buttonSignUp = activity.findViewById(R.id.buttonSignUp);
        buttonToLogIn = activity.findViewById(R.id.buttonToLogIn);

        buttonSignUp.setEnabled(false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userName = editTextUserName.getText().toString().trim();
                String userPassword = editTextUserPassword.getText().toString().trim();
                String userPasswordCheck = editTextUserPasswordCheck.getText().toString().trim();
                buttonSignUp.setEnabled(!userName.isEmpty() && !userPassword.isEmpty() && !userPasswordCheck.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        editTextUserName.addTextChangedListener(textWatcher);
        editTextUserPassword.addTextChangedListener(textWatcher);
        editTextUserPasswordCheck.addTextChangedListener(textWatcher);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextUserName.getText().toString().trim();
                String userPassword = editTextUserPassword.getText().toString().trim();
                String userPasswordCheck = editTextUserPasswordCheck.getText().toString().trim();
                String userID;
                if (!userPassword.equals(userPasswordCheck)){
                    Toast.makeText(getActivity(), "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
                } else {
                    userID = signUpViewModel.signUp(userName, userPassword);
                    AlertDialog.Builder builder= new AlertDialog.Builder(activity);
                    builder.setTitle("注册成功！您的ID为：" + userID);
                    builder.setPositiveButton("返回登录界面", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NavController controller = Navigation.findNavController(view);
                            controller.navigate(R.id.action_signUpFragment_to_logInFragment);
                        }
                    });
                    builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create();
                    builder.show();
                }
            }
        });


        buttonToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_signUpFragment_to_logInFragment);
            }
        });


    }
}