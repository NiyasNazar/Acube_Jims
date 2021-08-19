package com.acube.jims.datalayer.models.Filter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weight {
    @SerializedName("goldWeight")
    @Expose
    private Integer goldWeight;

    public Integer getGoldWeight() {
        return goldWeight;
    }

    public void setGoldWeight(Integer goldWeight) {
        this.goldWeight = goldWeight;
    }
}
