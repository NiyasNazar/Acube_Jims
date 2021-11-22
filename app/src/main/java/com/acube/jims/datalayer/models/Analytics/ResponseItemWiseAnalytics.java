package com.acube.jims.datalayer.models.Analytics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseItemWiseAnalytics {

    @SerializedName("productsViewed")
    @Expose
    private Object productsViewed;
    @SerializedName("productsNotViewed")
    @Expose
    private Object productsNotViewed;
    @SerializedName("totalCustomersServed")
    @Expose
    private Object totalCustomersServed;
    @SerializedName("categoryAnalytics")
    @Expose
    private Object categoryAnalytics;
    @SerializedName("analyticsDataSingleBar")
    @Expose
    private Object analyticsDataSingleBar;
    @SerializedName("employeePieChart")
    @Expose
    private Object employeePieChart;
    @SerializedName("analyticsItemWiseViewed")
    @Expose
    private List<AnalyticsItemWiseViewed> analyticsItemWiseViewed = null;
    @SerializedName("analyticsCustomersServed")
    @Expose
    private Object analyticsCustomersServed;

    public Object getProductsViewed() {
        return productsViewed;
    }

    public void setProductsViewed(Object productsViewed) {
        this.productsViewed = productsViewed;
    }

    public Object getProductsNotViewed() {
        return productsNotViewed;
    }

    public void setProductsNotViewed(Object productsNotViewed) {
        this.productsNotViewed = productsNotViewed;
    }

    public Object getTotalCustomersServed() {
        return totalCustomersServed;
    }

    public void setTotalCustomersServed(Object totalCustomersServed) {
        this.totalCustomersServed = totalCustomersServed;
    }

    public Object getCategoryAnalytics() {
        return categoryAnalytics;
    }

    public void setCategoryAnalytics(Object categoryAnalytics) {
        this.categoryAnalytics = categoryAnalytics;
    }

    public Object getAnalyticsDataSingleBar() {
        return analyticsDataSingleBar;
    }

    public void setAnalyticsDataSingleBar(Object analyticsDataSingleBar) {
        this.analyticsDataSingleBar = analyticsDataSingleBar;
    }

    public Object getEmployeePieChart() {
        return employeePieChart;
    }

    public void setEmployeePieChart(Object employeePieChart) {
        this.employeePieChart = employeePieChart;
    }

    public List<AnalyticsItemWiseViewed> getAnalyticsItemWiseViewed() {
        return analyticsItemWiseViewed;
    }

    public void setAnalyticsItemWiseViewed(List<AnalyticsItemWiseViewed> analyticsItemWiseViewed) {
        this.analyticsItemWiseViewed = analyticsItemWiseViewed;
    }

    public Object getAnalyticsCustomersServed() {
        return analyticsCustomersServed;
    }

    public void setAnalyticsCustomersServed(Object analyticsCustomersServed) {
        this.analyticsCustomersServed = analyticsCustomersServed;
    }
}
