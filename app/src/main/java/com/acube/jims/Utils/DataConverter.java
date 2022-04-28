package com.acube.jims.Utils;

import androidx.room.TypeConverter;

import com.acube.jims.datalayer.models.Filter.SubCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {
    @TypeConverter // note this annotation
    public String fromOptionValuesList(List<SubCategory> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<SubCategory>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<SubCategory> toOptionValuesList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<SubCategory>>() {
        }.getType();
        List<SubCategory> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

}