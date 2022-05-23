package com.san.ripenessidentification.Admin.HelperClasses;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ScanHistory {
    private String userid;
    private String imageUrl;
    private String dateCaptured;
    private String unRipe;
    private String earlyRipe;
    private String partiallyRipe;
    private String fullyRipe;
    private String defectiveness;

    ScanHistory() {
    }

    public ScanHistory(String userid, String imageUrl, String dateCaptured, String unRipe, String earlyRipe, String partiallyRipe, String fullyRipe, String defectiveness) {
        this.userid = userid;
        this.imageUrl = imageUrl;
        this.dateCaptured = dateCaptured;
        this.unRipe = unRipe;
        this.earlyRipe = earlyRipe;
        this.partiallyRipe = partiallyRipe;
        this.fullyRipe = fullyRipe;
        this.defectiveness = defectiveness;
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

    public String getDateCaptured() {
        return dateCaptured;
    }

    public void setDateCaptured(String dateCaptured) {
        this.dateCaptured = dateCaptured;
    }

    public String getUnRipe() {
        return unRipe;
    }

    public void setUnRipe(String unRipe) {
        this.unRipe = unRipe;
    }

    public String getEarlyRipe() {
        return earlyRipe;
    }

    public void setEarlyRipe(String earlyRipe) {
        this.earlyRipe = earlyRipe;
    }

    public String getPartiallyRipe() {
        return partiallyRipe;
    }

    public void setPartiallyRipe(String partiallyRipe) {
        this.partiallyRipe = partiallyRipe;
    }

    public String getFullyRipe() {
        return fullyRipe;
    }

    public void setFullyRipe(String fullyRipe) {
        this.fullyRipe = fullyRipe;
    }

    public String getDefectiveness() {
        return defectiveness;
    }

    public void setDefectiveness(String defectiveness) {
        this.defectiveness = defectiveness;
    }
}
