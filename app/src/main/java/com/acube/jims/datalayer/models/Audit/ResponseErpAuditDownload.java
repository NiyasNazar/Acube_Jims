package com.acube.jims.datalayer.models.Audit;

import android.location.Location;

import com.acube.jims.datalayer.models.Filter.SubCategory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseErpAuditDownload {
    @SerializedName("imageUrl")
    @Expose
    private Object imageUrl;
    @SerializedName("auditID")
    @Expose
    private String auditID;
    @SerializedName("locationId")
    @Expose
    private Object locationId;
    @SerializedName("itemId")
    @Expose
    private Object itemId;
    @SerializedName("subCategoryId")
    @Expose
    private Object subCategoryId;
    @SerializedName("serialNumber")
    @Expose
    private Object serialNumber;
    @SerializedName("scannedLocationId")
    @Expose
    private Object scannedLocationId;
    @SerializedName("warehouseId")
    @Expose
    private Object warehouseId;
    @SerializedName("systemLocationName")
    @Expose
    private Object systemLocationName;
    @SerializedName("systemLocationCode")
    @Expose
    private Object systemLocationCode;
    @SerializedName("scannedLocationName")
    @Expose
    private Object scannedLocationName;
    @SerializedName("scannedLocationCode")
    @Expose
    private Object scannedLocationCode;
    @SerializedName("warehouseCode")
    @Expose
    private Object warehouseCode;
    @SerializedName("warehouseName")
    @Expose
    private Object warehouseName;
    @SerializedName("locationCode")
    @Expose
    private Object locationCode;
    @SerializedName("locationName")
    @Expose
    private Object locationName;
    @SerializedName("itemCode")
    @Expose
    private Object itemCode;
    @SerializedName("itemDesc")
    @Expose
    private Object itemDesc;
    @SerializedName("subCategoryCode")
    @Expose
    private Object subCategoryCode;
    @SerializedName("subCategoryName")
    @Expose
    private Object subCategoryName;
    @SerializedName("toBeAuditedOn")
    @Expose
    private String toBeAuditedOn;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("items")
    @Expose
    private List<AuditItem> items = null;
    @SerializedName("subCategories")
    @Expose
    private List<AuditSubCategory> subCategories = null;
    @SerializedName("locations")
    @Expose
    private List<AuditLocation> locations = null;
    @SerializedName("stores")
    @Expose
    private List<Store> stores = null;
    @SerializedName("auditSnapShot")
    @Expose
    private List<AuditSnapShot> auditSnapShot = null;

    public Object getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Object imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuditID() {
        return auditID;
    }

    public void setAuditID(String auditID) {
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

    public Object getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Object warehouseId) {
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

    public Object getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(Object warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public Object getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(Object warehouseName) {
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

    public List<AuditItem> getItems() {
        return items;
    }

    public void setItems(List<AuditItem> items) {
        this.items = items;
    }

    public List<AuditSubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<AuditSubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public List<AuditLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<AuditLocation> locations) {
        this.locations = locations;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public List<AuditSnapShot> getAuditSnapShot() {
        return auditSnapShot;
    }

    public void setAuditSnapShot(List<AuditSnapShot> auditSnapShot) {
        this.auditSnapShot = auditSnapShot;
    }
}
