package com.acube.jims.datalayer.remote.db;

public class LocalAudit {
    private Integer locationId;
    private Integer status;
    private String auditID;
    private String username;
    private String locationName;
    private String toBeAuditedOn;

    private String remark;
    private Integer systemLocationID;
    private String systemLocationName;

    public Integer getLocationId() {
        return locationId;
    }


    public Integer getStatus() {
        return status;
    }


    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public String getAuditID() {
        return auditID;
    }

    public void setAuditID(String auditID) {
        this.auditID = auditID;
    }

    public Integer getSystemLocationID() {
        return systemLocationID;
    }

    public void setSystemLocationID(Integer systemLocationID) {
        this.systemLocationID = systemLocationID;
    }

    public String getSystemLocationName() {
        return systemLocationName;
    }

    public void setSystemLocationName(String systemLocationName) {
        this.systemLocationName = systemLocationName;
    }

    @Override
    public String toString() {
        return systemLocationName;
    }
}
