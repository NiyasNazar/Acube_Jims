package com.acube.jims.datalayer.models.HomePage;

public class NavMenuModel {
    public String menuName, url;
    public boolean hasChildren, isGroup;
    public int icon;

    public NavMenuModel(String menuName, boolean isGroup, boolean hasChildren, int icon) {

        this.menuName = menuName;
        this.icon = icon;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }
}
