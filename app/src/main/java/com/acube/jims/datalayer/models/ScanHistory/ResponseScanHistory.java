package com.acube.jims.datalayer.models.ScanHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseScanHistory {

    @SerializedName("auditID")
    @Expose
    private String auditID;
    @SerializedName("scannedDate")
    @Expose
    private String scannedDate;
    @SerializedName("scanLocationCode")
    @Expose
    private String scanLocationCode;
    @SerializedName("scanLocationName")
    @Expose
    private String scanLocationName;

    public String getAuditID() {
        return auditID;
    }

    public void setAuditID(String auditID) {
        this.auditID = auditID;
    }

    public String getScannedDate() {
        return scannedDate;
    }

    public void setScannedDate(String scannedDate) {
        this.scannedDate = scannedDate;
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
}
