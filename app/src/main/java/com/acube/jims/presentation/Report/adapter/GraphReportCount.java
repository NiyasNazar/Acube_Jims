package com.acube.jims.presentation.Report.adapter;

public class GraphReportCount {
    private String auditID;
    private int Total;
    private int Found;
    private int Extra;
    private int Missing;
    private int locmismatch;

  /*  public GraphReportCount(String auditID, int Total, int Found, int Extra, int Missing, int locmismatch) {
        this.auditId = auditID;
        this.Total = Total;
        this.Found = Found;
        this.Extra = Extra;
        this.Missing = Missing;
        this.locmismatch = locmismatch;
    }*/

    public String getAuditID() {
        return auditID;
    }

    public void setAuditID(String auditID) {
        this.auditID = auditID;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public int getFound() {
        return Found;
    }

    public void setFound(int found) {
        Found = found;
    }

    public int getExtra() {
        return Extra;
    }

    public void setExtra(int extra) {
        Extra = extra;
    }

    public int getMissing() {
        return Missing;
    }

    public void setMissing(int missing) {
        Missing = missing;
    }

    public int getLocmismatch() {
        return locmismatch;
    }

    public void setLocmismatch(int locmismatch) {
        this.locmismatch = locmismatch;
    }
}
