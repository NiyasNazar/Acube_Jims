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
    private List<ExtraItems> extra = null;
    @SerializedName("locationMismatch")
    @Expose
    private  List<LocationMismatch> locationMismatches = null;
    @SerializedName("locationMismatchApproved")
    @Expose
    private final List<LocationMismatchApproved> locationMismatchApprovedList = null;


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

    public List<ExtraItems> getExtra() {
        return extra;
    }

    public void setExtra(List<ExtraItems> extra) {
        this.extra = extra;
    }

    public List<LocationMismatch> getLocationMismatches() {
        return locationMismatches;
    }

    public void setLocationMismatches(List<LocationMismatch> locationMismatches) {
        this.locationMismatches = locationMismatches;
    }

    public List<LocationMismatchApproved> getLocationMismatchApprovedList() {
        return locationMismatchApprovedList;
    }
}
