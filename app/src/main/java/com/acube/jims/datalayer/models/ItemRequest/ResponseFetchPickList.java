package com.acube.jims.datalayer.models.ItemRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseFetchPickList {

    @SerializedName("itemRequestNo")
    @Expose
    private String itemRequestNo;
    @SerializedName("customerID")
    @Expose
    private Integer customerID;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("customerCode")
    @Expose
    private String customerCode;
    @SerializedName("contactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("employeeID")
    @Expose
    private Integer employeeID;
    @SerializedName("employeeName")
    @Expose
    private String employeeName;
    @SerializedName("employeeCode")
    @Expose
    private String employeeCode;
    @SerializedName("trayID")
    @Expose
    private Integer trayID;
    @SerializedName("deviceID")
    @Expose
    private Integer deviceID;
    @SerializedName("deviceName")
    @Expose
    private String deviceName;
    @SerializedName("macAddress")
    @Expose
    private String macAddress;
    @SerializedName("trayName")
    @Expose
    private String trayName;
    @SerializedName("trayMacAddress")
    @Expose
    private String trayMacAddress;

    public String getItemRequestNo() {
        return itemRequestNo;
    }

    public void setItemRequestNo(String itemRequestNo) {
        this.itemRequestNo = itemRequestNo;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public Integer getTrayID() {
        return trayID;
    }

    public void setTrayID(Integer trayID) {
        this.trayID = trayID;
    }

    public Integer getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Integer deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
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
}
