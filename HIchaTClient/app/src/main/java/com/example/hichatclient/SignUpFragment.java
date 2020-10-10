package com.example.hichatclient;

import android.os.Build;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hichatclient.databinding.FragmentSignUpBinding;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

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
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SignUpViewModel signUpViewModel;
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        final FragmentSignUpBinding binding;
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up,container,false);
        binding.setData(signUpViewModel);
        binding.setLifecycleOwner(getActivity());

        binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = binding.userName.getText().toString();
                String userPassword = binding.userPassword.getText().toString();
                String userPasswordCheck = binding.userPasswordCheck.getText().toString();
                if ( TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPassword) || TextUtils.isEmpty(userPasswordCheck)){
                    System.out.println("userName: " + userName);
                    System.out.println("userPassword: " +userPassword);
                    System.out.println("userPassword: " +userPasswordCheck);
                    Toast.makeText(getActivity(), "请填写所有信息！", Toast.LENGTH_SHORT).show();
                }
                else if(!userPassword.equals(userPasswordCheck)){
                    Toast.makeText(getActivity(), "两次填写的密码不相同！", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        DatagramSocket socket= new DatagramSocket();
                        // **********发送"用户名和密码"***********
                        InetAddress serverAddress = InetAddress.getByName("139.199.102.166");
                        String str = userName + userPassword;
                        byte send_data[] = str.getBytes();
                        DatagramPacket send_packet = new DatagramPacket(send_data, send_data.length ,serverAddress ,9000);
                        socket.send(send_packet);
                        Toast.makeText(getActivity(), "连接成功！", Toast.LENGTH_SHORT).show();

                        // **********接收"是否连接成功和用户ID"***********
                        byte recieve_data[] = new byte[4 * 1024];
                        DatagramPacket recieve_packet = new DatagramPacket(recieve_data, recieve_data.length);
                        socket.receive(recieve_packet);
                        String result = new String(recieve_packet.getData(), recieve_packet.getOffset(), recieve_packet.getLength());
                        socket.close();
                        System.out.println("udpData:" + result);
                        Toast.makeText(getActivity(), "注册成功！"+result, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        binding.buttonToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_signUpFragment_to_logInFragment);
            }
        });

        return binding.getRoot();

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }
}