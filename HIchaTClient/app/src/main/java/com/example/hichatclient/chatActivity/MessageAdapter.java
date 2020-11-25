package com.example.hichatclient.chatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.ChattingContent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<ChattingContent> allMsg = new ArrayList<>();

    public void setAllMsg(List<ChattingContent> allMsg) {
        this.allMsg = allMsg;
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_message, parent, false);
        return new MessageViewHolder(itemView);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        // position是当前子项在集合中的位置，通过position参数得到当前项的Msg实例
        ChattingContent chattingContent = allMsg.get(position);
        if (chattingContent.getMsgType().equals("receive")) {
            //如果是收到的信息，则显示左边的布局信息，将右边的信息隐藏
            holder.rightImageView.setVisibility(View.GONE);
            holder.rightLinearLayout.setVisibility(View.GONE);
            holder.rightMsg.setVisibility(View.GONE);


            holder.leftImageView.setVisibility(View.VISIBLE);
            holder.leftLinearLayout.setVisibility(View.VISIBLE);
            holder.leftMsg.setVisibility(View.VISIBLE);
            holder.leftMsg.setText(chattingContent.getMsgContent());
        } else {
            // 若是发出的信息，则显示右边的布局信息，隐藏左边的布局信息
            holder.leftImageView.setVisibility(View.GONE);
            holder.leftLinearLayout.setVisibility(View.GONE);
            holder.leftMsg.setVisibility(View.GONE);


            holder.rightImageView.setVisibility(View.VISIBLE);
            holder.rightLinearLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(chattingContent.getMsgContent());
        }

    }

    @Override
    public int getItemCount() {
        return allMsg.size();
    }



    static class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView leftImageView;
        ImageView rightImageView;
        LinearLayout leftLinearLayout;
        LinearLayout rightLinearLayout;
        TextView leftMsg;
        TextView rightMsg;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            leftLinearLayout = itemView.findViewById(R.id.left_layout);
            rightLinearLayout = itemView.findViewById(R.id.right_layout);
            leftImageView = itemView.findViewById(R.id.left_head);
            rightImageView = itemView.findViewById(R.id.right_head);
            leftMsg = itemView.findViewById(R.id.left_msg);
            rightMsg = itemView.findViewById(R.id.right_msg);
        }
    }
}
