package com.acube.jims.datalayer.models.Audit;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ResponseAudit {

    @SerializedName("auditID")
    @Expose
    @NonNull
    @PrimaryKey(autoGenerate = false)

    private String auditID;
    @SerializedName("remark")
    @Expose
    private String remark;

    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    @SerializedName("toBeAuditedOn")
    @Expose
    private String toBeAuditedOn;
    @SerializedName("auditResultsList")
    @Expose
    private final List<AuditResults> auditResultsList = null;

    public String getAuditID() {
        return auditID;
    }

    public void setAuditID(String auditID) {
        this.auditID = auditID;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setToBeAuditedOn(String toBeAuditedOn) {
        this.toBeAuditedOn = toBeAuditedOn;
    }

    public String getToBeAuditedOn() {
        return toBeAuditedOn;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    @Override
    public String toString() {
        return auditID + "-" + remark;
    }

    public List<AuditResults> getAuditResultsList() {
        return auditResultsList;
    }
}
