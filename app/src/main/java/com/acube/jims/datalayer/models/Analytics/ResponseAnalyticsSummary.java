package com.acube.jims.datalayer.models.Analytics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseAnalyticsSummary {
    @SerializedName("productsViewed")
    @Expose
    private Integer productsViewed;
    @SerializedName("productsNotViewed")
    @Expose
    private Integer productsNotViewed;
    @SerializedName("totalCustomersServed")
    @Expose
    private Integer totalCustomersServed;
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
    private Object analyticsItemWiseViewed;
    @SerializedName("analyticsCustomersServed")
    @Expose
    private Object analyticsCustomersServed;

    public Integer getProductsViewed() {
        return productsViewed;
    }

    public void setProductsViewed(Integer productsViewed) {
        this.productsViewed = productsViewed;
    }

    public Integer getProductsNotViewed() {
        return productsNotViewed;
    }

    public void setProductsNotViewed(Integer productsNotViewed) {
        this.productsNotViewed = productsNotViewed;
    }

    public Integer getTotalCustomersServed() {
        return totalCustomersServed;
    }

    public void setTotalCustomersServed(Integer totalCustomersServed) {
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

    public Object getAnalyticsItemWiseViewed() {
        return analyticsItemWiseViewed;
    }

    public void setAnalyticsItemWiseViewed(Object analyticsItemWiseViewed) {
        this.analyticsItemWiseViewed = analyticsItemWiseViewed;
    }

    public Object getAnalyticsCustomersServed() {
        return analyticsCustomersServed;
    }

    public void setAnalyticsCustomersServed(Object analyticsCustomersServed) {
        this.analyticsCustomersServed = analyticsCustomersServed;
    }

}

