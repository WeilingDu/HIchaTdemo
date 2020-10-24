package com.example.hichatclient.data.entity;

public class SearchResult {
    private String resultID;
    private String resultName;

    public String getResultID() {
        return resultID;
    }

    public void setResultID(String resultID) {
        this.resultID = resultID;
    }

    public String getResultName() {
        return resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public SearchResult(String resultID, String resultName) {
        this.resultID = resultID;
        this.resultName = resultName;
    }


}
