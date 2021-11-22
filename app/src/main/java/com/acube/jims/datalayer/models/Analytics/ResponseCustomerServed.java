
package com.acube.jims.datalayer.models.Analytics;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseCustomerServed {

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
    private List<EmployeePieChart> employeePieChart = null;
    @SerializedName("analyticsItemWiseViewed")
    @Expose
    private Object analyticsItemWiseViewed;
    @SerializedName("analyticsCustomersServed")
    @Expose
    private List<AnalyticsCustomersServed> analyticsCustomersServed = null;

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

    public List<EmployeePieChart> getEmployeePieChart() {
        return employeePieChart;
    }

    public void setEmployeePieChart(List<EmployeePieChart> employeePieChart) {
        this.employeePieChart = employeePieChart;
    }

    public Object getAnalyticsItemWiseViewed() {
        return analyticsItemWiseViewed;
    }

    public void setAnalyticsItemWiseViewed(Object analyticsItemWiseViewed) {
        this.analyticsItemWiseViewed = analyticsItemWiseViewed;
    }

    public List<AnalyticsCustomersServed> getAnalyticsCustomersServed() {
        return analyticsCustomersServed;
    }

    public void setAnalyticsCustomersServed(List<AnalyticsCustomersServed> analyticsCustomersServed) {
        this.analyticsCustomersServed = analyticsCustomersServed;
    }

}
