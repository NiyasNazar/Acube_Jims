package com.acube.jims.datalayer.models.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseTopCategory {
    @SerializedName("totalInventory")
    @Expose
    private Object totalInventory;
    @SerializedName("todaySales")
    @Expose
    private Object todaySales;
    @SerializedName("todayTotalSales")
    @Expose
    private Object todayTotalSales;
    @SerializedName("totalProductSold")
    @Expose
    private Object totalProductSold;
    @SerializedName("customerCount")
    @Expose
    private Object customerCount;
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
    private List<DashboardCategoryWiseSold> dashboardCategoryWiseSold = null;
    @SerializedName("dashboardEmployeeAnalytics")
    @Expose
    private Object dashboardEmployeeAnalytics;

    public Object getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(Object totalInventory) {
        this.totalInventory = totalInventory;
    }

    public Object getTodaySales() {
        return todaySales;
    }

    public void setTodaySales(Object todaySales) {
        this.todaySales = todaySales;
    }

    public Object getTodayTotalSales() {
        return todayTotalSales;
    }

    public void setTodayTotalSales(Object todayTotalSales) {
        this.todayTotalSales = todayTotalSales;
    }

    public Object getTotalProductSold() {
        return totalProductSold;
    }

    public void setTotalProductSold(Object totalProductSold) {
        this.totalProductSold = totalProductSold;
    }

    public Object getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Object customerCount) {
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

    public List<DashboardCategoryWiseSold> getDashboardCategoryWiseSold() {
        return dashboardCategoryWiseSold;
    }

    public void setDashboardCategoryWiseSold(List<DashboardCategoryWiseSold> dashboardCategoryWiseSold) {
        this.dashboardCategoryWiseSold = dashboardCategoryWiseSold;
    }

    public Object getDashboardEmployeeAnalytics() {
        return dashboardEmployeeAnalytics;
    }

    public void setDashboardEmployeeAnalytics(Object dashboardEmployeeAnalytics) {
        this.dashboardEmployeeAnalytics = dashboardEmployeeAnalytics;
    }

}
