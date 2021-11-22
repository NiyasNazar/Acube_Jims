package com.acube.jims.datalayer.models.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDashboardSummary {
    @SerializedName("totalInventory")
    @Expose
    private Integer totalInventory;
    @SerializedName("todaySales")
    @Expose
    private Integer todaySales;
    @SerializedName("todayTotalSales")
    @Expose
    private Integer todayTotalSales;
    @SerializedName("totalProductSold")
    @Expose
    private Integer totalProductSold;
    @SerializedName("customerCount")
    @Expose
    private Integer customerCount;
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

    public Integer getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(Integer totalInventory) {
        this.totalInventory = totalInventory;
    }

    public Integer getTodaySales() {
        return todaySales;
    }

    public void setTodaySales(Integer todaySales) {
        this.todaySales = todaySales;
    }

    public Integer getTodayTotalSales() {
        return todayTotalSales;
    }

    public void setTodayTotalSales(Integer todayTotalSales) {
        this.todayTotalSales = todayTotalSales;
    }

    public Integer getTotalProductSold() {
        return totalProductSold;
    }

    public void setTotalProductSold(Integer totalProductSold) {
        this.totalProductSold = totalProductSold;
    }

    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
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

