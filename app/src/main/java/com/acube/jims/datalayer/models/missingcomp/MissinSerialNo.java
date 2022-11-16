package com.acube.jims.datalayer.models.missingcomp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MissinSerialNo {

    @SerializedName("serialNo")
    @Expose
    private String serialNo;
    @SerializedName("missinDetails")
    @Expose
    private List<MissinDetail> missinDetails = null;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public List<MissinDetail> getMissinDetails() {
        return missinDetails;
    }

    public void setMissinDetails(List<MissinDetail> missinDetails) {
        this.missinDetails = missinDetails;
    }
}
