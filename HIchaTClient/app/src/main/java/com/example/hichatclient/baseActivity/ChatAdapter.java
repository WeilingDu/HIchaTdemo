package com.example.hichatclient.baseActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hichatclient.R;
import com.example.hichatclient.chatActivity.ChatActivity;
import com.example.hichatclient.data.entity.ChattingContent;
import com.example.hichatclient.data.entity.ChattingFriend;
import com.example.hichatclient.data.entity.Friend;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChattingFriend> chattingFriends = new ArrayList<>();

    public void setChattingFriends(List<ChattingFriend> chattingFriends) {
        this.chattingFriends = chattingFriends;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_chat, parent, false);
        return new ChatViewHolder(itemView);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatViewHolder holder, int position) {
        final ChattingFriend chattingFriend = chattingFriends.get(position);

        holder.textViewChatFriendName.setText(chattingFriend.getFriendName());
        holder.textViewChatNewContent.setText(chattingFriend.getTheLastMsg());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ChatActivity.class);
                intent.putExtra("friendID", chattingFriend.getFriendID());
                holder.itemView.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return chattingFriends.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView textViewChatFriendName, textViewChatNewContent;
        ImageView imageViewChatFriendImage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewChatFriendName = itemView.findViewById(R.id.chatFriendName);
            textViewChatNewContent = itemView.findViewById(R.id.chatNewContent);
            imageViewChatFriendImage = itemView.findViewById(R.id.imageViewChatFriend);
        }
    }

}
