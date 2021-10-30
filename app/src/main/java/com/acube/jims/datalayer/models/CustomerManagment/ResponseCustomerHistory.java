package com.acube.jims.datalayer.models.CustomerManagment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCustomerHistory {
    @SerializedName("customerID")
    @Expose
    private Integer customerID;
    @SerializedName("customerCode")
    @Expose
    private String customerCode;



    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("customerEmailID")
    @Expose
    private String customerEmailID;
    @SerializedName("customerContactNo")
    @Expose
    private String customerContactNo;
    @SerializedName("totalVisit")
    @Expose
    private Integer totalVisit;
    @SerializedName("lastVisit")
    @Expose
    private String lastVisit;
    @SerializedName("totalPurchase")
    @Expose
    private Integer totalPurchase;
    @SerializedName("itemViewHistory")
    @Expose
    private List<ItemViewHistory> itemViewHistory = null;
    @SerializedName("saleHistory")
    @Expose
    private List<SalesHistory> saleHistory = null;

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmailID() {
        return customerEmailID;
    }

    public void setCustomerEmailID(String customerEmailID) {
        this.customerEmailID = customerEmailID;
    }

    public String getCustomerContactNo() {
        return customerContactNo;
    }

    public void setCustomerContactNo(String customerContactNo) {
        this.customerContactNo = customerContactNo;
    }

    public Integer getTotalVisit() {
        return totalVisit;
    }

    public void setTotalVisit(Integer totalVisit) {
        this.totalVisit = totalVisit;
    }

    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }

    public Integer getTotalPurchase() {
        return totalPurchase;
    }

    public void setTotalPurchase(Integer totalPurchase) {
        this.totalPurchase = totalPurchase;
    }

    public List<ItemViewHistory> getItemViewHistory() {
        return itemViewHistory;
    }

    public void setItemViewHistory(List<ItemViewHistory> itemViewHistory) {
        this.itemViewHistory = itemViewHistory;
    }

    public List<SalesHistory> getSaleHistory() {
        return saleHistory;
    }

    public void setSaleHistory(List<SalesHistory> saleHistory) {
        this.saleHistory = saleHistory;
    }

}