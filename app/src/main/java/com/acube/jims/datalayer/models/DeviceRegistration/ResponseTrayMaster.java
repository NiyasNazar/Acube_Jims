package com.acube.jims.datalayer.models.DeviceRegistration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseTrayMaster {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("trayName")
    @Expose
    private String trayName;
    @SerializedName("traySerialNo")
    @Expose
    private String traySerialNo;
    @SerializedName("trayMacAddress")
    @Expose
    private String trayMacAddress;
    @SerializedName("companyID")
    @Expose
    private String companyID;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("modifiedBy")
    @Expose
    private Object modifiedBy;
    @SerializedName("modifiedDate")
    @Expose
    private Object modifiedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrayName() {
        return trayName;
    }

    public void setTrayName(String trayName) {
        this.trayName = trayName;
    }

    public String getTraySerialNo() {
        return traySerialNo;
    }

    public void setTraySerialNo(String traySerialNo) {
        this.traySerialNo = traySerialNo;
    }

    public String getTrayMacAddress() {
        return trayMacAddress;
    }

    public void setTrayMacAddress(String trayMacAddress) {
        this.trayMacAddress = trayMacAddress;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Object getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Object modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Object getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Object modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        return trayMacAddress;
    }
}
