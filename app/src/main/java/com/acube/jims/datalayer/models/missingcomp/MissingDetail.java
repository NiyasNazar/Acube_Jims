package com.acube.jims.datalayer.models.missingcomp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MissingDetail {
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    @SerializedName("status")
    @Expose
    private List<MissingStatus> status = null;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<MissingStatus> getStatus() {
        return status;
    }

    public void setStatus(List<MissingStatus> status) {
        this.status = status;
    }

}
