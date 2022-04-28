package com.acube.jims.presentation.ScanItems;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ResponseItems", indices = @Index(value = {"serialNumber"}, unique = true))
public class ResponseItems {
    @ColumnInfo
    @SerializedName("itemID")
    @Expose
    private Integer itemID;
    @SerializedName("itemName")
    @Expose
    @ColumnInfo
    private String itemName;
    @ColumnInfo
    private String Status;
    @SerializedName("serialNumber")
    @Expose

    @PrimaryKey
    @NonNull
    private String serialNumber;
    @SerializedName("imagePath")
    @Expose
    @ColumnInfo
    private String imagePath;
    @SerializedName("qty")
    @Expose
    @ColumnInfo
    private int qty;
    @SerializedName("uomid")
    @Expose
    @ColumnInfo
    private Integer uomid;
    @SerializedName("grossWeight")
    @Expose
    @ColumnInfo
    private Double grossWeight;
    @SerializedName("stoneWeight")
    @Expose
    @ColumnInfo
    private Double stoneWeight;
    @SerializedName("karatCode")
    @Expose
    @ColumnInfo
    private Double karatCode;
    @SerializedName("karatName")
    @Expose
    @ColumnInfo
    private String karatName;
    @SerializedName("customerID")
    @Expose
    @ColumnInfo
    private int customerID;


    @SerializedName("mrp")
    @Expose
    @ColumnInfo
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

    @NonNull
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(@NonNull String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
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

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }


    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "ResponseItems{" +
                "itemID=" + itemID +
                ", itemName='" + itemName + '\'' +
                ", Status='" + Status + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", qty=" + qty +
                ", uomid=" + uomid +
                ", grossWeight=" + grossWeight +
                ", stoneWeight=" + stoneWeight +
                ", karatCode=" + karatCode +
                ", karatName='" + karatName + '\'' +
                ", customerID=" + customerID +
                ", mrp=" + mrp +
                '}';
    }
}
