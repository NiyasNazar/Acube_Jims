package com.acube.jims.datalayer.models.Favorites;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ResponseFavorites {
    @SerializedName("makingChargeMax")
    @Expose
    private Object makingChargeMax;
    @SerializedName("makingChargeMin")
    @Expose
    private Object makingChargeMin;
    @SerializedName("labourCharge")
    @Expose
    private Object labourCharge;
    @SerializedName("categoryName")
    @Expose
    private Object categoryName;
    @SerializedName("subCategoryName")
    @Expose
    private Object subCategoryName;
    @SerializedName("itemFamily")
    @Expose
    private Object itemFamily;
    @SerializedName("itemGroup")
    @Expose
    private Object itemGroup;
    @SerializedName("itemBrand")
    @Expose
    private Object itemBrand;
    @SerializedName("itemCollection")
    @Expose
    private Object itemCollection;
    @SerializedName("color")
    @Expose
    private Object color;
    @SerializedName("uomName")
    @Expose
    private Object uomName;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("itemDesc")
    @Expose
    private String itemDesc;
    @SerializedName("itemCode")
    @Expose
    private String itemCode;
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
    private Object imagePath;
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
    private Object karatCode;
    @SerializedName("karatName")
    @Expose
    private Object karatName;
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
    @SerializedName("mrp")
    @Expose
    private Double mrp;

    public Object getMakingChargeMax() {
        return makingChargeMax;
    }

    public void setMakingChargeMax(Object makingChargeMax) {
        this.makingChargeMax = makingChargeMax;
    }

    public Object getMakingChargeMin() {
        return makingChargeMin;
    }

    public void setMakingChargeMin(Object makingChargeMin) {
        this.makingChargeMin = makingChargeMin;
    }

    public Object getLabourCharge() {
        return labourCharge;
    }

    public void setLabourCharge(Object labourCharge) {
        this.labourCharge = labourCharge;
    }

    public Object getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(Object categoryName) {
        this.categoryName = categoryName;
    }

    public Object getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(Object subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public Object getItemFamily() {
        return itemFamily;
    }

    public void setItemFamily(Object itemFamily) {
        this.itemFamily = itemFamily;
    }

    public Object getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(Object itemGroup) {
        this.itemGroup = itemGroup;
    }

    public Object getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(Object itemBrand) {
        this.itemBrand = itemBrand;
    }

    public Object getItemCollection() {
        return itemCollection;
    }

    public void setItemCollection(Object itemCollection) {
        this.itemCollection = itemCollection;
    }

    public Object getColor() {
        return color;
    }

    public void setColor(Object color) {
        this.color = color;
    }

    public Object getUomName() {
        return uomName;
    }

    public void setUomName(Object uomName) {
        this.uomName = uomName;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
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

    public Object getImagePath() {
        return imagePath;
    }

    public void setImagePath(Object imagePath) {
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

    public Object getKaratCode() {
        return karatCode;
    }

    public void setKaratCode(Object karatCode) {
        this.karatCode = karatCode;
    }

    public Object getKaratName() {
        return karatName;
    }

    public void setKaratName(Object karatName) {
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

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }
}

