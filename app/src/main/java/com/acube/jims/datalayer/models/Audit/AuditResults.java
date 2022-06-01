package com.acube.jims.datalayer.models.Audit;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "InventoryAudit", indices = @Index(value = {"auditID","serialNumber"}, unique = true))

public class AuditResults {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @SerializedName("auditID")
    @Expose
    private String auditID;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("warehouseID")
    @Expose
    @Ignore
    private Object warehouseID;
    @SerializedName("warehouseCode")
    @Expose
    @Ignore
    private Object warehouseCode;
    @SerializedName("warehouseName")
    @Expose
    @Ignore
    private Object warehouseName;
    @SerializedName("systemLocationID")
    @Expose
    private int systemLocationID;
    @SerializedName("systemLocationCode")
    @Expose
    @Ignore
    private Object systemLocationCode;
    @SerializedName("systemLocationName")
    @Expose
    private String systemLocationName;
    @SerializedName("scanLocationID")
    @Expose
    private int scanLocationID;
    @SerializedName("scanLocationCode")
    @Expose
    @Ignore
    private Object scanLocationCode;
    @SerializedName("scanLocationName")
    @Expose
    @Ignore

    private Object scanLocationName;
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    @SerializedName("releasedBy")
    @Expose
    @Ignore

    private Object releasedBy;
    @SerializedName("releasedDate")
    @Expose
    @Ignore

    private Object releasedDate;
    @SerializedName("closedBy")
    @Expose
    @Ignore

    private Object closedBy;
    @SerializedName("auditStatusText")
    @Expose
    @Ignore

    private Object auditStatusText;
    @SerializedName("closingRemark")
    @Expose
    @Ignore

    private Object closingRemark;
    @SerializedName("closedDate")
    @Expose
    @Ignore
    private Object closedDate;
    @SerializedName("postedBy")
    @Expose
    @Ignore

    private Object postedBy;
    @SerializedName("postedDate")
    @Expose
    @Ignore

    private Object postedDate;
    @SerializedName("createdBy")
    @Expose
    @Ignore

    private Object createdBy;
    @SerializedName("createdDate")
    @Expose
    @Ignore

    private Object createdDate;
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("itemID")
    @Expose
    private Integer itemID;
    @SerializedName("itemCode")
    @Expose
    private String itemCode;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("scannedBy")
    @Expose
    private String scannedBy;
    @SerializedName("scannedDate")
    @Expose
    private String scannedDate;

    @SerializedName("toBeAuditedOn")
    @Expose
    private String toBeAuditedOn;

    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("subCategoryName")
    @Expose
    private String subCategoryName;
    @SerializedName("karatCode")
    @Expose
    @Ignore
    private Object karatCode;
    @SerializedName("categoryID")
    @Expose
    private Integer categoryID;
    @SerializedName("subCategoryID")
    @Expose
    private Integer subCategoryID;


    public String getAuditID() {
        return auditID;
    }

    public void setAuditID(String auditID) {
        this.auditID = auditID;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Object getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Object warehouseID) {
        this.warehouseID = warehouseID;
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

    public int getSystemLocationID() {
        return systemLocationID;
    }

    public void setSystemLocationID(int systemLocationID) {
        this.systemLocationID = systemLocationID;
    }

    public Object getSystemLocationCode() {
        return systemLocationCode;
    }

    public void setSystemLocationCode(Object systemLocationCode) {
        this.systemLocationCode = systemLocationCode;
    }

    public String getSystemLocationName() {
        return systemLocationName;
    }

    public void setSystemLocationName(String systemLocationName) {
        this.systemLocationName = systemLocationName;
    }

    public int getScanLocationID() {
        return scanLocationID;
    }

    public void setScanLocationID(int scanLocationID) {
        this.scanLocationID = scanLocationID;
    }

    public Object getScanLocationCode() {
        return scanLocationCode;
    }

    public void setScanLocationCode(Object scanLocationCode) {
        this.scanLocationCode = scanLocationCode;
    }

    public Object getScanLocationName() {
        return scanLocationName;
    }

    public void setScanLocationName(Object scanLocationName) {
        this.scanLocationName = scanLocationName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Object getReleasedBy() {
        return releasedBy;
    }

    public void setReleasedBy(Object releasedBy) {
        this.releasedBy = releasedBy;
    }

    public Object getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(Object releasedDate) {
        this.releasedDate = releasedDate;
    }

    public Object getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(Object closedBy) {
        this.closedBy = closedBy;
    }

    public Object getAuditStatusText() {
        return auditStatusText;
    }

    public void setAuditStatusText(Object auditStatusText) {
        this.auditStatusText = auditStatusText;
    }

    public Object getClosingRemark() {
        return closingRemark;
    }

    public void setClosingRemark(Object closingRemark) {
        this.closingRemark = closingRemark;
    }

    public Object getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Object closedDate) {
        this.closedDate = closedDate;
    }

    public Object getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(Object postedBy) {
        this.postedBy = postedBy;
    }

    public Object getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Object postedDate) {
        this.postedDate = postedDate;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Object getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Object createdDate) {
        this.createdDate = createdDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



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


    public String getToBeAuditedOn() {
        return toBeAuditedOn;
    }

    public void setToBeAuditedOn(String toBeAuditedOn) {
        this.toBeAuditedOn = toBeAuditedOn;
    }




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



    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public Integer getSubCategoryID() {
        return subCategoryID;
    }

    public void setSubCategoryID(Integer subCategoryID) {
        this.subCategoryID = subCategoryID;
    }




    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
