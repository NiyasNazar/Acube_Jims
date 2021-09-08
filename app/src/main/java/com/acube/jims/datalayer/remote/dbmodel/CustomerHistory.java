package com.acube.jims.datalayer.remote.dbmodel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "CustomerHistory", indices = @Index(value = {"SerialNo"}, unique = true))
public class CustomerHistory {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private Integer customerID;
    @ColumnInfo
    private Integer itemID;
    @ColumnInfo
    private String StartTime;
    @ColumnInfo
    private String SerialNo;

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }
}
