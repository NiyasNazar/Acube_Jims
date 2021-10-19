package com.acube.jims.datalayer.models.Invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseInvoiceList {
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    @SerializedName("itemID")
    @Expose
    private Integer itemID;
    @SerializedName("itemCode")
    @Expose
    private String itemCode;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("priceWithoutTax")
    @Expose
    private Double priceWithoutTax;
    @SerializedName("labourChargeMax")
    @Expose
    private Double labourChargeMax;
    @SerializedName("labourChargeMin")
    @Expose
    private Double labourChargeMin;
    @SerializedName("labourCharge")
    @Expose
    private Integer labourCharge;
    @SerializedName("labourTax")
    @Expose
    private Integer labourTax;
    @SerializedName("itemTax")
    @Expose
    private Integer itemTax;
    @SerializedName("imagePath")
    @Expose
    private String imagePath;
    @SerializedName("goldWeight")
    @Expose
    private Double goldWeight;
    @SerializedName("stoneWeight")
    @Expose
    private Integer stoneWeight;
    @SerializedName("priceForGram")
    @Expose
    private Object priceForGram;
    @SerializedName("karatCode")
    @Expose
    private String karatCode;

    @Expose
    private Double goldVat;
    @Expose
    private Double laborVat;
    @SerializedName("karatPriceList")
    @Expose
    private List<KaratPrice> karatPriceList = null;
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getPriceWithoutTax() {
        return priceWithoutTax;
    }

    public void setPriceWithoutTax(Double priceWithoutTax) {
        this.priceWithoutTax = priceWithoutTax;
    }

    public Integer getLabourCharge() {
        return labourCharge;
    }

    public void setLabourCharge(Integer labourCharge) {
        this.labourCharge = labourCharge;
    }

    public Integer getLabourTax() {
        return labourTax;
    }

    public void setLabourTax(Integer labourTax) {
        this.labourTax = labourTax;
    }

    public Integer getItemTax() {
        return itemTax;
    }

    public void setItemTax(Integer itemTax) {
        this.itemTax = itemTax;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Double getGoldWeight() {
        return goldWeight;
    }

    public void setGoldWeight(Double goldWeight) {
        this.goldWeight = goldWeight;
    }

    public Integer getStoneWeight() {
        return stoneWeight;
    }

    public void setStoneWeight(Integer stoneWeight) {
        this.stoneWeight = stoneWeight;
    }

    public Object getPriceForGram() {
        return priceForGram;
    }

    public void setPriceForGram(Object priceForGram) {
        this.priceForGram = priceForGram;
    }

    public Double getLabourChargeMax() {
        return labourChargeMax;
    }

    public void setLabourChargeMax(Double labourChargeMax) {
        this.labourChargeMax = labourChargeMax;
    }

    public Double getLabourChargeMin() {
        return labourChargeMin;
    }

    public void setLabourChargeMin(Double labourChargeMin) {
        this.labourChargeMin = labourChargeMin;
    }

    public Double getGoldVat() {
        return goldVat;
    }

    public void setGoldVat(Double goldVat) {
        this.goldVat = goldVat;
    }

    public Double getLaborVat() {
        return laborVat;
    }

    public void setLaborVat(Double laborVat) {
        this.laborVat = laborVat;
    }

    public List<KaratPrice> getKaratPriceList() {
        return karatPriceList;
    }

    public void setKaratPriceList(List<KaratPrice> karatPriceList) {
        this.karatPriceList = karatPriceList;
    }

    public String getKaratCode() {
        return karatCode;
    }

    public void setKaratCode(String karatCode) {
        this.karatCode = karatCode;
    }
}
