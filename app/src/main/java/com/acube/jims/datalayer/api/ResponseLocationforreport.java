package com.acube.jims.datalayer.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLocationforreport {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("locationCode")
    @Expose
    private String locationCode;
    @SerializedName("warehouseID")
    @Expose
    private Integer warehouseID;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public Integer getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Integer warehouseID) {
        this.warehouseID = warehouseID;
    }

    @Override
    public String toString() {
        return locationName;
    }
}
