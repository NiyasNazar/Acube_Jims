package com.acube.jims.datalayer.models.Catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemSub {
    @SerializedName("itemID")
    @Expose
    private Integer itemID;
    @SerializedName("imageFileName")
    @Expose
    private String imageFileName;
    @SerializedName("imageFilePath")
    @Expose
    private String imageFilePath;
    @SerializedName("itemImage")
    @Expose
    private Object itemImage;

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public Object getItemImage() {
        return itemImage;
    }

    public void setItemImage(Object itemImage) {
        this.itemImage = itemImage;
    }
}
