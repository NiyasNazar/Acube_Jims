
package com.acube.jims.datalayer.models.Filter;

import java.util.List;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseFetchFilters {

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
    @SerializedName("karats")
    @Expose
    private List<Karatresult> karats = null;
    @SerializedName("colors")
    @Expose
    private List<Colorresult> colors = null;

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

    public List<Karatresult> getKarats() {
        return karats;
    }

    public void setKarats(List<Karatresult> karats) {
        this.karats = karats;
    }

    public List<Colorresult> getColors() {
        return colors;
    }

    public void setColors(List<Colorresult> colors) {
        this.colors = colors;
    }


}
