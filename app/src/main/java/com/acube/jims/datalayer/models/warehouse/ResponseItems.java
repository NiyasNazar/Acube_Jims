package com.acube.jims.datalayer.models.warehouse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseItems {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("itemDesc")
    @Expose
    private String itemDesc;
    @SerializedName("itemCode")
    @Expose
    private String itemCode;
    @SerializedName("itemReqQty")
    @Expose
    private Object itemReqQty;
    @SerializedName("mrp")
    @Expose
    private Object mrp;
    @SerializedName("labourCharge")
    @Expose
    private Object labourCharge;
    @SerializedName("makingChargeMin")
    @Expose
    private Object makingChargeMin;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("makingChargeMax")
    @Expose
    private Object makingChargeMax;
    @SerializedName("categoryID")
    @Expose
    private Integer categoryID;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("subCategoryID")
    @Expose
    private Integer subCategoryID;
    @SerializedName("subCategoryName")
    @Expose
    private String subCategoryName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Object getItemReqQty() {
        return itemReqQty;
    }

    public void setItemReqQty(Object itemReqQty) {
        this.itemReqQty = itemReqQty;
    }

    public Object getMrp() {
        return mrp;
    }

    public void setMrp(Object mrp) {
        this.mrp = mrp;
    }

    public Object getLabourCharge() {
        return labourCharge;
    }

    public void setLabourCharge(Object labourCharge) {
        this.labourCharge = labourCharge;
    }

    public Object getMakingChargeMin() {
        return makingChargeMin;
    }

    public void setMakingChargeMin(Object makingChargeMin) {
        this.makingChargeMin = makingChargeMin;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getMakingChargeMax() {
        return makingChargeMax;
    }

    public void setMakingChargeMax(Object makingChargeMax) {
        this.makingChargeMax = makingChargeMax;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getSubCategoryID() {
        return subCategoryID;
    }

    public void setSubCategoryID(Integer subCategoryID) {
        this.subCategoryID = subCategoryID;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    @Override
    public String toString() {
        return itemName;
    }
}
