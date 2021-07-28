
package com.acube.jims.datalayer.models.Filter;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Catresult {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("categoryCode")
    @Expose
    private String categoryCode;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("subCategories")
    @Expose
    private List<SubCategory> subCategories = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

}
