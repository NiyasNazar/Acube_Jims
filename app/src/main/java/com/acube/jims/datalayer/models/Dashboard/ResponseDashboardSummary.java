package com.acube.jims.datalayer.models.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDashboardSummary {
    @SerializedName("totalInventory")
    @Expose
    private Double totalInventory;
    @SerializedName("todaySales")
    @Expose
    private Double todaySales;
    @SerializedName("todayTotalSales")
    @Expose
    private Double todayTotalSales;
    @SerializedName("totalProductSold")
    @Expose
    private Double totalProductSold;
    @SerializedName("customerCount")
    @Expose
    private Double customerCount;
    @SerializedName("dashboardDataSingleBar")
    @Expose
    private Object dashboardDataSingleBar;
    @SerializedName("dashboardEmployeePieChart")
    @Expose
    private Object dashboardEmployeePieChart;
    @SerializedName("dashboardDataMultipleBar")
    @Expose
    private Object dashboardDataMultipleBar;
    @SerializedName("dashboardItemWiseSold")
    @Expose
    private Object dashboardItemWiseSold;
    @SerializedName("dashboardCategoryWiseSold")
    @Expose
    private Object dashboardCategoryWiseSold;
    @SerializedName("dashboardEmployeeAnalytics")
    @Expose
    private Object dashboardEmployeeAnalytics;

    public Double getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(Double totalInventory) {
        this.totalInventory = totalInventory;
    }

    public Double getTodaySales() {
        return todaySales;
    }

    public void setTodaySales(Double todaySales) {
        this.todaySales = todaySales;
    }

    public Double getTodayTotalSales() {
        return todayTotalSales;
    }

    public void setTodayTotalSales(Double todayTotalSales) {
        this.todayTotalSales = todayTotalSales;
    }

    public Double getTotalProductSold() {
        return totalProductSold;
    }

    public void setTotalProductSold(Double totalProductSold) {
        this.totalProductSold = totalProductSold;
    }

    public Double getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Double customerCount) {
        this.customerCount = customerCount;
    }

    public Object getDashboardDataSingleBar() {
        return dashboardDataSingleBar;
    }

    public void setDashboardDataSingleBar(Object dashboardDataSingleBar) {
        this.dashboardDataSingleBar = dashboardDataSingleBar;
    }

    public Object getDashboardEmployeePieChart() {
        return dashboardEmployeePieChart;
    }

    public void setDashboardEmployeePieChart(Object dashboardEmployeePieChart) {
        this.dashboardEmployeePieChart = dashboardEmployeePieChart;
    }

    public Object getDashboardDataMultipleBar() {
        return dashboardDataMultipleBar;
    }

    public void setDashboardDataMultipleBar(Object dashboardDataMultipleBar) {
        this.dashboardDataMultipleBar = dashboardDataMultipleBar;
    }

    public Object getDashboardItemWiseSold() {
        return dashboardItemWiseSold;
    }

    public void setDashboardItemWiseSold(Object dashboardItemWiseSold) {
        this.dashboardItemWiseSold = dashboardItemWiseSold;
    }

    public Object getDashboardCategoryWiseSold() {
        return dashboardCategoryWiseSold;
    }

    public void setDashboardCategoryWiseSold(Object dashboardCategoryWiseSold) {
        this.dashboardCategoryWiseSold = dashboardCategoryWiseSold;
    }

    public Object getDashboardEmployeeAnalytics() {
        return dashboardEmployeeAnalytics;
    }

    public void setDashboardEmployeeAnalytics(Object dashboardEmployeeAnalytics) {
        this.dashboardEmployeeAnalytics = dashboardEmployeeAnalytics;
    }

}

