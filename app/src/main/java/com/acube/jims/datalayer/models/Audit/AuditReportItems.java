package com.acube.jims.datalayer.models.Audit;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuditReportItems {
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
    @Ignore
    private Integer systemLocationID;
    @SerializedName("systemLocationCode")
    @Expose
    private String systemLocationCode;
    @SerializedName("systemLocationName")
    @Expose
    private String systemLocationName;
    @SerializedName("scanLocationID")
    @Expose
    private Integer scanLocationID;
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
    private Integer status;
    @SerializedName("statusText")
    @Expose
    private String statusText;
    @SerializedName("itemID")
    @Expose
    @Ignore
    private Object itemID;
    @SerializedName("itemCode")
    @Expose
    private String itemCode;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("scannedBy")
    @Expose
    @Ignore
    private Object scannedBy;
    @SerializedName("scannedDate")
    @Expose
    @Ignore
    private Object scannedDate;
    @SerializedName("candidateLocationID")
    @Expose
    @Ignore
    private Object candidateLocationID;
    @SerializedName("candidateLocationCode")
    @Expose
    @Ignore
    private Object candidateLocationCode;
    @SerializedName("candidateLocationName")
    @Expose
    @Ignore
    private Object candidateLocationName;
    @SerializedName("modifiedBy")
    @Expose
    @Ignore
    private Object modifiedBy;
    @SerializedName("modifiedDate")
    @Expose
    @Ignore
    private Object modifiedDate;
    @SerializedName("toBeAuditedOn")
    @Expose
    @Ignore
    private Object toBeAuditedOn;
    @SerializedName("companyID")
    @Expose
    @Ignore
    private Object companyID;
    @SerializedName("lineStatus")
    @Expose
    private Integer lineStatus;
    @SerializedName("totalStock")
    @Expose
    @Ignore
    private Object totalStock;
    @SerializedName("found")
    @Expose
    @Ignore
    private Object found;
    @SerializedName("missing")
    @Expose
    @Ignore
    private Object missing;
    @SerializedName("locationMismatch")
    @Expose
    @Ignore
    private Object locationMismatch;
    @SerializedName("locationMismatchApproved")
    @Expose
    @Ignore
    private Object locationMismatchApproved;
    @SerializedName("unknown")
    @Expose
    @Ignore
    private Object unknown;
    @SerializedName("itemImagePath")
    @Expose
    private String itemImagePath;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("subCategoryName")
    @Expose
    private String subCategoryName;
    @SerializedName("karatCode")
    @Expose
    private Double karatCode;
    @SerializedName("categoryID")
    @Expose
    private Integer categoryID;
    @SerializedName("subCategoryID")
    @Expose
    private Integer subCategoryID;
    @SerializedName("karatID")
    @Expose
    private Integer karatID;

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

    public Integer getSystemLocationID() {
        return systemLocationID;
    }

    public void setSystemLocationID(Integer systemLocationID) {
        this.systemLocationID = systemLocationID;
    }

    public String getSystemLocationCode() {
        return systemLocationCode;
    }

    public void setSystemLocationCode(String systemLocationCode) {
        this.systemLocationCode = systemLocationCode;
    }

    public String getSystemLocationName() {
        return systemLocationName;
    }

    public void setSystemLocationName(String systemLocationName) {
        this.systemLocationName = systemLocationName;
    }

    public Integer getScanLocationID() {
        return scanLocationID;
    }

    public void setScanLocationID(Integer scanLocationID) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Object getItemID() {
        return itemID;
    }

    public void setItemID(Object itemID) {
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

    public Object getScannedBy() {
        return scannedBy;
    }

    public void setScannedBy(Object scannedBy) {
        this.scannedBy = scannedBy;
    }

    public Object getScannedDate() {
        return scannedDate;
    }

    public void setScannedDate(Object scannedDate) {
        this.scannedDate = scannedDate;
    }

    public Object getCandidateLocationID() {
        return candidateLocationID;
    }

    public void setCandidateLocationID(Object candidateLocationID) {
        this.candidateLocationID = candidateLocationID;
    }

    public Object getCandidateLocationCode() {
        return candidateLocationCode;
    }

    public void setCandidateLocationCode(Object candidateLocationCode) {
        this.candidateLocationCode = candidateLocationCode;
    }

    public Object getCandidateLocationName() {
        return candidateLocationName;
    }

    public void setCandidateLocationName(Object candidateLocationName) {
        this.candidateLocationName = candidateLocationName;
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

    public Object getToBeAuditedOn() {
        return toBeAuditedOn;
    }

    public void setToBeAuditedOn(Object toBeAuditedOn) {
        this.toBeAuditedOn = toBeAuditedOn;
    }

    public Object getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Object companyID) {
        this.companyID = companyID;
    }

    public Integer getLineStatus() {
        return lineStatus;
    }

    public void setLineStatus(Integer lineStatus) {
        this.lineStatus = lineStatus;
    }

    public Object getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Object totalStock) {
        this.totalStock = totalStock;
    }

    public Object getFound() {
        return found;
    }

    public void setFound(Object found) {
        this.found = found;
    }

    public Object getMissing() {
        return missing;
    }

    public void setMissing(Object missing) {
        this.missing = missing;
    }

    public Object getLocationMismatch() {
        return locationMismatch;
    }

    public void setLocationMismatch(Object locationMismatch) {
        this.locationMismatch = locationMismatch;
    }

    public Object getLocationMismatchApproved() {
        return locationMismatchApproved;
    }

    public void setLocationMismatchApproved(Object locationMismatchApproved) {
        this.locationMismatchApproved = locationMismatchApproved;
    }

    public Object getUnknown() {
        return unknown;
    }

    public void setUnknown(Object unknown) {
        this.unknown = unknown;
    }

    public String getItemImagePath() {
        return itemImagePath;
    }

    public void setItemImagePath(String itemImagePath) {
        this.itemImagePath = itemImagePath;
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

    public Double getKaratCode() {
        return karatCode;
    }

    public void setKaratCode(Double karatCode) {
        this.karatCode = karatCode;
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

    public Integer getKaratID() {
        return karatID;
    }

    public void setKaratID(Integer karatID) {
        this.karatID = karatID;
    }
}
