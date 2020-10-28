package com.example.hichatclient.baseActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.SearchResult;
import com.example.hichatclient.data.entity.User;
import com.example.hichatclient.dataResource.AddFriendRepository;
import com.example.hichatclient.dataResource.FriendsRepository;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.SearchFriendViewHolder> implements View.OnClickListener{

    List<SearchResult> allSearchFriends = new ArrayList<>();
    AddFriendRepository addFriendRepository;

    public void setAllSearchFriends(List<SearchResult> allSearchFriends) {
        this.allSearchFriends = allSearchFriends;
    }

    @NonNull
    @Override
    public SearchFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_search_friend_info, parent, false);
        return new SearchFriendViewHolder(itemView);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFriendViewHolder holder, int position) {
        final SearchResult friend = allSearchFriends.get(position);
        holder.textViewSearchName.setText(friend.getResultName());
        holder.textViewSearchID.setText(friend.getResultID());
        holder.buttonAdd.setTag(position);

    }

    @Override
    public int getItemCount() {
        return allSearchFriends.size();
    }

    class SearchFriendViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSearchID;
        TextView textViewSearchName;
        ImageButton buttonAdd;

        public SearchFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSearchID = itemView.findViewById(R.id.textViewSearchID);
            textViewSearchName = itemView.findViewById(R.id.textViewSearchName);
            buttonAdd = itemView.findViewById(R.id.imageButton);

        }
    }


    public enum ViewName{
        ITEM,
        PRACTISE
    }

    public interface OnItemClickListener{
        void onItemClick(View v, ViewName viewName, int position);
        void onItemLongClick(View v);
    }


    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v){
        int position = (int) v.getTag();
        if(mOnItemClickListener != null){
            if (v.getId() == R.id.imageButton){
                mOnItemClickListener.onItemClick(v, ViewName.PRACTISE, position);
            }
        }
    }
}
