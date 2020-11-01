package com.example.hichatclient.newFriendsActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hichatclient.R;
import com.example.hichatclient.chatActivity.ChatActivity;
import com.example.hichatclient.data.entity.OthersToMe;

import java.util.ArrayList;
import java.util.List;

public class OthersToMeAdapter extends RecyclerView.Adapter<OthersToMeAdapter.OthersToMeViewHolder> {
    List<OthersToMe> allOthersToMe = new ArrayList<>();

    public void setAllOthersToMe(List<OthersToMe> allOthersToMe) {
        this.allOthersToMe = allOthersToMe;
    }

    @NonNull
    @Override
    public OthersToMeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_others_to_me, parent, false);
        return new OthersToMeViewHolder(itemView);
    }

    static class OthersToMeViewHolder extends RecyclerView.ViewHolder {
        TextView textViewObjectID, textViewObjectName, textViewUserResponse;

        public OthersToMeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewObjectID = itemView.findViewById(R.id.textViewObjectID);
            textViewObjectName = itemView.findViewById(R.id.textViewObjectName);
            textViewUserResponse = itemView.findViewById(R.id.textViewUserRep);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final OthersToMeViewHolder holder, int position) {
        final OthersToMe othersToMe = allOthersToMe.get(position);
        holder.textViewObjectID.setText(othersToMe.getObjectID());
        holder.textViewObjectName.setText(othersToMe.getObjectName());
        holder.textViewUserResponse.setText(othersToMe.getUserResponse());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), OthersRequestActivity.class);
                intent.putExtra("objectID", othersToMe.getObjectID());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allOthersToMe.size();
    }
}
