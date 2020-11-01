package com.example.hichatclient.newFriendsActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.MeToOthers;

import java.util.ArrayList;
import java.util.List;

public class MeToOthersAdapter extends RecyclerView.Adapter<MeToOthersAdapter.MeToOthersViewHolder> {
    List<MeToOthers> allMeToOthers = new ArrayList<>();

    public void setAllMeToOthers(List<MeToOthers> allMeToOthers) {
        this.allMeToOthers = allMeToOthers;
    }

    @NonNull
    @Override
    public MeToOthersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_me_to_others, parent, false);
        return new MeToOthersViewHolder(itemView);
    }
    static class MeToOthersViewHolder extends RecyclerView.ViewHolder{
        TextView textViewNewFriendID, textViewNewFriendName, textViewNewFriendRep;


        public MeToOthersViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNewFriendID = itemView.findViewById(R.id.textViewNewFriendID);
            textViewNewFriendName = itemView.findViewById(R.id.textViewNewFriendName);
            textViewNewFriendRep = itemView.findViewById(R.id.textViewNewFriendRep);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MeToOthersViewHolder holder, int position) {
        MeToOthers meToOthers = allMeToOthers.get(position);
        holder.textViewNewFriendID.setText(meToOthers.getObjectID());
        holder.textViewNewFriendName.setText(meToOthers.getObjectName());
        holder.textViewNewFriendRep.setText(meToOthers.getObjectResponse());

    }

    @Override
    public int getItemCount() {
        return allMeToOthers.size();
    }


}
