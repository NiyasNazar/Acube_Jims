package com.acube.jims.datalayer.models.Compare;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rscja.team.qcom.deviceapi.S;

public class ResponseCompare {
    @SerializedName("makingChargeMax")
    @Expose
    private Double makingChargeMax;
    @SerializedName("makingChargeMin")
    @Expose
    private Double makingChargeMin;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("subCategoryName")
    @Expose
    private String subCategoryName;
    @SerializedName("itemFamily")
    @Expose
    private String itemFamily;
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
    private String uomName;
    @SerializedName("gender")
    @Expose
    private String gender;
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
    private Object karatCode;
    @SerializedName("karatName")
    @Expose
    private Object karatName;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getItemFamily() {
        return itemFamily;
    }

    public void setItemFamily(String itemFamily) {
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

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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

    public Double getMakingChargeMin() {
        return makingChargeMin;
    }

    public void setMakingChargeMin(Double makingChargeMin) {
        this.makingChargeMin = makingChargeMin;
    }

    public Double getMakingChargeMax() {
        return makingChargeMax;
    }

    public void setMakingChargeMax(Double makingChargeMax) {
        this.makingChargeMax = makingChargeMax;
    }
}
