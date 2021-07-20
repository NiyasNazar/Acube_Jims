package com.acube.jims.datalayer.repositories.DeviceRegistration;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceRegistration;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDeviceDetailsRepository {
    private Application application;
    private MutableLiveData<ResponseDeviceRegistration> dataset;

    public AddDeviceDetailsRepository() {
        dataset = new MutableLiveData<>();
    }

    public void RegisterDevice(String Token,JsonObject jsonObject) {
        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<ResponseDeviceRegistration> call = restApiService.registerDevice(Token,jsonObject);
        call.enqueue(new Callback<ResponseDeviceRegistration>() {
            @Override
            public void onResponse(Call<ResponseDeviceRegistration> call, Response<ResponseDeviceRegistration> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {

                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseDeviceRegistration> call, Throwable t) {

            }
        });

    }

    public LiveData<ResponseDeviceRegistration> getResponseLiveData() {
        return dataset;
    }
}
