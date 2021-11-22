
package com.acube.jims.datalayer.models.Analytics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeePieChart {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("extraName")
    @Expose
    private Object extraName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Object getExtraName() {
        return extraName;
    }

    public void setExtraName(Object extraName) {
        this.extraName = extraName;
    }

}
