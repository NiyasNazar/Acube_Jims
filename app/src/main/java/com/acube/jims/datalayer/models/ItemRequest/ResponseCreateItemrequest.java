package com.acube.jims.datalayer.models.ItemRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseCreateItemrequest {
    @SerializedName("itemRequestNo")
    @Expose
    private String itemRequestNo;
    @SerializedName("itemID")
    @Expose
    private Integer itemID;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("itemCode")
    @Expose
    private String itemCode;
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    @SerializedName("deviceID")
    @Expose
    private Integer deviceID;
    @SerializedName("viewedOn")
    @Expose
    private String viewedOn;
    @SerializedName("deviceName")
    @Expose
    private String deviceName;
    @SerializedName("trayName")
    @Expose
    private String trayName;
    @SerializedName("customerCode")
    @Expose
    private String customerCode;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("contactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("locationCode")
    @Expose
    private String locationCode;
    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("karatCode")
    @Expose
    private String karatCode;
    @SerializedName("goldWeight")
    @Expose
    private Integer goldWeight;
    @SerializedName("stoneWeight")
    @Expose
    private Integer stoneWeight;

    public String getItemRequestNo() {
        return itemRequestNo;
    }

    public void setItemRequestNo(String itemRequestNo) {
        this.itemRequestNo = itemRequestNo;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Integer deviceID) {
        this.deviceID = deviceID;
    }

    public String getViewedOn() {
        return viewedOn;
    }

    public void setViewedOn(String viewedOn) {
        this.viewedOn = viewedOn;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getTrayName() {
        return trayName;
    }

    public void setTrayName(String trayName) {
        this.trayName = trayName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getKaratCode() {
        return karatCode;
    }

    public void setKaratCode(String karatCode) {
        this.karatCode = karatCode;
    }

    public Integer getGoldWeight() {
        return goldWeight;
    }

    public void setGoldWeight(Integer goldWeight) {
        this.goldWeight = goldWeight;
    }

    public Integer getStoneWeight() {
        return stoneWeight;
    }

    public void setStoneWeight(Integer stoneWeight) {
        this.stoneWeight = stoneWeight;
    }
}
