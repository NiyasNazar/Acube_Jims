package com.acube.jims.datalayer.repositories.Catalogue;

import android.app.Application;

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

    public CatalogueRepository() {
        dataset = new MutableLiveData<>();
    }

    public void FetchCatalogueItems(int PageNum, int PageSize, String CatID, String SubCatID,String ColorCode,String KaratCode) {
        RestApiService restApiService = RetrofitInstance.getApiService();
        JsonObject jsonObject=new JsonObject();

        jsonObject.addProperty("pageNo",PageNum);
        jsonObject.addProperty("pageSize",PageSize);
        jsonObject.addProperty("categoryCode",CatID);
        jsonObject.addProperty("subCategoryCode",SubCatID);
        jsonObject.addProperty("karatCode",KaratCode);
        jsonObject.addProperty("colorCode",ColorCode);

        Call<List<ResponseCatalogueListing>> call = restApiService.getCatalogueItems(jsonObject);
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

            }
        });

    }

    public LiveData<List<ResponseCatalogueListing>> getResponseLiveData() {
        return dataset;
    }
}
