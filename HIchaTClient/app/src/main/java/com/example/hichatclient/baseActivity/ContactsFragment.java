package com.example.hichatclient.baseActivity;

import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.Friend;
import com.example.hichatclient.viewModel.ContactsViewModel;

import java.util.List;

public class ContactsFragment extends Fragment {

    private ContactsViewModel contactsViewModel;
    private FragmentActivity activity;
    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;
    private Button button;
    private SharedPreferences sharedPreferences;

    public ContactsFragment() {
        setHasOptionsMenu(true);  // 强制顶部工具条显示
    }

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contacts_fragment, container, false);
    }

    // 通讯录界面的顶部工具条
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.contacts_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(900);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = requireActivity();
        // contactsViewModel只在这个Fragment的生命周期中存活
        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);

        // 获取Share Preferences中的数据
        sharedPreferences = activity.getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID", "fail");
        final String userShortToken = sharedPreferences.getString("userShortToken", "fail");


        recyclerView = activity.findViewById(R.id.recyclerFriends);
        friendAdapter = new FriendAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(friendAdapter);


        contactsViewModel.getUserFriendsFromSQL(userID).observe(activity, new Observer<List<Friend>>() {
            @Override
            public void onChanged(List<Friend> friends) {
                int temp = friendAdapter.getItemCount();
                friendAdapter.setAllFriends(friends);
                friendAdapter.setUserID(userID);
                friendAdapter.setUserShortToken(userShortToken);
                if(temp != friends.size()){
                    friendAdapter.notifyDataSetChanged();
                }
            }
        });

        button = activity.findViewById(R.id.buttonAddFriend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_contactsFragment_to_addFriendFragment);
            }
        });

        // TODO: Use the ViewModel
    }

}