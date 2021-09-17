package com.acube.jims.datalayer.models.Authentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLogin {
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("roleName")
    @Expose
    private String roleName;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("employeeID")
    @Expose
    private Integer employeeID;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("warehouseID")
    @Expose
    private Integer warehouseID;
    @SerializedName("companyID")
    @Expose
    private String companyID;

    public String getUserName() {
        return userName;
    }
    @SerializedName("employeeName")
    @Expose
    private String employeeName;


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
