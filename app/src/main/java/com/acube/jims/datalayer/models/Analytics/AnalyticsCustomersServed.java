
package com.acube.jims.datalayer.models.Analytics;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AnalyticsCustomersServed {

    @SerializedName("customerID")
    @Expose
    private Object customerID;
    @SerializedName("handledDate")
    @Expose
    private Object handledDate;
    @SerializedName("salesManID")
    @Expose
    private Integer salesManID;
    @SerializedName("employeeCode")
    @Expose
    private String employeeCode;
    @SerializedName("employeeName")
    @Expose
    private String employeeName;
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("performancePercentage")
    @Expose
    private Integer performancePercentage;

    public Object getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Object customerID) {
        this.customerID = customerID;
    }

    public Object getHandledDate() {
        return handledDate;
    }

    public void setHandledDate(Object handledDate) {
        this.handledDate = handledDate;
    }

    public Integer getSalesManID() {
        return salesManID;
    }

    public void setSalesManID(Integer salesManID) {
        this.salesManID = salesManID;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPerformancePercentage() {
        return performancePercentage;
    }

    public void setPerformancePercentage(Integer performancePercentage) {
        this.performancePercentage = performancePercentage;
    }

}
