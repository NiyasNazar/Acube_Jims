package com.acube.jims.datalayer.models.Favorites;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ResponseFavorites {
    @SerializedName("itemID")
    @Expose
    private Integer itemID;
    @SerializedName("itemName")
    @Expose

    private String itemName;
    @SerializedName("imagePath")
    @Expose

    private String imagePath;
    @SerializedName("qty")
    @Expose

    private Object qty;
    @SerializedName("uomid")
    @Expose

    private Object uomid;
    @SerializedName("grossWeight")
    @Expose

    private Double  grossWeight;
    @SerializedName("stoneWeight")
    @Expose

    private Double  stoneWeight;
    @SerializedName("karatCode")
    @Expose

    private Double  karatCode;
    @SerializedName("karatName")
    @Expose

    private String karatName;
    @SerializedName("customerID")
    @Expose

    private Integer customerID;
    @SerializedName("customerCode")
    @Expose

    private String customerCode;
    @SerializedName("customerName")
    @Expose

    private String customerName;
    @SerializedName("employeeID")
    @Expose

    private Integer employeeID;
    @SerializedName("employeeCode")
    @Expose

    private String employeeCode;
    @SerializedName("employeeName")
    @Expose

    private String employeeName;

    @SerializedName("serialNumber")
    @Expose

    private String serialNumber;


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

    public Object getUomid() {
        return uomid;
    }

    public void setUomid(Object uomid) {
        this.uomid = uomid;
    }

    public Double  getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double  grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double  getStoneWeight() {
        return stoneWeight;
    }

    public void setStoneWeight(Double  stoneWeight) {
        this.stoneWeight = stoneWeight;
    }

    public Double  getKaratCode() {
        return karatCode;
    }

    public void setKaratCode(Double  karatCode) {
        this.karatCode = karatCode;
    }

    public String getKaratName() {
        return karatName;
    }

    public void setKaratName(String karatName) {
        this.karatName = karatName;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
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

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
