package com.example.hichatclient;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hichatclient.databinding.FragmentLogInBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {

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
        final LogInViewModel logInViewModel;
        logInViewModel = new ViewModelProvider(this).get(LogInViewModel.class);

        FragmentLogInBinding binding;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_in, container, false);
        binding.setData(logInViewModel);
        binding.setLifecycleOwner(getActivity());


        final String userID = binding.userID.getText().toString();
        final String userPassword = binding.userPassword.getText().toString();

        binding.buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( TextUtils.isEmpty(userID) || TextUtils.isEmpty(userPassword)){
                    System.out.println("userID: " + userID);
                    System.out.println("userPassword: " +userPassword);
                    Toast.makeText(getActivity(), "请输入用户名和密码！", Toast.LENGTH_SHORT).show();
                }
                if(logInViewModel.logIn()){
                    Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.buttonToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_logInFragment_to_signUpFragment);
            }
        });

        return binding.getRoot();
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_log_in, container, false);
    }


}