package com.acube.jims.datalayer.models.Catalogue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemSub implements Parcelable {
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

    protected ItemSub(Parcel in) {
        if (in.readByte() == 0) {
            itemID = null;
        } else {
            itemID = in.readInt();
        }
        imageFileName = in.readString();
        imageFilePath = in.readString();
    }

    public static final Creator<ItemSub> CREATOR = new Creator<ItemSub>() {
        @Override
        public ItemSub createFromParcel(Parcel in) {
            return new ItemSub(in);
        }

        @Override
        public ItemSub[] newArray(int size) {
            return new ItemSub[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (itemID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(itemID);
        }
        dest.writeString(imageFileName);
        dest.writeString(imageFilePath);
    }
}
