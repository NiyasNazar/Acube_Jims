package com.acube.jims.datalayer.repositories.DeviceRegistration;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceUpdation;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseGetRegistered;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateRegistrationRepository {
    private Application application;
    private final MutableLiveData<ResponseDeviceUpdation> dataset;

    public UpdateRegistrationRepository() {
        dataset = new MutableLiveData<>();
    }

    public void UpdateDeviceRegistration(String Token, String key, JsonObject body, Context context) {

        RestApiService restApiService = RetrofitInstance.getApiService(context);
        Call<ResponseDeviceUpdation> call = restApiService.updateDeviceRegistration(Token, key, body);
        call.enqueue(new Callback<ResponseDeviceUpdation>() {
            @Override
            public void onResponse(Call<ResponseDeviceUpdation> call, Response<ResponseDeviceUpdation> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {
                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseDeviceUpdation> call, Throwable t) {
                dataset.setValue(null);

            }
        });

    }

    public LiveData<ResponseDeviceUpdation> getResponseLiveData() {
        return dataset;
    }
}
