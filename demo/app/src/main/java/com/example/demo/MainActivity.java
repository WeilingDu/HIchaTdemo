package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = (Button) findViewById(R.id.send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendAndReceive();
            }
        });
    }

    protected void SendAndReceive(){
        try {
            //创建DatagramSocket对象
            DatagramSocket socket= new DatagramSocket();

            // **********发送数据***********
            // 使用InetAddress(Inet4Address).getByName把IP地址转换为网络地址
            InetAddress serverAddress = InetAddress.getByName("139.199.102.166");
            // 设置要发送的报文
            String str = "hello world";
            // 把字符串str字符串转换为字节数组
            byte send_data[] = str.getBytes();
            // 创建一个DatagramPacket对象，用于发送数据
            // 参数一：要发送的数据  参数二：数据的长度  参数三：服务端的网络地址  参数四：服务器端端口号
            DatagramPacket send_packet = new DatagramPacket(send_data, send_data.length ,serverAddress ,9000);
            // 把数据发送到服务端
            socket.send(send_packet);
            Toast.makeText(MainActivity.this, "连接成功！", Toast.LENGTH_SHORT).show();

            // **********接收数据***********
            byte recieve_data[] = new byte[4 * 1024];
            // 参数一:要接受的data 参数二：data的长度
            DatagramPacket recieve_packet = new DatagramPacket(recieve_data, recieve_data.length);
            socket.receive(recieve_packet);
            // 把接收到的data转换为String字符串
            String result = new String(recieve_packet.getData(), recieve_packet.getOffset(), recieve_packet.getLength());
            socket.close();
            System.out.println("udpData:" + result);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}