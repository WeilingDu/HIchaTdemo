package com.example.hichatclient.mainActivity;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    private EditText editTextUserName;
    private EditText editTextUserPassword;
    private EditText editTextUserPasswordCheck;
    private Button buttonSignUp;
    private Button buttonToLogIn;
    private SignUpViewModel signUpViewModel;
    private FragmentActivity activity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = requireActivity();
        signUpViewModel = new ViewModelProvider(activity).get(SignUpViewModel.class);
        editTextUserName = activity.findViewById(R.id.userName1);
        editTextUserPassword = activity.findViewById(R.id.userPassword1);
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
                    System.out.println(userID);
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