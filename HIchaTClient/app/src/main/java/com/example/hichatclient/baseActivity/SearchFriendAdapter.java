package com.example.hichatclient.baseActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.SearchFriendViewHolder>{

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
    public void onBindViewHolder(@NonNull final SearchFriendViewHolder holder, int position) {
        final SearchResult friend = allSearchFriends.get(position);
        holder.textViewSearchName.setText(friend.getResultName());
        holder.textViewSearchID.setText(friend.getResultID());
        holder.buttonAdd.setTag(position);
        holder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), "已发送好友请求！", Toast.LENGTH_SHORT).show();
//                AlertDialog.Builder builder= new AlertDialog.Builder(holder.itemView.getContext());
//                builder.setTitle("已发送好友请求！");
//                builder.setNeutralButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
            }
        });

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


//    public enum ViewName{
//        ITEM,
//        PRACTISE
//    }
//
//    public interface OnItemClickListener{
//        void onItemClick(View v, ViewName viewName, int position);
//        void onItemLongClick(View v);
//    }
//
//
//    private OnItemClickListener mOnItemClickListener;
//
//    public void setOnItemClickListener(OnItemClickListener listener){
//        this.mOnItemClickListener = listener;
//    }
//
//    @Override
//    public void onClick(View v){
//        int position = (int) v.getTag();
//        if(mOnItemClickListener != null){
//            if (v.getId() == R.id.imageButton){
//                mOnItemClickListener.onItemClick(v, ViewName.PRACTISE, position);
//            }
//        }
//    }
}
