package com.acube.jims.datalayer.models.Filter;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "FilterStore", indices = @Index(value = {"id"}, unique = true))

public class FilterStore {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NonNull
    private Integer id;
    @SerializedName("warehouseCode")
    @Expose
    private String warehouseCode;
    @SerializedName("warehouseName")
    @Expose
    private String warehouseName;
    @SerializedName("address1")
    @Expose
    @Ignore
    private Object address1;
    @SerializedName("address2")
    @Expose
    @Ignore
    private Object address2;
    @SerializedName("city")
    @Expose
    @Ignore
    private Object city;
    @SerializedName("state")
    @Expose
    @Ignore
    private Object state;
    @SerializedName("contactNumber")
    @Expose
    @Ignore
    private Object contactNumber;
    @SerializedName("contactEmail")
    @Expose
    @Ignore
    private Object contactEmail;
    @SerializedName("region")
    @Expose
    @Ignore
    private Object region;
    @SerializedName("regionID")
    @Expose
    private Integer regionID;
    @SerializedName("companyID")
    @Expose
    @Ignore
    private Object companyID;
    @SerializedName("companyKey")
    @Expose
    private Integer companyKey;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("createdBy")
    @Expose
    @Ignore
    private Object createdBy;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("modifiedBy")
    @Expose
    @Ignore
    private Object modifiedBy;
    @SerializedName("modifiedDate")
    @Expose
    @Ignore
    private Object modifiedDate;


    private boolean isselected;

    public boolean isSelected() {
        return isselected;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Object getAddress1() {
        return address1;
    }

    public void setAddress1(Object address1) {
        this.address1 = address1;
    }

    public Object getAddress2() {
        return address2;
    }

    public void setAddress2(Object address2) {
        this.address2 = address2;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public Object getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(Object contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Object getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(Object contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Object getRegion() {
        return region;
    }

    public void setRegion(Object region) {
        this.region = region;
    }

    public Integer getRegionID() {
        return regionID;
    }

    public void setRegionID(Integer regionID) {
        this.regionID = regionID;
    }

    public Object getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Object companyID) {
        this.companyID = companyID;
    }

    public Integer getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Integer companyKey) {
        this.companyKey = companyKey;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Object getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Object modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Object getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Object modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public boolean isIsselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }
}
