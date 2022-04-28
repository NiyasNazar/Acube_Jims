package com.acube.jims.datalayer.models.Invoice;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity(tableName = "KaratPrice", indices = @Index(value = {"karatCode","karatPrice"}, unique = true))

public class KaratPrice {
    @PrimaryKey(autoGenerate = true)
    private  int ID;
    @SerializedName("karatCode")
    @Expose
    private String karatCode;
    @SerializedName("karatPrice")
    @Expose
    private Double karatPrice;

    public String getKaratCode() {
        return karatCode;
    }

    public void setKaratCode(String karatCode) {
        this.karatCode = karatCode;
    }

    public Double getKaratPrice() {
        return karatPrice;
    }

    public void setKaratPrice(Double karatPrice) {
        this.karatPrice = karatPrice;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}

