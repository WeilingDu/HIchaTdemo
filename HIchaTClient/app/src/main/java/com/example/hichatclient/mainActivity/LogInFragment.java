package com.example.hichatclient.mainActivity;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hichatclient.R;
import com.example.hichatclient.baseActivity.BaseActivity;
import com.example.hichatclient.viewModel.LogInViewModel;
import com.example.hichatclient.data.User;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {
    private Button buttonLogIn;
    private Button buttonToSignUp;
    private EditText editTextUserID;
    private EditText editTextUserPassword;
    private LogInViewModel logInViewModel;
    private FragmentActivity activity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        editTextUserID.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextUserID, 0);

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
//                System.out.println("userID is: " + userID);
//                System.out.println("userPassword is: " + userPassword);
                User user;
                user = logInViewModel.sendIDAndPassword(userID, userPassword);
//                System.out.println(user.getUserToken());
//                System.out.println(user.getUserName());
                if (user == null) {
//                    System.out.println("failed!");
                    Toast.makeText(getActivity(), "登录失败！", Toast.LENGTH_SHORT).show();
                } else {
//                    System.out.println("succeed!");
                    logInViewModel.insertUser(user);
                    Toast.makeText(getActivity(), "登录成功！", Toast.LENGTH_SHORT).show();
//                    NavController navController = Navigation.findNavController(v);
//                    navController.navigate(R.id.action_logInFragment_to_meFragment);
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    // 跳转至BaseActivity的MeFragment
                    Intent intent = new Intent();
                    intent.setClass(activity, BaseActivity.class);
                    intent.putExtra("userID", user.getUserID());
                    intent.putExtra("userName", user.getUserName());
                    intent.putExtra("userToken", user.getUserToken());
                    startActivity(intent);
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