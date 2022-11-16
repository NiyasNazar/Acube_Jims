package com.acube.jims.datalayer.models.missingcomp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuditResult {
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("weight")
    @Expose
    private Double weight;
    @SerializedName("categoryMissing")
    @Expose
    private Object categoryMissing;
    @SerializedName("lineStatusText")
    @Expose
    private Object lineStatusText;
    @SerializedName("warehouseId")
    @Expose
    private Object warehouseId;
    @SerializedName("locationId")
    @Expose
    private Object locationId;
    @SerializedName("itemId")
    @Expose
    private Object itemId;
    @SerializedName("subCategoryId")
    @Expose
    private Object subCategoryId;
    @SerializedName("itemFamilyId")
    @Expose
    private Object itemFamilyId;
    @SerializedName("assignedLocationId")
    @Expose
    private Object assignedLocationId;
    @SerializedName("assignedWarehouseId")
    @Expose
    private Object assignedWarehouseId;
    @SerializedName("assignedItemId")
    @Expose
    private Object assignedItemId;
    @SerializedName("assignedSubCategoryId")
    @Expose
    private Object assignedSubCategoryId;
    @SerializedName("assignedItemFamilyId")
    @Expose
    private Object assignedItemFamilyId;
    @SerializedName("warehouseName")
    @Expose
    private String warehouseName;
    @SerializedName("warehouseCode")
    @Expose
    private Object warehouseCode;
    @SerializedName("locationCode")
    @Expose
    private Object locationCode;
    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("itemDesc")
    @Expose
    private String itemDesc;
    @SerializedName("subCategoryName")
    @Expose
    private String subCategoryName;
    @SerializedName("subCategoryCode")
    @Expose
    private Object subCategoryCode;
    @SerializedName("categoryName")
    @Expose
    private Object categoryName;
    @SerializedName("categoryCode")
    @Expose
    private Object categoryCode;
    @SerializedName("itemFamilyName")
    @Expose
    private Object itemFamilyName;
    @SerializedName("auditID")
    @Expose
    private String auditID;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("branch")
    @Expose
    private Object branch;
    @SerializedName("systemBranch")
    @Expose
    private Object systemBranch;
    @SerializedName("scanBranch")
    @Expose
    private Object scanBranch;
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
    private String itemCode;
    @SerializedName("itemName")
    @Expose
    private Object itemName;
    @SerializedName("scannedBy")
    @Expose
    private String scannedBy;
    @SerializedName("scannedDate")
    @Expose
    private String scannedDate;
    @SerializedName("candidateLocationID")
    @Expose
    private Object candidateLocationID;
    @SerializedName("candidateLocationCode")
    @Expose
    private Object candidateLocationCode;
    @SerializedName("candidateLocationName")
    @Expose
    private Object candidateLocationName;
    @SerializedName("scannedLocationName")
    @Expose
    private Object scannedLocationName;
    @SerializedName("assignedZones")
    @Expose
    private Object assignedZones;
    @SerializedName("modifiedBy")
    @Expose
    private Object modifiedBy;
    @SerializedName("modifiedDate")
    @Expose
    private Object modifiedDate;
    @SerializedName("toBeAuditedOn")
    @Expose
    private String toBeAuditedOn;
    @SerializedName("companyID")
    @Expose
    private Object companyID;
    @SerializedName("lineStatus")
    @Expose
    private Object lineStatus;
    @SerializedName("totalStock")
    @Expose
    private Object totalStock;
    @SerializedName("totalScanned")
    @Expose
    private Object totalScanned;
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
    @SerializedName("category")
    @Expose
    private Object category;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("itemType")
    @Expose
    private Object itemType;
    @SerializedName("uom")
    @Expose
    private Object uom;
    @SerializedName("barcode")
    @Expose
    private Object barcode;
    @SerializedName("scanDate")
    @Expose
    private Object scanDate;
    @SerializedName("subCode")
    @Expose
    private Object subCode;
    @SerializedName("entryType")
    @Expose
    private Object entryType;
    @SerializedName("qty")
    @Expose
    private Object qty;
    @SerializedName("assignedCount")
    @Expose
    private Object assignedCount;
    @SerializedName("imageName")
    @Expose
    private Object imageName;
    @SerializedName("auditCandidates")
    @Expose
    private Object auditCandidates;
    @SerializedName("auditItemCandidates")
    @Expose
    private Object auditItemCandidates;
    @SerializedName("auditItemFamily")
    @Expose
    private Object auditItemFamily;
    @SerializedName("auditCategories")
    @Expose
    private Object auditCategories;
    @SerializedName("auditStores")
    @Expose
    private Object auditStores;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Object getCategoryMissing() {
        return categoryMissing;
    }

    public void setCategoryMissing(Object categoryMissing) {
        this.categoryMissing = categoryMissing;
    }

    public Object getLineStatusText() {
        return lineStatusText;
    }

    public void setLineStatusText(Object lineStatusText) {
        this.lineStatusText = lineStatusText;
    }

    public Object getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Object warehouseId) {
        this.warehouseId = warehouseId;
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

    public Object getItemFamilyId() {
        return itemFamilyId;
    }

    public void setItemFamilyId(Object itemFamilyId) {
        this.itemFamilyId = itemFamilyId;
    }

    public Object getAssignedLocationId() {
        return assignedLocationId;
    }

    public void setAssignedLocationId(Object assignedLocationId) {
        this.assignedLocationId = assignedLocationId;
    }

    public Object getAssignedWarehouseId() {
        return assignedWarehouseId;
    }

    public void setAssignedWarehouseId(Object assignedWarehouseId) {
        this.assignedWarehouseId = assignedWarehouseId;
    }

    public Object getAssignedItemId() {
        return assignedItemId;
    }

    public void setAssignedItemId(Object assignedItemId) {
        this.assignedItemId = assignedItemId;
    }

    public Object getAssignedSubCategoryId() {
        return assignedSubCategoryId;
    }

    public void setAssignedSubCategoryId(Object assignedSubCategoryId) {
        this.assignedSubCategoryId = assignedSubCategoryId;
    }

    public Object getAssignedItemFamilyId() {
        return assignedItemFamilyId;
    }

    public void setAssignedItemFamilyId(Object assignedItemFamilyId) {
        this.assignedItemFamilyId = assignedItemFamilyId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Object getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(Object warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public Object getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(Object locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public Object getSubCategoryCode() {
        return subCategoryCode;
    }

    public void setSubCategoryCode(Object subCategoryCode) {
        this.subCategoryCode = subCategoryCode;
    }

    public Object getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(Object categoryName) {
        this.categoryName = categoryName;
    }

    public Object getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(Object categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Object getItemFamilyName() {
        return itemFamilyName;
    }

    public void setItemFamilyName(Object itemFamilyName) {
        this.itemFamilyName = itemFamilyName;
    }

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

    public Object getBranch() {
        return branch;
    }

    public void setBranch(Object branch) {
        this.branch = branch;
    }

    public Object getSystemBranch() {
        return systemBranch;
    }

    public void setSystemBranch(Object systemBranch) {
        this.systemBranch = systemBranch;
    }

    public Object getScanBranch() {
        return scanBranch;
    }

    public void setScanBranch(Object scanBranch) {
        this.scanBranch = scanBranch;
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

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Object getItemName() {
        return itemName;
    }

    public void setItemName(Object itemName) {
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

    public Object getScannedLocationName() {
        return scannedLocationName;
    }

    public void setScannedLocationName(Object scannedLocationName) {
        this.scannedLocationName = scannedLocationName;
    }

    public Object getAssignedZones() {
        return assignedZones;
    }

    public void setAssignedZones(Object assignedZones) {
        this.assignedZones = assignedZones;
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

    public String getToBeAuditedOn() {
        return toBeAuditedOn;
    }

    public void setToBeAuditedOn(String toBeAuditedOn) {
        this.toBeAuditedOn = toBeAuditedOn;
    }

    public Object getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Object companyID) {
        this.companyID = companyID;
    }

    public Object getLineStatus() {
        return lineStatus;
    }

    public void setLineStatus(Object lineStatus) {
        this.lineStatus = lineStatus;
    }

    public Object getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Object totalStock) {
        this.totalStock = totalStock;
    }

    public Object getTotalScanned() {
        return totalScanned;
    }

    public void setTotalScanned(Object totalScanned) {
        this.totalScanned = totalScanned;
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

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getItemType() {
        return itemType;
    }

    public void setItemType(Object itemType) {
        this.itemType = itemType;
    }

    public Object getUom() {
        return uom;
    }

    public void setUom(Object uom) {
        this.uom = uom;
    }

    public Object getBarcode() {
        return barcode;
    }

    public void setBarcode(Object barcode) {
        this.barcode = barcode;
    }

    public Object getScanDate() {
        return scanDate;
    }

    public void setScanDate(Object scanDate) {
        this.scanDate = scanDate;
    }

    public Object getSubCode() {
        return subCode;
    }

    public void setSubCode(Object subCode) {
        this.subCode = subCode;
    }

    public Object getEntryType() {
        return entryType;
    }

    public void setEntryType(Object entryType) {
        this.entryType = entryType;
    }

    public Object getQty() {
        return qty;
    }

    public void setQty(Object qty) {
        this.qty = qty;
    }

    public Object getAssignedCount() {
        return assignedCount;
    }

    public void setAssignedCount(Object assignedCount) {
        this.assignedCount = assignedCount;
    }

    public Object getImageName() {
        return imageName;
    }

    public void setImageName(Object imageName) {
        this.imageName = imageName;
    }

    public Object getAuditCandidates() {
        return auditCandidates;
    }

    public void setAuditCandidates(Object auditCandidates) {
        this.auditCandidates = auditCandidates;
    }

    public Object getAuditItemCandidates() {
        return auditItemCandidates;
    }

    public void setAuditItemCandidates(Object auditItemCandidates) {
        this.auditItemCandidates = auditItemCandidates;
    }

    public Object getAuditItemFamily() {
        return auditItemFamily;
    }

    public void setAuditItemFamily(Object auditItemFamily) {
        this.auditItemFamily = auditItemFamily;
    }

    public Object getAuditCategories() {
        return auditCategories;
    }

    public void setAuditCategories(Object auditCategories) {
        this.auditCategories = auditCategories;
    }

    public Object getAuditStores() {
        return auditStores;
    }

    public void setAuditStores(Object auditStores) {
        this.auditStores = auditStores;
    }

}
