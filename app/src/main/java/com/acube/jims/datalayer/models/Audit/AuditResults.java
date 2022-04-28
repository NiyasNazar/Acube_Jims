package com.acube.jims.datalayer.models.Audit;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "InventoryAudit", indices = @Index(value = {"ID"}, unique = true))

public class AuditResults {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String ID;
    @SerializedName("auditID")
    @Expose
    private String auditID;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;

    @SerializedName("systemLocationID")
    @Expose
    private Integer systemLocationID;
    @SerializedName("systemLocationName")
    @Expose
    private String systemLocationName;

    private String toBeAuditedOn;

    private int status;

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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @NonNull
    public String getID() {
        return ID;
    }

    public void setID(@NonNull String ID) {
        this.ID = ID;
    }

    public String getToBeAuditedOn() {
        return toBeAuditedOn;
    }

    public void setToBeAuditedOn(String toBeAuditedOn) {
        this.toBeAuditedOn = toBeAuditedOn;
    }

    public Integer getSystemLocationID() {
        return systemLocationID;
    }

    public void setSystemLocationID(Integer systemLocationID) {
        this.systemLocationID = systemLocationID;
    }

    public String getSystemLocationName() {
        return systemLocationName;
    }

    public void setSystemLocationName(String systemLocationName) {
        this.systemLocationName = systemLocationName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
