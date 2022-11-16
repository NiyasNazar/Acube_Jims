package com.acube.jims.datalayer.models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


public class OfflineConsignment {
    private String serialNumber;

    private String consignmentId;









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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
