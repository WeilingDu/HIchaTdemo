package com.example.hichatclient;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hichatclient.databinding.FragmentLogInBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedHashMap;


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
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        LogInViewModel logInViewModel;
        logInViewModel = new ViewModelProvider(this).get(LogInViewModel.class);

        final FragmentLogInBinding binding;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_in, container, false);
        binding.setData(logInViewModel);
        binding.setLifecycleOwner(getActivity());


        binding.buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = binding.userID.getText().toString();
                String userPassword = binding.userPassword.getText().toString();
                if ( TextUtils.isEmpty(userID) || TextUtils.isEmpty(userPassword)){
                    System.out.println("userID: " + userID);
                    System.out.println("userPassword: " +userPassword);
                    Toast.makeText(getActivity(), "请输入用户名和密码！", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        DatagramSocket socket= new DatagramSocket();
                        // **********发送"用户名和密码"***********
                        InetAddress serverAddress = InetAddress.getByName("139.199.102.166");
                        JSONObject jsonObject=new JSONObject();
                        jsonObject.put("status", "login");
                        jsonObject.put("id", userID);
                        jsonObject.put("password", userPassword);
                        final String str = jsonObject.toString();
                        //String str = userID + userPassword;
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
                        JSONObject jsonObj = new JSONObject(result);
                        //String token = (String) jsonObj.get("token");
                        System.out.println("udpData:" + result);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
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

    }


}