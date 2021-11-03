package com.acube.jims.datalayer.models.HomePage;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class HomeData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("menuType")
    @Expose
    @ColumnInfo(name = "menuType")

    private String menuType;
    @SerializedName("parentMenuName")
    @Expose
    @ColumnInfo(name = "parentMenuName")

    private String parentMenuName;
    @SerializedName("menuName")
    @Expose
    @ColumnInfo(name = "menuName")

    private String menuName;
    @SerializedName("menuCondition")
    @Expose
    @ColumnInfo(name = "menuCondition")

    private String menuCondition;
    @SerializedName("displayName")
    @Expose
    @ColumnInfo(name = "displayName")

    private String displayName;
    @SerializedName("menuText")
    @Expose
    @ColumnInfo(name = "menuText")

    private String menuText;
    @SerializedName("menuIconName")
    @Expose
    @ColumnInfo(name = "menuIconName")

    private String menuIconName;
    @SerializedName("sortOrder")
    @Expose
    @ColumnInfo(name = "sortOrder")

    private Integer sortOrder;
    private int menuicon;

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getParentMenuName() {
        return parentMenuName;
    }

    public void setParentMenuName(String parentMenuName) {
        this.parentMenuName = parentMenuName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuCondition() {
        return menuCondition;
    }

    public void setMenuCondition(String menuCondition) {
        this.menuCondition = menuCondition;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMenuicon() {
        return menuicon;
    }

    public void setMenuicon(int menuicon) {
        this.menuicon = menuicon;
    }
}
