package com.acube.jims.datalayer.models.Audit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuditScanUpload {
    @SerializedName("auditID")
    @Expose
    private String auditID;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("warehouseID")
    @Expose
    private Integer warehouseID;
    @SerializedName("warehouseCode")
    @Expose
    private String warehouseCode;
    @SerializedName("warehouseName")
    @Expose
    private String warehouseName;
    @SerializedName("systemLocationID")
    @Expose
    private Object systemLocationID;
    @SerializedName("systemLocationCode")
    @Expose
    private Object systemLocationCode;
    @SerializedName("systemLocationName")
    @Expose
    private Object systemLocationName;
    @SerializedName("scanLocationID")
    @Expose
    private Object scanLocationID;
    @SerializedName("scanLocationCode")
    @Expose
    private Object scanLocationCode;
    @SerializedName("scanLocationName")
    @Expose
    private Object scanLocationName;
    @SerializedName("serialNumber")
    @Expose
    private Object serialNumber;
    @SerializedName("releasedBy")
    @Expose
    private Object releasedBy;
    @SerializedName("releasedDate")
    @Expose
    private Object releasedDate;
    @SerializedName("closedBy")
    @Expose
    private Object closedBy;
    @SerializedName("auditStatusText")
    @Expose
    private Object auditStatusText;
    @SerializedName("closingRemark")
    @Expose
    private Object closingRemark;
    @SerializedName("closedDate")
    @Expose
    private Object closedDate;
    @SerializedName("postedBy")
    @Expose
    private Object postedBy;
    @SerializedName("postedDate")
    @Expose
    private Object postedDate;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("statusText")
    @Expose
    private Object statusText;
    @SerializedName("itemID")
    @Expose
    private Object itemID;
    @SerializedName("itemCode")
    @Expose
    private Object itemCode;
    @SerializedName("itemName")
    @Expose
    private Object itemName;
    @SerializedName("scannedBy")
    @Expose
    private Object scannedBy;
    @SerializedName("scannedDate")
    @Expose
    private Object scannedDate;
    @SerializedName("candidateLocationID")
    @Expose
    private Integer candidateLocationID;
    @SerializedName("candidateLocationCode")
    @Expose
    private String candidateLocationCode;
    @SerializedName("candidateLocationName")
    @Expose
    private String candidateLocationName;
    @SerializedName("modifiedBy")
    @Expose
    private String modifiedBy;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("toBeAuditedOn")
    @Expose
    private String toBeAuditedOn;
    @SerializedName("companyID")
    @Expose
    private String companyID;
    @SerializedName("lineStatus")
    @Expose
    private Object lineStatus;
    @SerializedName("totalStock")
    @Expose
    private Integer totalStock;
    @SerializedName("found")
    @Expose
    private Integer found;
    @SerializedName("missing")
    @Expose
    private Integer missing;
    @SerializedName("locationMismatch")
    @Expose
    private Integer locationMismatch;

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

    public Integer getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Integer warehouseID) {
        this.warehouseID = warehouseID;
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

    public Object getSystemLocationID() {
        return systemLocationID;
    }

    public void setSystemLocationID(Object systemLocationID) {
        this.systemLocationID = systemLocationID;
    }

    public Object getSystemLocationCode() {
        return systemLocationCode;
    }

    public void setSystemLocationCode(Object systemLocationCode) {
        this.systemLocationCode = systemLocationCode;
    }

    public Object getSystemLocationName() {
        return systemLocationName;
    }

    public void setSystemLocationName(Object systemLocationName) {
        this.systemLocationName = systemLocationName;
    }

    public Object getScanLocationID() {
        return scanLocationID;
    }

    public void setScanLocationID(Object scanLocationID) {
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

    public Object getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Object serialNumber) {
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getStatusText() {
        return statusText;
    }

    public void setStatusText(Object statusText) {
        this.statusText = statusText;
    }

    public Object getItemID() {
        return itemID;
    }

    public void setItemID(Object itemID) {
        this.itemID = itemID;
    }

    public Object getItemCode() {
        return itemCode;
    }

    public void setItemCode(Object itemCode) {
        this.itemCode = itemCode;
    }

    public Object getItemName() {
        return itemName;
    }

    public void setItemName(Object itemName) {
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

    public Integer getCandidateLocationID() {
        return candidateLocationID;
    }

    public void setCandidateLocationID(Integer candidateLocationID) {
        this.candidateLocationID = candidateLocationID;
    }

    public String getCandidateLocationCode() {
        return candidateLocationCode;
    }

    public void setCandidateLocationCode(String candidateLocationCode) {
        this.candidateLocationCode = candidateLocationCode;
    }

    public String getCandidateLocationName() {
        return candidateLocationName;
    }

    public void setCandidateLocationName(String candidateLocationName) {
        this.candidateLocationName = candidateLocationName;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getToBeAuditedOn() {
        return toBeAuditedOn;
    }

    public void setToBeAuditedOn(String toBeAuditedOn) {
        this.toBeAuditedOn = toBeAuditedOn;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public Object getLineStatus() {
        return lineStatus;
    }

    public void setLineStatus(Object lineStatus) {
        this.lineStatus = lineStatus;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }

    public Integer getFound() {
        return found;
    }

    public void setFound(Integer found) {
        this.found = found;
    }

    public Integer getMissing() {
        return missing;
    }

    public void setMissing(Integer missing) {
        this.missing = missing;
    }

    public Integer getLocationMismatch() {
        return locationMismatch;
    }

    public void setLocationMismatch(Integer locationMismatch) {
        this.locationMismatch = locationMismatch;
    }
}

