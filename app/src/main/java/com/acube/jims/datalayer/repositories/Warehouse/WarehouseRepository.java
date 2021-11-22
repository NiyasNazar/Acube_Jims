package com.acube.jims.datalayer.repositories.Warehouse;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Audit.ResponseReport;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseTrayMaster;
import com.acube.jims.datalayer.models.warehouse.ResponseWareHouse;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarehouseRepository {

    private Application application;
    private final MutableLiveData<List<ResponseWareHouse>> dataset;

    public WarehouseRepository() {
        dataset = new MutableLiveData<>();
    }

    public void FetchWareHouses(String Token) {
        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<List<ResponseWareHouse>> call = restApiService.Fetchwarehouse(Token);
        call.enqueue(new Callback<List<ResponseWareHouse>>() {
            @Override
            public void onResponse(Call<List<ResponseWareHouse>> call, Response<List<ResponseWareHouse>> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {

                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseWareHouse>> call, Throwable t) {

            }
        });

    }

    public LiveData<List<ResponseWareHouse>> getResponseLiveData() {
        return dataset;
    }
}

