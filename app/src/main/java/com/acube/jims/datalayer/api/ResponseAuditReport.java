package com.acube.jims.datalayer.api;

import com.acube.jims.datalayer.models.Audit.ResponseReportList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseAuditReport {
    @SerializedName("result")
    @Expose
    private List<ResponseReportList> result = null;

    public List<ResponseReportList> getResult() {
        return result;
    }

    public void setResult(List<ResponseReportList> result) {
        this.result = result;
    }

}
