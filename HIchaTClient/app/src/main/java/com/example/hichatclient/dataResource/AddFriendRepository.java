package com.example.hichatclient.dataResource;

import com.example.hichatclient.data.entity.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class AddFriendRepository {


    // 向服务器发送好友请求，1表示发送成功
    public int addFriend (String personID){

        return 1;
    }

    public List<SearchResult> searchPeopleFromID (String personID){
        List<SearchResult> results= new ArrayList<>();
        SearchResult result1 = new SearchResult("10032", "Jane");
        //SearchResult result2 = new SearchResult("10031", "jack");
        results.add(result1);
        //results.add(result2);
        System.out.println("reposi");
        return results;
    }

}
