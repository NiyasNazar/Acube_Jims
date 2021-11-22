
package com.acube.jims.datalayer.models.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardEmployeeAnalytic {

    @SerializedName("employeeCode")
    @Expose
    private String employeeCode;
    @SerializedName("employeeName")
    @Expose
    private String employeeName;
    @SerializedName("employeeID")
    @Expose
    private Integer employeeID;
    @SerializedName("totalValue")
    @Expose
    private Double totalValue;
    @SerializedName("performancePercentage")
    @Expose
    private Integer performancePercentage;

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

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Integer getPerformancePercentage() {
        return performancePercentage;
    }

    public void setPerformancePercentage(Integer performancePercentage) {
        this.performancePercentage = performancePercentage;
    }

}
