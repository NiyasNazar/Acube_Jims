package com.acube.jims.datalayer.models.Cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartDetail {
    @SerializedName("cartListNo")
    @Expose
    private String cartListNo;
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
    private Double qty;
    @SerializedName("trayID")
    @Expose
    private Object trayID;
    @SerializedName("trayName")
    @Expose
    private Object trayName;
    @SerializedName("uomid")
    @Expose
    private Object uomid;
    @SerializedName("discount")
    @Expose
    private Object discount;
    @SerializedName("vatPercentage")
    @Expose
    private Object vatPercentage;
    @SerializedName("vatAmount")
    @Expose
    private Object vatAmount;
    @SerializedName("grossAmount")
    @Expose
    private Object grossAmount;
    @SerializedName("netAmount")
    @Expose
    private Object netAmount;
    @SerializedName("finalAmount")
    @Expose
    private Object finalAmount;
    @SerializedName("grossWeight")
    @Expose
    private Double grossWeight;
    @SerializedName("stoneWeight")
    @Expose
    private Double stoneWeight;
    @SerializedName("cartSerialDTOs")
    @Expose
    private Object cartSerialDTOs;

    public String getCartListNo() {
        return cartListNo;
    }

    public void setCartListNo(String cartListNo) {
        this.cartListNo = cartListNo;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
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

    public Object getUomid() {
        return uomid;
    }

    public void setUomid(Object uomid) {
        this.uomid = uomid;
    }

    public Object getDiscount() {
        return discount;
    }

    public void setDiscount(Object discount) {
        this.discount = discount;
    }

    public Object getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(Object vatPercentage) {
        this.vatPercentage = vatPercentage;
    }

    public Object getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(Object vatAmount) {
        this.vatAmount = vatAmount;
    }

    public Object getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(Object grossAmount) {
        this.grossAmount = grossAmount;
    }

    public Object getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Object netAmount) {
        this.netAmount = netAmount;
    }

    public Object getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(Object finalAmount) {
        this.finalAmount = finalAmount;
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

    public Object getCartSerialDTOs() {
        return cartSerialDTOs;
    }

    public void setCartSerialDTOs(Object cartSerialDTOs) {
        this.cartSerialDTOs = cartSerialDTOs;
    }

}
