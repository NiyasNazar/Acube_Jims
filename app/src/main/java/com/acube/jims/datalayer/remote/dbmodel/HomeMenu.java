package com.acube.jims.datalayer.remote.dbmodel;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity

public class HomeMenu implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "menuType")
    private String menuType;
    @ColumnInfo(name = "parentMenuName")
    private String parentMenuName;
    @ColumnInfo(name = "menuName")
    private String menuName;
    @ColumnInfo(name = "menuCondition")
    private String menuCondition;
    @ColumnInfo(name = "displayName")
    private String displayName;
    @ColumnInfo(name = "menuText")
    private String menuText;
    @ColumnInfo(name = "menuIconName")
    private String menuIconName;
    @ColumnInfo(name = "sortOrder")
    private Integer sortOrder;


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

    public void setMenuCondition(String menuCondition) {
        this.menuCondition = menuCondition;
    }

    public void setMenuIconName(String menuIconName) {
        this.menuIconName = menuIconName;
    }
}
