package com.acube.jims.datalayer.models.Invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KaratPrice {
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

}

