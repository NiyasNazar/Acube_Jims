package com.acube.jims.datalayer.models.HomePage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeData {
    @SerializedName("menuType")
    @Expose
    private String menuType;
    @SerializedName("parentMenuName")
    @Expose
    private Object parentMenuName;
    @SerializedName("menuName")
    @Expose
    private String menuName;
    @SerializedName("menuCondition")
    @Expose
    private Object menuCondition;
    @SerializedName("displayName")
    @Expose
    private Object displayName;
    @SerializedName("menuText")
    @Expose
    private String menuText;
    @SerializedName("menuIconName")
    @Expose
    private String menuIconName;
    @SerializedName("sortOrder")
    @Expose
    private Integer sortOrder;

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public Object getParentMenuName() {
        return parentMenuName;
    }

    public void setParentMenuName(Object parentMenuName) {
        this.parentMenuName = parentMenuName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Object getMenuCondition() {
        return menuCondition;
    }

    public void setMenuCondition(Object menuCondition) {
        this.menuCondition = menuCondition;
    }

    public Object getDisplayName() {
        return displayName;
    }

    public void setDisplayName(Object displayName) {
        this.displayName = displayName;
    }

    public String getMenuText() {
        return menuText;
    }

    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }

    public String getMenuIconName() {
        return menuIconName;
    }

    public void setMenuIconName(String menuIconName) {
        this.menuIconName = menuIconName;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

}
