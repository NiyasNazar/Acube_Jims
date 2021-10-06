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
    private double discount;
    @ColumnInfo
    private double totalwithtax;
    @ColumnInfo
    private double totaltax;

    @ColumnInfo
    private int itemID;
    @ColumnInfo
    private double goldweight;
    @ColumnInfo
    private double discountpercentage;
    @ColumnInfo
    private double itemVat;
    @ColumnInfo
    private double itemVatAmount;
    @ColumnInfo
    private double labourVat;
    @ColumnInfo
    private double labourVatAmount;

    @ColumnInfo
    private double laborRate;

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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotaltax() {
        return totaltax;
    }

    public void setTotaltax(double totaltax) {
        this.totaltax = totaltax;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public double getGoldweight() {
        return goldweight;
    }

    public void setGoldweight(double goldweight) {
        this.goldweight = goldweight;
    }

    public double getDiscountpercentage() {
        return discountpercentage;
    }

    public void setDiscountpercentage(double discountpercentage) {
        this.discountpercentage = discountpercentage;
    }

    public double getItemVat() {
        return itemVat;
    }

    public void setItemVat(double itemVat) {
        this.itemVat = itemVat;
    }

    public double getLabourVat() {
        return labourVat;
    }

    public void setLabourVat(double labourVat) {
        this.labourVat = labourVat;
    }

    public double getItemVatAmount() {
        return itemVatAmount;
    }

    public void setItemVatAmount(double itemVatAmount) {
        this.itemVatAmount = itemVatAmount;
    }

    public double getLabourVatAmount() {
        return labourVatAmount;
    }

    public void setLabourVatAmount(double labourVatAmount) {
        this.labourVatAmount = labourVatAmount;
    }



    public double getLaborRate() {
        return laborRate;
    }

    public void setLaborRate(double laborRate) {
        this.laborRate = laborRate;
    }
}
