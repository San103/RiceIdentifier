package com.san.ripenessidentification.HelperClasses.HomeAdapter;

import android.content.Intent;
import android.widget.ImageView;

public class FeaturedHelperClass{

    String plantname,description,imageUrl,fertilizer,sunlight,watering;
    FeaturedHelperClass(){

    }

    public FeaturedHelperClass(String plantname, String description, String imageUrl,String fertilizer,String sunlight,String watering) {
        this.plantname = plantname;
        this.description = description;
        this.imageUrl = imageUrl;
        this.fertilizer = fertilizer;
        this.sunlight = sunlight;
        this.watering = watering;
    }

    public String getPlantname() {
        return plantname;
    }

    public void setPlantname(String plantname) {
        this.plantname = plantname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFertilizer() {
        return fertilizer;
    }

    public void setFertilizer(String fertilizer) {
        this.fertilizer = fertilizer;
    }

    public String getSunlight() {
        return sunlight;
    }

    public void setSunlight(String sunlight) {
        this.sunlight = sunlight;
    }

    public String getWatering() {
        return watering;
    }

    public void setWatering(String watering) {
        this.watering = watering;
    }


}
