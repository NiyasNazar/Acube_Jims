package com.acube.jims.datalayer.models.Authentication;

import com.acube.jims.datalayer.models.Invoice.KaratPrice;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseLogin {
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("roleName")
    @Expose
    private Integer roleName;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("employeeID")
    @Expose
    private Integer employeeID;
    @SerializedName("employeeName")
    @Expose
    private String employeeName;
    @SerializedName("warehouseID")
    @Expose
    private Integer warehouseID;
    @SerializedName("companyID")
    @Expose
    private String companyID;
    @SerializedName("applicationID")
    @Expose
    private String applicationID;
    @SerializedName("companyLogo")
    @Expose
    private String companyLogo;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("companyRegNumber")
    @Expose
    private String companyRegNumber;
    @SerializedName("contactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("metalRegNumber")
    @Expose
    private String metalRegNumber;
    @SerializedName("vatNumber")
    @Expose
    private String vatNumber;
    @SerializedName("warehouseAddress1")
    @Expose
    private String warehouseAddress1;
    @SerializedName("warehouseAddress2")
    @Expose
    private String warehouseAddress2;
    @SerializedName("goldRate")
    @Expose
    private Object goldRate;
    @SerializedName("karatPriceList")
    @Expose
    private List<KaratPrice> karatPriceList = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getRoleName() {
        return roleName;
    }

    public void setRoleName(Integer roleName) {
        this.roleName = roleName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Integer warehouseID) {
        this.warehouseID = warehouseID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyRegNumber() {
        return companyRegNumber;
    }

    public void setCompanyRegNumber(String companyRegNumber) {
        this.companyRegNumber = companyRegNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMetalRegNumber() {
        return metalRegNumber;
    }

    public void setMetalRegNumber(String metalRegNumber) {
        this.metalRegNumber = metalRegNumber;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getWarehouseAddress1() {
        return warehouseAddress1;
    }

    public void setWarehouseAddress1(String warehouseAddress1) {
        this.warehouseAddress1 = warehouseAddress1;
    }

    public String getWarehouseAddress2() {
        return warehouseAddress2;
    }

    public void setWarehouseAddress2(String warehouseAddress2) {
        this.warehouseAddress2 = warehouseAddress2;
    }

    public Object getGoldRate() {
        return goldRate;
    }

    public void setGoldRate(Object goldRate) {
        this.goldRate = goldRate;
    }

    public List<KaratPrice> getKaratPriceList() {
        return karatPriceList;
    }

    public void setKaratPriceList(List<KaratPrice> karatPriceList) {
        this.karatPriceList = karatPriceList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
