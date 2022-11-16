package com.acube.jims.datalayer.models.missingcomp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MissingStatus {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("auditDate")
    @Expose
    private String auditDate;
    @SerializedName("auditId")
    @Expose
    private String auditId;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }
}
