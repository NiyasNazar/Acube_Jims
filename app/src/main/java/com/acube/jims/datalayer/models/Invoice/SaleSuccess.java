package com.acube.jims.datalayer.models.Invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaleSuccess {
    @SerializedName("invoiceNo")
    @Expose
    private String invoiceNo;
    @SerializedName("invoiceDate")
    @Expose
    private String invoiceDate;
    @SerializedName("customerID")
    @Expose
    private Integer customerID;
    @SerializedName("customerCode")
    @Expose
    private Object customerCode;
    @SerializedName("customerName")
    @Expose
    private Object customerName;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("warehouseID")
    @Expose
    private Integer warehouseID;
    @SerializedName("warehouseCode")
    @Expose
    private Object warehouseCode;
    @SerializedName("warehouseName")
    @Expose
    private Object warehouseName;
    @SerializedName("salesManID")
    @Expose
    private Integer salesManID;
    @SerializedName("salesManCode")
    @Expose
    private Object salesManCode;
    @SerializedName("salesManName")
    @Expose
    private Object salesManName;
    @SerializedName("cashCounterID")
    @Expose
    private Integer cashCounterID;
    @SerializedName("cashCounterName")
    @Expose
    private Object cashCounterName;
    @SerializedName("goldVAT")
    @Expose
    private Object goldVAT;
    @SerializedName("laborVAT")
    @Expose
    private Object laborVAT;
    @SerializedName("goldRate")
    @Expose
    private Double goldRate;
    @SerializedName("saleType")
    @Expose
    private String saleType;


    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Object getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(Object customerCode) {
        this.customerCode = customerCode;
    }

    public Object getCustomerName() {
        return customerName;
    }

    public void setCustomerName(Object customerName) {
        this.customerName = customerName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Integer warehouseID) {
        this.warehouseID = warehouseID;
    }

    public Object getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(Object warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public Object getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(Object warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Integer getSalesManID() {
        return salesManID;
    }

    public void setSalesManID(Integer salesManID) {
        this.salesManID = salesManID;
    }

    public Object getSalesManCode() {
        return salesManCode;
    }

    public void setSalesManCode(Object salesManCode) {
        this.salesManCode = salesManCode;
    }

    public Object getSalesManName() {
        return salesManName;
    }

    public void setSalesManName(Object salesManName) {
        this.salesManName = salesManName;
    }

    public Integer getCashCounterID() {
        return cashCounterID;
    }

    public void setCashCounterID(Integer cashCounterID) {
        this.cashCounterID = cashCounterID;
    }

    public Object getCashCounterName() {
        return cashCounterName;
    }

    public void setCashCounterName(Object cashCounterName) {
        this.cashCounterName = cashCounterName;
    }

    public Object getGoldVAT() {
        return goldVAT;
    }

    public void setGoldVAT(Object goldVAT) {
        this.goldVAT = goldVAT;
    }

    public Object getLaborVAT() {
        return laborVAT;
    }

    public void setLaborVAT(Object laborVAT) {
        this.laborVAT = laborVAT;
    }

    public Double getGoldRate() {
        return goldRate;
    }

    public void setGoldRate(Double goldRate) {
        this.goldRate = goldRate;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }



}
