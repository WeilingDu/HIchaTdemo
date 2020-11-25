package com.example.hichatclient.baseActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.User;
import com.example.hichatclient.mainActivity.LogInFragment;
import com.example.hichatclient.mainActivity.MainActivity;
import com.example.hichatclient.viewModel.MeViewModel;

import java.util.List;
import java.util.Objects;

public class MeFragment extends Fragment {
    private MeViewModel meViewModel;
    private FragmentActivity activity;
    private TextView textViewUserID;
    private TextView textViewUserName;
    private Button buttonChangePassword;
    private Button buttonExit;
    private ImageView imageViewProfile;
    private LiveData<List<User>> users;
    private User user;
    private SharedPreferences sharedPreferences;


    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
        activity = requireActivity();
        meViewModel = new ViewModelProvider(activity).get(MeViewModel.class);

        textViewUserID = activity.findViewById(R.id.textViewUserID3);
        textViewUserName = activity.findViewById(R.id.textViewUserName3);
        buttonChangePassword = activity.findViewById(R.id.buttonChangePassword);
        buttonExit = activity.findViewById(R.id.buttonExit);
        imageViewProfile = activity.findViewById(R.id.imageViewProfile);

//        imageViewProfile.setImageResource(R.drawable.profile);


        // 获取Share Preferences中的数据
        sharedPreferences = activity.getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID", "fail");

        // 从数据库中获取用户信息
        users = meViewModel.getUserInfo(userID);
        users.observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                user = users.get(0);
                textViewUserID.setText(user.getUserID());
                textViewUserName.setText(user.getUserName());
                imageViewProfile.setImageBitmap(toRoundCorner(BitmapFactory.decodeByteArray(user.getUserProfile(), 0, user.getUserProfile().length), 2));
            }
        });

        textViewUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                Bundle bundle = new Bundle();
                bundle.putString("userName", user.getUserName());
                navController.navigate(R.id.action_meFragment_to_changNameFragment, bundle);
            }
        });

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_meFragment_to_changePasswordFragment);
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(activity, MainActivity.class);
                startActivity(intent);
            }
        });





    }

    public static Bitmap toRoundCorner(Bitmap bitmap, float ratio) {
        System.out.println("图片是否变成圆形模式了+++++++++++++");
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, bitmap.getWidth() / ratio,
                bitmap.getHeight() / ratio, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        System.out.println("pixels+++++++" + String.valueOf(ratio));

        return output;

    }

}