package com.acube.jims.datalayer.models.Audit;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "TemDataSerial", indices = @Index(value = {"SerialNo"}, unique = true))

public class TemDataSerial {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String SerialNo;
    private String auditId;
    private int status;
    private String branch;

    public TemDataSerial(String SerialNo,String auditId) {
        this.SerialNo = SerialNo;
        this.auditId = auditId;
    }



    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }


}
