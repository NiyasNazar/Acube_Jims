package com.acube.jims.datalayer.models.missingcomp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMissingComp {
    @SerializedName("missinSerialNoList")
    @Expose
    private List<MissinSerialNo> missinSerialNoList = null;
    @SerializedName("auditDates")
    @Expose
    private List<AuditDate> auditDates = null;
    @SerializedName("result")
    @Expose
    private List<AuditResult> result = null;
    @SerializedName("missingDetails")
    @Expose
    private Object missingDetails;
    @SerializedName("totalWeight")
    @Expose
    private Object totalWeight;
    @SerializedName("totalPrice")
    @Expose
    private Object totalPrice;
    @SerializedName("totalQty")
    @Expose
    private Object totalQty;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("companyLogo")
    @Expose
    private String companyLogo;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("address2")
    @Expose
    private String address2;
    @SerializedName("contactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("contactEmail")
    @Expose
    private String contactEmail;

    public List<MissinSerialNo> getMissinSerialNoList() {
        return missinSerialNoList;
    }

    public void setMissinSerialNoList(List<MissinSerialNo> missinSerialNoList) {
        this.missinSerialNoList = missinSerialNoList;
    }

    public List<AuditDate> getAuditDates() {
        return auditDates;
    }

    public void setAuditDates(List<AuditDate> auditDates) {
        this.auditDates = auditDates;
    }

    public List<AuditResult> getResult() {
        return result;
    }

    public void setResult(List<AuditResult> result) {
        this.result = result;
    }

    public Object getMissingDetails() {
        return missingDetails;
    }

    public void setMissingDetails(Object missingDetails) {
        this.missingDetails = missingDetails;
    }

    public Object getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Object totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Object getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Object totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Object getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Object totalQty) {
        this.totalQty = totalQty;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

}
