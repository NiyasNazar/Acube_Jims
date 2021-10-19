package com.acube.jims.datalayer.models.Audit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationMismatchApproved {

    @SerializedName("auditID")
    @Expose
    private String auditID;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("warehouseID")
    @Expose
    private Object warehouseID;
    @SerializedName("warehouseCode")
    @Expose
    private Object warehouseCode;
    @SerializedName("warehouseName")
    @Expose
    private Object warehouseName;
    @SerializedName("systemLocationID")
    @Expose
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
    private String scanLocationCode;
    @SerializedName("scanLocationName")
    @Expose
    private String scanLocationName;
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
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
    private Object createdBy;
    @SerializedName("createdDate")
    @Expose
    private Object createdDate;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("statusText")
    @Expose
    private String statusText;
    @SerializedName("itemID")
    @Expose
    private Object itemID;
    @SerializedName("itemCode")
    @Expose
    private String itemCode;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("scannedBy")
    @Expose
    private Object scannedBy;
    @SerializedName("scannedDate")
    @Expose
    private Object scannedDate;
    @SerializedName("candidateLocationID")
    @Expose
    private Object candidateLocationID;
    @SerializedName("candidateLocationCode")
    @Expose
    private Object candidateLocationCode;
    @SerializedName("candidateLocationName")
    @Expose
    private Object candidateLocationName;
    @SerializedName("modifiedBy")
    @Expose
    private Object modifiedBy;
    @SerializedName("modifiedDate")
    @Expose
    private Object modifiedDate;
    @SerializedName("toBeAuditedOn")
    @Expose
    private Object toBeAuditedOn;
    @SerializedName("companyID")
    @Expose
    private Object companyID;
    @SerializedName("lineStatus")
    @Expose
    private Integer lineStatus;
    @SerializedName("totalStock")
    @Expose
    private Object totalStock;
    @SerializedName("found")
    @Expose
    private Object found;
    @SerializedName("missing")
    @Expose
    private Object missing;
    @SerializedName("locationMismatch")
    @Expose
    private Object locationMismatch;
    @SerializedName("locationMismatchApproved")
    @Expose
    private Object locationMismatchApproved;
    @SerializedName("unknown")
    @Expose
    private Object unknown;

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

    public String getScanLocationCode() {
        return scanLocationCode;
    }

    public void setScanLocationCode(String scanLocationCode) {
        this.scanLocationCode = scanLocationCode;
    }

    public String getScanLocationName() {
        return scanLocationName;
    }

    public void setScanLocationName(String scanLocationName) {
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

}
