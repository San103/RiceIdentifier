package com.san.ripenessidentification.Admin.HelperClasses;

public class PlantInfo {
    private String plantname;
    private String description;
    private String fertilizer;
    private String sunlight;
    private String watering;
    private String imageUrl;

    public String getPlantname() {
        return plantname;
    }

    public PlantInfo(String plantname, String description, String fertilizer, String sunlight, String watering, String imageUrl) {
        this.plantname = plantname;
        this.description = description;
        this.fertilizer = fertilizer;
        this.sunlight = sunlight;
        this.watering = watering;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
