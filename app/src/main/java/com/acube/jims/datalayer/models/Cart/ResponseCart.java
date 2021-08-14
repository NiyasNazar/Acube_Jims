package com.acube.jims.datalayer.models.Cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCart {
    @SerializedName("cartListNo")
    @Expose
    private String cartListNo;
    @SerializedName("customerID")
    @Expose
    private Integer customerID;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("customerCode")
    @Expose
    private String customerCode;
    @SerializedName("viewedOn")
    @Expose
    private String viewedOn;
    @SerializedName("employeeID")
    @Expose
    private Integer employeeID;
    @SerializedName("employeeName")
    @Expose
    private String employeeName;
    @SerializedName("employeeCode")
    @Expose
    private String employeeCode;
    @SerializedName("cartDetails")
    @Expose
    private List<CartDetail> cartDetails = null;

    public String getCartListNo() {
        return cartListNo;
    }

    public void setCartListNo(String cartListNo) {
        this.cartListNo = cartListNo;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getViewedOn() {
        return viewedOn;
    }

    public void setViewedOn(String viewedOn) {
        this.viewedOn = viewedOn;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public List<CartDetail> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(List<CartDetail> cartDetails) {
        this.cartDetails = cartDetails;
    }
}
