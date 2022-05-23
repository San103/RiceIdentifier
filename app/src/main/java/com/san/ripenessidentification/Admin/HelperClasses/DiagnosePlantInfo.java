package com.san.ripenessidentification.Admin.HelperClasses;

public class DiagnosePlantInfo {
    String diagname;
    String diagdescription;
    String imgUrl;

    public DiagnosePlantInfo(String diagname, String diagdescription, String imgUrl) {
        this.diagname = diagname;
        this.diagdescription = diagdescription;
        this.imgUrl = imgUrl;
    }

    public String getDiagname() {
        return diagname;
    }

    public void setDiagname(String diagname) {
        this.diagname = diagname;
    }

    public String getDiagdescription() {
        return diagdescription;
    }

    public void setDiagdescription(String diagdescription) {
        this.diagdescription = diagdescription;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
