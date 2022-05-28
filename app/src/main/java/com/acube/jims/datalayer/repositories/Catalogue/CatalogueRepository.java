package com.acube.jims.datalayer.repositories.Catalogue;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceRegistration;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogueRepository {
    private Application application;
    private MutableLiveData<List<ResponseCatalogueListing>> dataset;
    private MutableLiveData<List<ResponseCatalogueListing>> datasetsummary;

    public CatalogueRepository() {
        dataset = new MutableLiveData<>();
        datasetsummary = new MutableLiveData<>();
    }
    public void FetchCatalogueSummary(String Auth,int PageNum, int PageSize, String CatID, String SubCatID,String ColorCode,String KaratCode,String MinWeight,String MaxWeight,String priceMin,String priceMax,String gender,int customerID) {
        RestApiService restApiService = RetrofitInstance.getApiService();
        JsonObject jsonObject=new JsonObject();

        jsonObject.addProperty("pageNo",PageNum);
        jsonObject.addProperty("pageSize",PageSize);
        jsonObject.addProperty("categoryCode",CatID);
        jsonObject.addProperty("subCategoryCode",SubCatID);
        jsonObject.addProperty("karatCode",KaratCode);
        jsonObject.addProperty("colorCode",ColorCode);
        jsonObject.addProperty("minWeight",MinWeight);
        jsonObject.addProperty("maxWeight",MaxWeight);

        jsonObject.addProperty("minPrice",priceMin);
        jsonObject.addProperty("maxPrice",priceMax);
        jsonObject.addProperty("gender",gender);
        jsonObject.addProperty("customerID",customerID);

        Call<List<ResponseCatalogueListing>> call = restApiService.getCatalogueSummary(Auth,jsonObject);
        call.enqueue(new Callback<List<ResponseCatalogueListing>>() {
            @Override
            public void onResponse(Call<List<ResponseCatalogueListing>> call, Response<List<ResponseCatalogueListing>> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    datasetsummary.setValue(response.body());
                } else {

                    datasetsummary.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseCatalogueListing>> call, Throwable t) {
                datasetsummary.setValue(null);
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });

    }

    public void FetchCatalogueItems(String Auth,int PageNum, int PageSize, String CatID, String SubCatID,String ColorCode,String KaratCode,String MinWeight,String MaxWeight,String priceMin,String priceMax,String gender,int ID,int customerID) {
        RestApiService restApiService = RetrofitInstance.getApiService();
        JsonObject jsonObject=new JsonObject();

        jsonObject.addProperty("pageNo",PageNum);
        jsonObject.addProperty("pageSize",PageSize);
        jsonObject.addProperty("categoryCode",CatID);
        jsonObject.addProperty("subCategoryCode",SubCatID);
        jsonObject.addProperty("karatCode",KaratCode);
        jsonObject.addProperty("colorCode",ColorCode);
        jsonObject.addProperty("minWeight",MinWeight);
        jsonObject.addProperty("maxWeight",MaxWeight);
        jsonObject.addProperty("minPrice",priceMin);
        jsonObject.addProperty("maxPrice",priceMax);
        jsonObject.addProperty("gender",gender);
        jsonObject.addProperty("itemID",ID);
        jsonObject.addProperty("customerID",customerID);


        Call<List<ResponseCatalogueListing>> call = restApiService.getCatalogueItems(Auth,jsonObject);
        call.enqueue(new Callback<List<ResponseCatalogueListing>>() {
            @Override
            public void onResponse(Call<List<ResponseCatalogueListing>> call, Response<List<ResponseCatalogueListing>> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {

                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseCatalogueListing>> call, Throwable t) {
                dataset.setValue(null);
            }
        });

    }

    public LiveData<List<ResponseCatalogueListing>> getResponseLiveData() {
        return dataset;
    }

    public LiveData<List<ResponseCatalogueListing>> getResponseLiveDataSummary() {
        return datasetsummary;
    }
}
