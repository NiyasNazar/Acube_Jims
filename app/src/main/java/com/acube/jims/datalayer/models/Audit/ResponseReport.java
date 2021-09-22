package com.acube.jims.datalayer.models.Audit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseReport {
    @SerializedName("found")
    @Expose
    private List<Found> found = null;
    @SerializedName("missing")
    @Expose
    private List<Missing> missing = null;
    @SerializedName("extra")
    @Expose
    private List<Object> extra = null;

    public List<Found> getFound() {
        return found;
    }

    public void setFound(List<Found> found) {
        this.found = found;
    }

    public List<Missing> getMissing() {
        return missing;
    }

    public void setMissing(List<Missing> missing) {
        this.missing = missing;
    }

    public List<Object> getExtra() {
        return extra;
    }

    public void setExtra(List<Object> extra) {
        this.extra = extra;
    }
}
