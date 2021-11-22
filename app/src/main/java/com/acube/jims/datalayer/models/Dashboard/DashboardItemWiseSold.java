package com.acube.jims.datalayer.models.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardItemWiseSold {
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("itemCode")
    @Expose
    private String itemCode;
    @SerializedName("itemID")
    @Expose
    private Integer itemID;
    @SerializedName("totalValue")
    @Expose
    private Double totalValue;
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("invoiceDate")
    @Expose
    private Object invoiceDate;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Object getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Object invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

}
