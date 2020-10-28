package com.example.hichatclient.dataResource;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.hichatclient.data.ChatDatabase;
import com.example.hichatclient.data.entity.Friend;
import com.example.hichatclient.data.dao.FriendDao;

import java.util.ArrayList;
import java.util.List;

public class FriendsRepository {
    private FriendDao friendDao;

    public FriendsRepository(Context context) {
        ChatDatabase chatDatabase = ChatDatabase.getDatabase(context.getApplicationContext());
        friendDao = chatDatabase.getFriendDao();
    }


    // 从服务器获取好友列表，在Base Activity中执行
    public List<Friend> getUserFriendsFromServer(String userID, String userShortToken, String userLongToken){
        List<Friend> friends = new ArrayList<>();
        Friend friend1 = new Friend("10048", "10001", "Vincent", "123", "123", "123");
        Friend friend2 = new Friend("10048", "10002", "Vivian", "123", "123", "123");
        Friend friend3 = new Friend("10048", "10003", "Zonglin Wang", "123", "123", "123");
        Friend friend4 = new Friend("10048", "10004", "Siyu Zhang", "123", "123", "123");
        Friend friend5 = new Friend("10048", "10005", "Weiling Du", "123", "123", "123");
        Friend friend6 = new Friend("10048", "10006", "Qinyi Xu", "123", "123", "123");
        friends.add(friend1);
        friends.add(friend2);
        friends.add(friend3);
        friends.add(friend4);
        friends.add(friend5);
        friends.add(friend6);
        insertFriends(friends); // 将用户的好友信息插入数据库
        return friends;
    }

    // 将从服务器获取的好友列表存入到数据库中
    public void insertFriends (List<Friend> friends){
        new FriendsRepository.insertFriendsTread(friendDao, friends).start();
    }

    static class insertFriendsTread extends Thread {
        FriendDao friendDao;
        List<Friend> friends;

        public insertFriendsTread(FriendDao friendDao, List<Friend> friends){
            this.friendDao = friendDao;
            this.friends = friends;
        }

        @Override
        public void run() {
            super.run();
            for (int j=0; j<friends.size(); j++){
                friendDao.insertFriend(friends.get(j));
            }
        }
    }


    // 从数据库中获取好友列表，在ContactsFragments中执行
    public LiveData<List<Friend>> getUserFriendsFromSQL (String userID){
        // 当返回值是LiveData的时候，系统自动放在副线程执行，不用另外写副线程类
        LiveData<List<Friend>> friends;
        friends = friendDao.getAllUserFriend(userID);
        return friends;
    }

    // 从数据库中搜索好友
    public LiveData<List<Friend>> findFriendsWithPatten(String patten){
        return friendDao.findFriendsWithPatten( " %" + patten + "%");  // 加上通配符，模糊匹配
    }


}
