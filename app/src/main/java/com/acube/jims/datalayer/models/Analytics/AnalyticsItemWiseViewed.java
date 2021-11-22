package com.acube.jims.datalayer.models.Analytics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnalyticsItemWiseViewed {
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("itemCode")
    @Expose
    private String itemCode;
    @SerializedName("itemID")
    @Expose
    private Integer itemID;
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("customerID")
    @Expose
    private Object customerID;
    @SerializedName("serialNumber")
    @Expose
    private Object serialNumber;

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

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Object getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Object customerID) {
        this.customerID = customerID;
    }

    public Object getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Object serialNumber) {
        this.serialNumber = serialNumber;
    }
}
