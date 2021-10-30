package com.acube.jims.datalayer.models.CustomerManagment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesHistory {
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
    @SerializedName("goldWeight")
    @Expose
    private Double goldWeight;
    @SerializedName("purchasePrice")
    @Expose
    private Double purchasePrice;
    @SerializedName("purchaseDate")
    @Expose
    private String purchaseDate;
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;

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

    public Double getGoldWeight() {
        return goldWeight;
    }

    public void setGoldWeight(Double goldWeight) {
        this.goldWeight = goldWeight;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
