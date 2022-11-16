package com.acube.jims.datalayer.models.Filter;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "goldWeight", indices = @Index(value = {"goldWeight"}, unique = true))

public class Weight {
    @PrimaryKey
    @NonNull
    @SerializedName("goldWeight")
    @Expose
    private Integer goldWeight;

    private boolean isSelected;

    public Integer getGoldWeight() {
        return goldWeight;
    }

    public void setGoldWeight(Integer goldWeight) {
        this.goldWeight = goldWeight;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
