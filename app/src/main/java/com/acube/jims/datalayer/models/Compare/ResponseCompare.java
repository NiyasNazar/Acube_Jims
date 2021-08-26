package com.acube.jims.datalayer.models.Compare;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseCompare {
    @SerializedName("itemID")
    @Expose
    private Integer itemID;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    @SerializedName("imagePath")
    @Expose
    private String imagePath;
    @SerializedName("qty")
    @Expose
    private Object qty;
    @SerializedName("uomid")
    @Expose
    private Integer uomid;
    @SerializedName("grossWeight")
    @Expose
    private Double grossWeight;
    @SerializedName("stoneWeight")
    @Expose
    private Double stoneWeight;
    @SerializedName("karatCode")
    @Expose
    private Double karatCode;
    @SerializedName("karatName")
    @Expose
    private String karatName;
    @SerializedName("customerID")
    @Expose
    private Object customerID;
    @SerializedName("customerCode")
    @Expose
    private Object customerCode;
    @SerializedName("customerName")
    @Expose
    private Object customerName;
    @SerializedName("employeeID")
    @Expose
    private Object employeeID;
    @SerializedName("employeeCode")
    @Expose
    private Object employeeCode;
    @SerializedName("employeeName")
    @Expose
    private Object employeeName;
    @SerializedName("mrp")
    @Expose
    private Double mrp;

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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Object getQty() {
        return qty;
    }

    public void setQty(Object qty) {
        this.qty = qty;
    }

    public Integer getUomid() {
        return uomid;
    }

    public void setUomid(Integer uomid) {
        this.uomid = uomid;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double getStoneWeight() {
        return stoneWeight;
    }

    public void setStoneWeight(Double stoneWeight) {
        this.stoneWeight = stoneWeight;
    }

    public Double getKaratCode() {
        return karatCode;
    }

    public void setKaratCode(Double karatCode) {
        this.karatCode = karatCode;
    }

    public String getKaratName() {
        return karatName;
    }

    public void setKaratName(String karatName) {
        this.karatName = karatName;
    }

    public Object getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Object customerID) {
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

    public Object getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Object employeeID) {
        this.employeeID = employeeID;
    }

    public Object getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(Object employeeCode) {
        this.employeeCode = employeeCode;
    }

    public Object getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(Object employeeName) {
        this.employeeName = employeeName;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

}
