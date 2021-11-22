package com.acube.jims.datalayer.models.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardDataSingleBar {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("value")
    @Expose
    private Double value;
    @SerializedName("extraName")
    @Expose
    private Object extraName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Object getExtraName() {
        return extraName;
    }

    public void setExtraName(Object extraName) {
        this.extraName = extraName;
    }
}
