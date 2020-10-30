package com.example.hichatclient.baseActivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.SearchResult;
import com.example.hichatclient.viewModel.AddFriendViewModel;

import java.util.List;


public class AddFriendFragment extends Fragment {
    private FragmentActivity activity;
    private AddFriendViewModel addFriendViewModel;
    private RecyclerView recyclerView;
    private SearchFriendAdapter searchFriendAdapter;
    private EditText editTextSearchID;
    private Button buttonSearch;
    private List<SearchResult> searchResults;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_friend, container, false);


        return inflater.inflate(R.layout.fragment_add_friend, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = requireActivity();
        addFriendViewModel = new ViewModelProvider(activity).get(AddFriendViewModel.class);

        editTextSearchID = activity.findViewById(R.id.editTextTextPersonName);
        buttonSearch = activity.findViewById(R.id.buttonSearch);

        recyclerView = activity.findViewById(R.id.recyclerSearch);
        searchFriendAdapter = new SearchFriendAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(searchFriendAdapter);
        //searchFriendAdapter.setOnItemClickListener(MyItemClickListener);


        // 搜索ID
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchID;
                searchID = editTextSearchID.getText().toString().trim();
                searchResults = addFriendViewModel.searchPeopleFromID(searchID);
                if (searchResults.size() == 0){
                    Toast.makeText(getActivity(), "用户不存在！", Toast.LENGTH_SHORT).show();
                } else if(searchID.equals("10021")){
                    Toast.makeText(getActivity(), "用户不存在！", Toast.LENGTH_SHORT).show();
                } else {
                    int temp = searchFriendAdapter.getItemCount();
                    searchFriendAdapter.setAllSearchFriends(searchResults);
                    if(temp != searchResults.size()){
                        searchFriendAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }



//    private SearchFriendAdapter.OnItemClickListener MyItemClickListener = new SearchFriendAdapter.OnItemClickListener() {
//        @Override
//        public void onItemClick(View v, SearchFriendAdapter.ViewName viewName, int position) {
//            if (v.getId() == R.id.imageButton){
//                Toast.makeText(activity,"发送请求"+(position+1),Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onItemLongClick(View v) {
//
//        }
//    };

}