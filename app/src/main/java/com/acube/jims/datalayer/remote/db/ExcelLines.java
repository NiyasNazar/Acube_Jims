package com.acube.jims.datalayer.remote.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity(tableName = "ExcelLines")

public class ExcelLines {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @SerializedName("serialNumber")
    @Expose

    private String serialNumber;
    @SerializedName("status")
    @Expose
    private Integer status;
    private String consignmentId;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(String consignmentId) {
        this.consignmentId = consignmentId;
    }

    @Override
    public String toString() {
        return consignmentId;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
