package com.acube.jims.datalayer.models.CustomerManagment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemViewHistory {
    @SerializedName("customerID")
    @Expose
    private Integer customerID;
    @SerializedName("customerCode")
    @Expose
    private Object customerCode;
    @SerializedName("customerName")
    @Expose
    private Object customerName;
    @SerializedName("customerEmailID")
    @Expose
    private Object customerEmailID;
    @SerializedName("customerContactNo")
    @Expose
    private Object customerContactNo;
    @SerializedName("itemID")
    @Expose
    private Integer itemID;
    @SerializedName("itemCode")
    @Expose
    private String itemCode;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("imagePath")
    @Expose
    private String imagePath;
    @SerializedName("trayID")
    @Expose
    private Object trayID;
    @SerializedName("trayName")
    @Expose
    private Object trayName;
    @SerializedName("employeeID")
    @Expose
    private Object employeeID;
    @SerializedName("employeeName")
    @Expose
    private Object employeeName;
    @SerializedName("employeeCode")
    @Expose
    private Object employeeCode;
    @SerializedName("viewedDate")
    @Expose
    private String viewedDate;

    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;

    @SerializedName("goldWeight")
    @Expose
    private String goldWeight;

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Object getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(Object customerCode) {
        this.customerCode = customerCode;
    }

    public Object getCustomerName() {
        return customerName;
    }

    public void setCustomerName(Object customerName) {
        this.customerName = customerName;
    }

    public Object getCustomerEmailID() {
        return customerEmailID;
    }

    public void setCustomerEmailID(Object customerEmailID) {
        this.customerEmailID = customerEmailID;
    }

    public Object getCustomerContactNo() {
        return customerContactNo;
    }

    public void setCustomerContactNo(Object customerContactNo) {
        this.customerContactNo = customerContactNo;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Object getTrayID() {
        return trayID;
    }

    public void setTrayID(Object trayID) {
        this.trayID = trayID;
    }

    public Object getTrayName() {
        return trayName;
    }

    public void setTrayName(Object trayName) {
        this.trayName = trayName;
    }

    public Object getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Object employeeID) {
        this.employeeID = employeeID;
    }

    public Object getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(Object employeeName) {
        this.employeeName = employeeName;
    }

    public Object getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(Object employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getViewedDate() {
        return viewedDate;
    }

    public void setViewedDate(String viewedDate) {
        this.viewedDate = viewedDate;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getGoldWeight() {
        return goldWeight;
    }

    public void setGoldWeight(String goldWeight) {
        this.goldWeight = goldWeight;
    }
}