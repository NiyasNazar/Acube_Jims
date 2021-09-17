package com.acube.jims.Presentation.Quotation.adapter;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "DiscountItem", indices = @Index(value = {"itemserial"}, unique = true))
public class DiscountItem {

    @PrimaryKey
    @NonNull
    private String itemserial;
    @ColumnInfo
    private double amt;
    @ColumnInfo
    private double totalwithtax;
    public String getItemserial() {
        return itemserial;
    }

    public void setItemserial(String itemserial) {
        this.itemserial = itemserial;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public double getTotalwithtax() {
        return totalwithtax;
    }

    public void setTotalwithtax(double totalwithtax) {
        this.totalwithtax = totalwithtax;
    }
}
