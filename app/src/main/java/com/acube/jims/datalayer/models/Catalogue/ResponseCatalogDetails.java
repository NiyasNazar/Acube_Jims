package com.acube.jims.datalayer.models.Catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCatalogDetails {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("itemDesc")
    @Expose
    private String itemDesc;
    @SerializedName("itemCode")
    @Expose
    private String itemCode;
    @SerializedName("itemReqQty")
    @Expose
    private Double itemReqQty;
    @SerializedName("mrp")
    @Expose
    private Double mrp;
    @SerializedName("makingChargeMin")
    @Expose
    private Double makingChargeMin;
    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    @SerializedName("makingChargeMax")
    @Expose
    private Double makingChargeMax;
    @SerializedName("categoryID")
    @Expose
    private Integer categoryID;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("subCategoryID")
    @Expose
    private Integer subCategoryID;
    @SerializedName("subCategoryName")
    @Expose
    private String subCategoryName;
    @SerializedName("karatID")
    @Expose
    private Integer karatID;
    @SerializedName("karatName")
    @Expose
    private String karatName;
    @SerializedName("colorID")
    @Expose
    private Integer colorID;
    @SerializedName("colorName")
    @Expose
    private String colorName;
    @SerializedName("colorCode")
    @Expose
    private String colorCode;
    @SerializedName("uomid")
    @Expose
    private Integer uomid;
    @SerializedName("uomName")
    @Expose
    private String uomName;
    @SerializedName("expiryValue")
    @Expose
    private Object expiryValue;
    @SerializedName("expiryPeriod")
    @Expose
    private Object expiryPeriod;
    @SerializedName("emarladWeight")
    @Expose
    private Double emarladWeight;
    @SerializedName("grossWeight")
    @Expose
    private Double grossWeight;
    @SerializedName("stoneWeight")
    @Expose
    private Double stoneWeight;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("imageList")
    @Expose
    private Object imageList;
    @SerializedName("itemSubList")
    @Expose
    private List<ItemSub> itemSubList = null;
    @SerializedName("itemCollectionID")
    @Expose
    private Integer itemCollectionID;
    @SerializedName("itemCollectionName")
    @Expose
    private String itemCollectionName;
    @SerializedName("itemFamilyID")
    @Expose
    private Integer itemFamilyID;
    @SerializedName("itemFamilyName")
    @Expose
    private String itemFamilyName;
    @SerializedName("itemGroupID")
    @Expose
    private Integer itemGroupID;
    @SerializedName("itemGroupName")
    @Expose
    private String itemGroupName;
    @SerializedName("itemBrandID")
    @Expose
    private Integer itemBrandID;
    @SerializedName("itemBrandName")
    @Expose
    private String itemBrandName;
    @SerializedName("totalCount")
    @Expose
    private Object totalCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Double getItemReqQty() {
        return itemReqQty;
    }

    public void setItemReqQty(Double itemReqQty) {
        this.itemReqQty = itemReqQty;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Double getMakingChargeMin() {
        return makingChargeMin;
    }

    public void setMakingChargeMin(Double makingChargeMin) {
        this.makingChargeMin = makingChargeMin;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getMakingChargeMax() {
        return makingChargeMax;
    }

    public void setMakingChargeMax(Double makingChargeMax) {
        this.makingChargeMax = makingChargeMax;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getSubCategoryID() {
        return subCategoryID;
    }

    public void setSubCategoryID(Integer subCategoryID) {
        this.subCategoryID = subCategoryID;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public Integer getKaratID() {
        return karatID;
    }

    public void setKaratID(Integer karatID) {
        this.karatID = karatID;
    }

    public String getKaratName() {
        return karatName;
    }

    public void setKaratName(String karatName) {
        this.karatName = karatName;
    }

    public Integer getColorID() {
        return colorID;
    }

    public void setColorID(Integer colorID) {
        this.colorID = colorID;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Integer getUomid() {
        return uomid;
    }

    public void setUomid(Integer uomid) {
        this.uomid = uomid;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public Object getExpiryValue() {
        return expiryValue;
    }

    public void setExpiryValue(Object expiryValue) {
        this.expiryValue = expiryValue;
    }

    public Object getExpiryPeriod() {
        return expiryPeriod;
    }

    public void setExpiryPeriod(Object expiryPeriod) {
        this.expiryPeriod = expiryPeriod;
    }

    public Double getEmarladWeight() {
        return emarladWeight;
    }

    public void setEmarladWeight(Double emarladWeight) {
        this.emarladWeight = emarladWeight;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double getStoneWeight() {
        return stoneWeight;
    }

    public void setStoneWeight(Double stoneWeight) {
        this.stoneWeight = stoneWeight;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Object getImageList() {
        return imageList;
    }

    public void setImageList(Object imageList) {
        this.imageList = imageList;
    }

    public List<ItemSub> getItemSubList() {
        return itemSubList;
    }

    public void setItemSubList(List<ItemSub> itemSubList) {
        this.itemSubList = itemSubList;
    }

    public Integer getItemCollectionID() {
        return itemCollectionID;
    }

    public void setItemCollectionID(Integer itemCollectionID) {
        this.itemCollectionID = itemCollectionID;
    }

    public String getItemCollectionName() {
        return itemCollectionName;
    }

    public void setItemCollectionName(String itemCollectionName) {
        this.itemCollectionName = itemCollectionName;
    }

    public Integer getItemFamilyID() {
        return itemFamilyID;
    }

    public void setItemFamilyID(Integer itemFamilyID) {
        this.itemFamilyID = itemFamilyID;
    }

    public String getItemFamilyName() {
        return itemFamilyName;
    }

    public void setItemFamilyName(String itemFamilyName) {
        this.itemFamilyName = itemFamilyName;
    }

    public Integer getItemGroupID() {
        return itemGroupID;
    }

    public void setItemGroupID(Integer itemGroupID) {
        this.itemGroupID = itemGroupID;
    }

    public String getItemGroupName() {
        return itemGroupName;
    }

    public void setItemGroupName(String itemGroupName) {
        this.itemGroupName = itemGroupName;
    }

    public Integer getItemBrandID() {
        return itemBrandID;
    }

    public void setItemBrandID(Integer itemBrandID) {
        this.itemBrandID = itemBrandID;
    }

    public String getItemBrandName() {
        return itemBrandName;
    }

    public void setItemBrandName(String itemBrandName) {
        this.itemBrandName = itemBrandName;
    }

    public Object getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Object totalCount) {
        this.totalCount = totalCount;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
