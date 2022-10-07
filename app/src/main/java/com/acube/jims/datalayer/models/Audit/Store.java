package com.acube.jims.datalayer.models.Audit;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Store", indices = @Index(value = {"warehouseId"}, unique = true))

public class Store {
    @PrimaryKey(autoGenerate = true)
    private int ID;
    @SerializedName("imageUrl")
    @Expose
    @Ignore
    private Object imageUrl;
    @SerializedName("auditID")
    @Expose
    @Ignore
    private Object auditID;
    @SerializedName("locationId")
    @Expose
    @Ignore
    private Object locationId;
    @SerializedName("itemId")
    @Expose
    @Ignore
    private Object itemId;
    @SerializedName("subCategoryId")
    @Expose
    @Ignore
    private Object subCategoryId;
    @SerializedName("serialNumber")
    @Expose
    @Ignore
    private Object serialNumber;
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
    @Ignore
    private Object locationCode;
    @SerializedName("locationName")
    @Expose
    @Ignore
    private Object locationName;
    @SerializedName("itemCode")
    @Expose
    @Ignore
    private Object itemCode;
    @SerializedName("itemDesc")
    @Expose
    @Ignore
    private Object itemDesc;
    @SerializedName("subCategoryCode")
    @Expose
    @Ignore
    private Object subCategoryCode;
    @SerializedName("subCategoryName")
    @Expose
    @Ignore
    private Object subCategoryName;
    @SerializedName("toBeAuditedOn")
    @Expose
    @Ignore
    private Object toBeAuditedOn;
    @SerializedName("remark")
    @Expose
    @Ignore
    private Object remark;
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

    public Object getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Object imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getAuditID() {
        return auditID;
    }

    public void setAuditID(Object auditID) {
        this.auditID = auditID;
    }

    public Object getLocationId() {
        return locationId;
    }

    public void setLocationId(Object locationId) {
        this.locationId = locationId;
    }

    public Object getItemId() {
        return itemId;
    }

    public void setItemId(Object itemId) {
        this.itemId = itemId;
    }

    public Object getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Object subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Object getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Object serialNumber) {
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

    public Object getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(Object locationCode) {
        this.locationCode = locationCode;
    }

    public Object getLocationName() {
        return locationName;
    }

    public void setLocationName(Object locationName) {
        this.locationName = locationName;
    }

    public Object getItemCode() {
        return itemCode;
    }

    public void setItemCode(Object itemCode) {
        this.itemCode = itemCode;
    }

    public Object getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(Object itemDesc) {
        this.itemDesc = itemDesc;
    }

    public Object getSubCategoryCode() {
        return subCategoryCode;
    }

    public void setSubCategoryCode(Object subCategoryCode) {
        this.subCategoryCode = subCategoryCode;
    }

    public Object getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(Object subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public Object getToBeAuditedOn() {
        return toBeAuditedOn;
    }

    public void setToBeAuditedOn(Object toBeAuditedOn) {
        this.toBeAuditedOn = toBeAuditedOn;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return warehouseName;
    }
}
