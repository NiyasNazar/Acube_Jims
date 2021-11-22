package com.acube.jims.datalayer.models.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardCategoryWiseSold {
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("categoryCode")
    @Expose
    private String categoryCode;
    @SerializedName("categoryID")
    @Expose
    private Integer categoryID;
    @SerializedName("subCategoryName")
    @Expose
    private String subCategoryName;
    @SerializedName("subCategoryCode")
    @Expose
    private String subCategoryCode;
    @SerializedName("subCategoryID")
    @Expose
    private Integer subCategoryID;
    @SerializedName("totalValue")
    @Expose
    private Double totalValue;
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("invoiceDate")
    @Expose
    private Object invoiceDate;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getSubCategoryCode() {
        return subCategoryCode;
    }

    public void setSubCategoryCode(String subCategoryCode) {
        this.subCategoryCode = subCategoryCode;
    }

    public Integer getSubCategoryID() {
        return subCategoryID;
    }

    public void setSubCategoryID(Integer subCategoryID) {
        this.subCategoryID = subCategoryID;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Object getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Object invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
}
