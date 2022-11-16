package com.acube.jims.datalayer.repositories.DeviceRegistration;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceRegistration;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseTrayMaster;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchTrayMasterRepository {
    private Application application;
    private MutableLiveData<List<ResponseTrayMaster>> dataset;

    public FetchTrayMasterRepository() {
        dataset = new MutableLiveData<>();
    }

    public void TrayMaster(String Token, Context context) {
        RestApiService restApiService = RetrofitInstance.getApiService(context);
        Call<List<ResponseTrayMaster>> call = restApiService.FetchTrayMaster(Token);
        call.enqueue(new Callback<List<ResponseTrayMaster>>() {
            @Override
            public void onResponse(Call<List<ResponseTrayMaster>> call, Response<List<ResponseTrayMaster>> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {

                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseTrayMaster>> call, Throwable t) {

            }
        });

    }

    public LiveData<List<ResponseTrayMaster>> getResponseLiveData() {
        return dataset;
    }
}
