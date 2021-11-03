package com.acube.jims.datalayer.models.LocateProduct;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "LocateItem", indices = @Index(value = {"serialnumber"}, unique = true))

public class LocateItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String serialnumber;

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
