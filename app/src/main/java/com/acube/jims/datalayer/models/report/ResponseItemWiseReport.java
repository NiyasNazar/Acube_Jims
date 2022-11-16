package com.acube.jims.datalayer.models.report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseItemWiseReport {
    @SerializedName("result")
    @Expose
    private List<ItemWiseReport> result = null;
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
    @SerializedName("totalWeight")
    @Expose
    private Double totalWeight;
    @SerializedName("totalQty")
    @Expose
    private Integer totalQty;

    public List<ItemWiseReport> getResult() {
        return result;
    }

    public void setResult(List<ItemWiseReport> result) {
        this.result = result;
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

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Integer getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
    }

}
