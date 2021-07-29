package com.acube.jims.datalayer.models.Catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCatalogueListing {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("itemDesc")
    @Expose
    private Object itemDesc;
    @SerializedName("itemCode")
    @Expose
    private String itemCode;
    @SerializedName("itemReqQty")
    @Expose
    private Object itemReqQty;
    @SerializedName("mrp")
    @Expose
    private Object mrp;
    @SerializedName("makingChargeMin")
    @Expose
    private Object makingChargeMin;
    @SerializedName("makingChargeMax")
    @Expose
    private Object makingChargeMax;
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
    private Object karatID;
    @SerializedName("karatName")
    @Expose
    private Object karatName;
    @SerializedName("colorID")
    @Expose
    private Object colorID;
    @SerializedName("colorName")
    @Expose
    private Object colorName;
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
    private Object emarladWeight;
    @SerializedName("grossWeight")
    @Expose
    private Object grossWeight;
    @SerializedName("stoneWeight")
    @Expose
    private Object stoneWeight;
    @SerializedName("remarks")
    @Expose
    private Object remarks;
    @SerializedName("imageList")
    @Expose
    private Object imageList;
    @SerializedName("itemSubList")
    @Expose
    private List<ItemSub> itemSubList = null;
    @SerializedName("itemCollectionID")
    @Expose
    private Object itemCollectionID;
    @SerializedName("itemCollectionName")
    @Expose
    private Object itemCollectionName;
    @SerializedName("itemFamilyID")
    @Expose
    private Object itemFamilyID;
    @SerializedName("itemFamilyName")
    @Expose
    private Object itemFamilyName;
    @SerializedName("itemGroupID")
    @Expose
    private Object itemGroupID;
    @SerializedName("itemGroupName")
    @Expose
    private Object itemGroupName;
    @SerializedName("itemBrandID")
    @Expose
    private Integer itemBrandID;
    @SerializedName("itemBrandName")
    @Expose
    private String itemBrandName;

    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;



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

    public Object getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(Object itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Object getItemReqQty() {
        return itemReqQty;
    }

    public void setItemReqQty(Object itemReqQty) {
        this.itemReqQty = itemReqQty;
    }

    public Object getMrp() {
        return mrp;
    }

    public void setMrp(Object mrp) {
        this.mrp = mrp;
    }

    public Object getMakingChargeMin() {
        return makingChargeMin;
    }

    public void setMakingChargeMin(Object makingChargeMin) {
        this.makingChargeMin = makingChargeMin;
    }

    public Object getMakingChargeMax() {
        return makingChargeMax;
    }

    public void setMakingChargeMax(Object makingChargeMax) {
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

    public Object getKaratID() {
        return karatID;
    }

    public void setKaratID(Object karatID) {
        this.karatID = karatID;
    }

    public Object getKaratName() {
        return karatName;
    }

    public void setKaratName(Object karatName) {
        this.karatName = karatName;
    }

    public Object getColorID() {
        return colorID;
    }

    public void setColorID(Object colorID) {
        this.colorID = colorID;
    }

    public Object getColorName() {
        return colorName;
    }

    public void setColorName(Object colorName) {
        this.colorName = colorName;
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

    public Object getEmarladWeight() {
        return emarladWeight;
    }

    public void setEmarladWeight(Object emarladWeight) {
        this.emarladWeight = emarladWeight;
    }

    public Object getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Object grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Object getStoneWeight() {
        return stoneWeight;
    }

    public void setStoneWeight(Object stoneWeight) {
        this.stoneWeight = stoneWeight;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
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

    public Object getItemCollectionID() {
        return itemCollectionID;
    }

    public void setItemCollectionID(Object itemCollectionID) {
        this.itemCollectionID = itemCollectionID;
    }

    public Object getItemCollectionName() {
        return itemCollectionName;
    }

    public void setItemCollectionName(Object itemCollectionName) {
        this.itemCollectionName = itemCollectionName;
    }

    public Object getItemFamilyID() {
        return itemFamilyID;
    }

    public void setItemFamilyID(Object itemFamilyID) {
        this.itemFamilyID = itemFamilyID;
    }

    public Object getItemFamilyName() {
        return itemFamilyName;
    }

    public void setItemFamilyName(Object itemFamilyName) {
        this.itemFamilyName = itemFamilyName;
    }

    public Object getItemGroupID() {
        return itemGroupID;
    }

    public void setItemGroupID(Object itemGroupID) {
        this.itemGroupID = itemGroupID;
    }

    public Object getItemGroupName() {
        return itemGroupName;
    }

    public void setItemGroupName(Object itemGroupName) {
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

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
