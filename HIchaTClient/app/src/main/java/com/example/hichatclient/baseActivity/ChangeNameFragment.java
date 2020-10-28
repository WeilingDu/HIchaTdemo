package com.example.hichatclient.baseActivity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

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

import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.User;
import com.example.hichatclient.viewModel.ChangeNameViewModel;

public class ChangeNameFragment extends Fragment {
    private ChangeNameViewModel changeNameViewModel;
    private String userID;
    private String userName;
    private String userNewName;
    private String userShortToken;
    private TextView textViewUserName;
    private EditText editTextUserNewName;
    private Button buttonChangeName;
    private FragmentActivity activity;

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
        textViewUserName = activity.findViewById(R.id.textView53);
        editTextUserNewName = activity.findViewById(R.id.userNewName);
        buttonChangeName = activity.findViewById(R.id.buttonChangeName);

        // 获取MeFragment传来的参数
        assert getArguments() != null;
        userID = getArguments().getString("userID");
        userName = getArguments().getString("userName");
        userShortToken = getArguments().getString("userShortToken");


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
                int flag = changeNameViewModel.updateUserNameToServer(userShortToken, userNewName);
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