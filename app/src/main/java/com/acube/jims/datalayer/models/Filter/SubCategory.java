
package com.acube.jims.datalayer.models.Filter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategory {

    @SerializedName("category")
    @Expose
    private Object category;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("subCategoryName")
    @Expose
    private String subCategoryName;
    @SerializedName("subCategoryCode")
    @Expose
    private String subCategoryCode;
    @SerializedName("categoryID")
    @Expose
    private Integer categoryID;
    @SerializedName("items")
    @Expose
    private Object items;
    @SerializedName("companyID")
    @Expose
    private Object companyID;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("modifiedBy")
    @Expose
    private Object modifiedBy;
    @SerializedName("modifiedDate")
    @Expose
    private Object modifiedDate;

    private boolean isSelected;


    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public Object getItems() {
        return items;
    }

    public void setItems(Object items) {
        this.items = items;
    }

    public Object getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Object companyID) {
        this.companyID = companyID;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Object getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Object modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Object getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Object modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
