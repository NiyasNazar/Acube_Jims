package com.acube.jims.datalayer.models.DeviceRegistration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDeviceRegistration {
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("trayName")
    @Expose
    private String trayName;
    @SerializedName("trayMacAddress")
    @Expose
    private String trayMacAddress;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("deviceID")
    @Expose
    private String deviceID;
    @SerializedName("macAddress")
    @Expose
    private String macAddress;
    @SerializedName("trayID")
    @Expose
    private Integer trayID;
    @SerializedName("companyID")
    @Expose
    private Object companyID;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("modifiedBy")
    @Expose
    private Object modifiedBy;
    @SerializedName("modifiedDate")
    @Expose
    private Object modifiedDate;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTrayName() {
        return trayName;
    }

    public void setTrayName(String trayName) {
        this.trayName = trayName;
    }

    public String getTrayMacAddress() {
        return trayMacAddress;
    }

    public void setTrayMacAddress(String trayMacAddress) {
        this.trayMacAddress = trayMacAddress;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Integer getTrayID() {
        return trayID;
    }

    public void setTrayID(Integer trayID) {
        this.trayID = trayID;
    }

    public Object getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Object companyID) {
        this.companyID = companyID;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
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
}
