package com.san.ripenessidentification.HelperClasses.HomeAdapter;

public class MostViewedHelperClass {

    String name,description,imgUrl;
    MostViewedHelperClass(){

    }
    public MostViewedHelperClass(String img, String name, String description) {
        this.imgUrl = img;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
