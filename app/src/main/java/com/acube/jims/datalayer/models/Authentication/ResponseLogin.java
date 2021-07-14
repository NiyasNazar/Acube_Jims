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

    public String getUserName() {
        return userName;
    }

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
}
