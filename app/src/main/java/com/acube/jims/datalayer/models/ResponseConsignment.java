package com.acube.jims.datalayer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseConsignment {
    @SerializedName("consignmentId")
    @Expose
    private String consignmentId;
    @SerializedName("warehouseId")
    @Expose
    private Integer warehouseId;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("consignmentLines")
    @Expose
    private List<ConsignmentLine> consignmentLines = null;

    public String getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(String consignmentId) {
        this.consignmentId = consignmentId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<ConsignmentLine> getConsignmentLines() {
        return consignmentLines;
    }

    public void setConsignmentLines(List<ConsignmentLine> consignmentLines) {
        this.consignmentLines = consignmentLines;
    }
}
