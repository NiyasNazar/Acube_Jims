package com.acube.jims.datalayer.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "SelectionHolder", indices = @Index(value = {"serialNumber"}, unique = true))

public class SelectionHolder {


    @SerializedName("imageUrl")
    @Expose

    private String imageUrl;
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
    @SerializedName("serialNumber")
    @Expose
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String serialNumber;
    @SerializedName("scannedLocationId")
    @Expose
    @Ignore
    private Object scannedLocationId;
    @SerializedName("warehouseId")
    @Expose
    private Integer warehouseId;
    @SerializedName("systemLocationName")
    @Expose
    @Ignore
    private Object systemLocationName;
    @SerializedName("systemLocationCode")
    @Expose
    @Ignore
    private Object systemLocationCode;
    @SerializedName("scannedLocationName")
    @Expose
    @Ignore
    private Object scannedLocationName;
    @SerializedName("scannedLocationCode")
    @Expose
    @Ignore
    private Object scannedLocationCode;
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
    @SerializedName("items")
    @Expose
    @Ignore
    private Object items;
    @SerializedName("subCategories")
    @Expose
    @Ignore
    private Object subCategories;
    @SerializedName("locations")
    @Expose
    @Ignore
    private Object locations;
    @SerializedName("stores")
    @Expose
    @Ignore
    private Object stores;
    @SerializedName("auditSnapShot")
    @Expose
    @Ignore
    private Object auditSnapShot;
    private int status;
    private int scanLocationID;
    private String Remarks;

    public String getImageUrl() {
        return imageUrl;
    }


    @SerializedName("imageName")
    @Expose
    private String imageName;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Object getScannedLocationId() {
        return scannedLocationId;
    }

    public void setScannedLocationId(Object scannedLocationId) {
        this.scannedLocationId = scannedLocationId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Object getSystemLocationName() {
        return systemLocationName;
    }

    public void setSystemLocationName(Object systemLocationName) {
        this.systemLocationName = systemLocationName;
    }

    public Object getSystemLocationCode() {
        return systemLocationCode;
    }

    public void setSystemLocationCode(Object systemLocationCode) {
        this.systemLocationCode = systemLocationCode;
    }

    public Object getScannedLocationName() {
        return scannedLocationName;
    }

    public void setScannedLocationName(Object scannedLocationName) {
        this.scannedLocationName = scannedLocationName;
    }

    public Object getScannedLocationCode() {
        return scannedLocationCode;
    }

    public void setScannedLocationCode(Object scannedLocationCode) {
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

    public Object getItems() {
        return items;
    }

    public void setItems(Object items) {
        this.items = items;
    }

    public Object getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(Object subCategories) {
        this.subCategories = subCategories;
    }

    public Object getLocations() {
        return locations;
    }

    public void setLocations(Object locations) {
        this.locations = locations;
    }

    public Object getStores() {
        return stores;
    }

    public void setStores(Object stores) {
        this.stores = stores;
    }

    public Object getAuditSnapShot() {
        return auditSnapShot;
    }

    public void setAuditSnapShot(Object auditSnapShot) {
        this.auditSnapShot = auditSnapShot;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
