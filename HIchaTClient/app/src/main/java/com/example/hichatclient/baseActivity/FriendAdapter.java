package com.example.hichatclient.baseActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hichatclient.R;
import com.example.hichatclient.chatActivity.ChatActivity;
import com.example.hichatclient.chatActivity.FriendInfoActivity;
import com.example.hichatclient.data.entity.Friend;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
    List<Friend> allFriends = new ArrayList<>();

    public void setAllFriends(List<Friend> allFriends) {
        this.allFriends = allFriends;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_friend_info, parent, false);
        return new FriendViewHolder(itemView);
        // return null;
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewFriendImage;
        TextView textViewFriendName;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFriendImage = itemView.findViewById(R.id.friendImage);
            textViewFriendName = itemView.findViewById(R.id.friendName);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendViewHolder holder, int position) {
        final Friend friend = allFriends.get(position);
        holder.textViewFriendName.setText(friend.getFriendName());
        holder.imageViewFriendImage.setImageResource(R.drawable.profile);;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击好友，跳转至好友信息界面（FriendInfoActivity）
                Intent intent = new Intent(holder.itemView.getContext(), FriendInfoActivity.class);
                intent.putExtra("friendID", friend.getFriendID());
                holder.itemView.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return allFriends.size();
    }


}
