package com.acube.jims.datalayer.remote.db;

public class LocalAuditLocation {
    private String auditID;
    private String toBeAuditedOn;

    private String remark;
    private Integer systemLocationID;
    private  String systemLocationName;


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
