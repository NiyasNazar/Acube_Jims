package com.acube.jims.datalayer.models.missingcomp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MissinDetail {
    @SerializedName("toBeAuditedOn")
    @Expose
    private String toBeAuditedOn;
    @SerializedName("auditID")
    @Expose
    private String auditID;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("serialNo")
    @Expose
    private String serialNo;

    public String getToBeAuditedOn() {
        return toBeAuditedOn;
    }

    public void setToBeAuditedOn(String toBeAuditedOn) {
        this.toBeAuditedOn = toBeAuditedOn;
    }

    public String getAuditID() {
        return auditID;
    }

    public void setAuditID(String auditID) {
        this.auditID = auditID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
}
