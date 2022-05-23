package com.san.ripenessidentification.Admin.HelperClasses;

import android.util.Log;

import static android.content.ContentValues.TAG;

public class SnapHistory {
    private String userid;
    private String imageUrl;
    private String date;

    SnapHistory(){

    }


    public SnapHistory(String userid, String imageUrl,String date) {
        this.userid = userid;
        this.imageUrl = imageUrl;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
