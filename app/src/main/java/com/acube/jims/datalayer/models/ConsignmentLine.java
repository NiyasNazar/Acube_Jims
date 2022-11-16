package com.acube.jims.datalayer.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ConsignmentLine", indices = @Index(value = {"serialNumber"}, unique = true))

public class ConsignmentLine {


    @SerializedName("serialNumber")
    @Expose
    @NonNull
    @PrimaryKey(autoGenerate = false)
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
}