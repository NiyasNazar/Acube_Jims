package com.acube.jims.datalayer.models.Audit;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rscja.team.qcom.deviceapi.S;

@Entity(tableName = "AuditSnapShot", indices = @Index(value = {"serialNumber", "auditID"}, unique = true))

public class AuditSnapShot {
    @PrimaryKey(autoGenerate = true)
    private int ID;


    @SerializedName("weight")
    @Expose
    private Double weight;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("imageName")
    @Expose
    private String imageName;
    @SerializedName("auditID")
    @Expose
    private String auditID;
    @SerializedName("locationId")
    @Expose
    private Integer locationId;
    @SerializedName("itemId")
    @Expose
    private Integer itemId;
    @SerializedName("subCategoryId")
    @Expose
    private Integer subCategoryId;
    @SerializedName("categoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("categoryCode")
    @Expose
    private String categoryCode;
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    @SerializedName("scannedLocationId")
    @Expose
    private Integer scannedLocationId;
    @SerializedName("warehouseId")
    @Expose
    private Integer warehouseId;
    @SerializedName("systemLocationName")
    @Expose
    private String systemLocationName;
    @SerializedName("systemLocationCode")
    @Expose
    private String systemLocationCode;
    @SerializedName("scannedLocationName")
    @Expose
    private String scannedLocationName;
    @SerializedName("scannedLocationCode")
    @Expose
    private String scannedLocationCode;
    @SerializedName("warehouseCode")
    @Expose
    private String warehouseCode;
    @SerializedName("warehouseName")
    @Expose
    private String warehouseName;
    @SerializedName("locationCode")
    @Expose
    private String locationCode;
    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("itemCode")
    @Expose
    private String itemCode;
    @SerializedName("itemDesc")
    @Expose
    private String itemDesc;
    @SerializedName("subCategoryCode")
    @Expose
    private String subCategoryCode;
    @SerializedName("subCategoryName")
    @Expose
    private String subCategoryName;
    @SerializedName("toBeAuditedOn")
    @Expose
    private String toBeAuditedOn;
    @SerializedName("remark")
    @Expose
    private String remark;

    @SerializedName("categories")
    @Expose
    private String categories;
    @SerializedName("subCategories")
    @Expose
    private String subCategories;
    @SerializedName("locations")
    @Expose
    private String locations;
    @SerializedName("stores")
    @Expose
    private String stores;
    @SerializedName("auditSnapShot")
    @Expose
    private String auditSnapShot;

    private int status;
    private int scanLocationID;
    private String Remarks;

    private String scannedBy;
    private String scannedDate;

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getAuditID() {
        return auditID;
    }

    public void setAuditID(String auditID) {
        this.auditID = auditID;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getScannedLocationId() {
        return scannedLocationId;
    }

    public void setScannedLocationId(Integer scannedLocationId) {
        this.scannedLocationId = scannedLocationId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getSystemLocationName() {
        return systemLocationName;
    }

    public void setSystemLocationName(String systemLocationName) {
        this.systemLocationName = systemLocationName;
    }

    public String getSystemLocationCode() {
        return systemLocationCode;
    }

    public void setSystemLocationCode(String systemLocationCode) {
        this.systemLocationCode = systemLocationCode;
    }

    public String getScannedLocationName() {
        return scannedLocationName;
    }

    public void setScannedLocationName(String scannedLocationName) {
        this.scannedLocationName = scannedLocationName;
    }

    public String getScannedLocationCode() {
        return scannedLocationCode;
    }

    public void setScannedLocationCode(String scannedLocationCode) {
        this.scannedLocationCode = scannedLocationCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getSubCategoryCode() {
        return subCategoryCode;
    }

    public void setSubCategoryCode(String subCategoryCode) {
        this.subCategoryCode = subCategoryCode;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getToBeAuditedOn() {
        return toBeAuditedOn;
    }

    public void setToBeAuditedOn(String toBeAuditedOn) {
        this.toBeAuditedOn = toBeAuditedOn;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }




    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(String subCategories) {
        this.subCategories = subCategories;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getStores() {
        return stores;
    }

    public void setStores(String stores) {
        this.stores = stores;
    }

    public String getAuditSnapShot() {
        return auditSnapShot;
    }

    public void setAuditSnapShot(String auditSnapShot) {
        this.auditSnapShot = auditSnapShot;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getScanLocationID() {
        return scanLocationID;
    }

    public void setScanLocationID(int scanLocationID) {
        this.scanLocationID = scanLocationID;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getScannedBy() {
        return scannedBy;
    }

    public void setScannedBy(String scannedBy) {
        this.scannedBy = scannedBy;
    }

    public String getScannedDate() {
        return scannedDate;
    }

    public void setScannedDate(String scannedDate) {
        this.scannedDate = scannedDate;
    }

}
