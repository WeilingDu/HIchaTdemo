package com.example.hichatclient.baseActivity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.hichatclient.R;
import com.example.hichatclient.data.entity.Friend;
import com.example.hichatclient.viewModel.ContactsViewModel;

import java.util.List;

public class ContactsFragment extends Fragment {

    private ContactsViewModel contactsViewModel;
    private FragmentActivity activity;
    private LiveData<List<Friend>> allUserFriendsLive;
    private TextView textView;

    public ContactsFragment() {
        setHasOptionsMenu(true);
    }

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.contacts_fragment, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.contacts_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(1000);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = requireActivity();
        contactsViewModel = new ViewModelProvider(activity).get(ContactsViewModel.class);
        if (isAdded()){
            assert getArguments() != null;
            String userID = activity.getIntent().getStringExtra("userID");
            String userName = activity.getIntent().getStringExtra("userName");
            String userToken = activity.getIntent().getStringExtra("userToken");
            contactsViewModel.setUserID(userID);
            contactsViewModel.setUserName(userName);
            contactsViewModel.setUserToken(userToken);
        }

        activity = requireActivity();
        contactsViewModel = new ViewModelProvider(activity).get(ContactsViewModel.class);
        textView = activity.findViewById(R.id.friends);
        String userID;
        userID = contactsViewModel.getUserID();

        contactsViewModel.getUserFriendsFromSQL(userID).observe(activity, new Observer<List<Friend>>() {
            @Override
            public void onChanged(List<Friend> friends) {
                StringBuilder text = new StringBuilder();
                for(int i=0; i<friends.size(); i++){
                    Friend friend = friends.get(i);
                    text.append(friend.getFriendName());
                }
                textView.setText(text);
            }
        });
        // TODO: Use the ViewModel
    }

}