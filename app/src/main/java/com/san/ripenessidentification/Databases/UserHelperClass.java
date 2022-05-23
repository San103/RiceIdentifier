package com.san.ripenessidentification.Databases;

public class UserHelperClass {
    String fullname,username, number, password, subscription, type, imgUrl;
    Integer clicked;
    public UserHelperClass(){}

    public UserHelperClass(String fullname, String username, String number, String password, String subscription, String type, String imgUrl, Integer clicked) {
        this.fullname = fullname;
        this.username = username;
        this.number = number;
        this.password = password;
        this.subscription = subscription;
        this.type = type;
        this.imgUrl = imgUrl;
        this.clicked = clicked;
    }

    public Integer getClicked() {
        return clicked;
    }

    public void setClicked(Integer clicked) {
        clicked = clicked;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
