package com.acube.jims.datalayer.repositories.Authentication;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCustomerRepository {
    private Application application;
    private MutableLiveData<JsonObject> dataset;

    public UpdateCustomerRepository() {
        dataset = new MutableLiveData<>();
    }

    public void UpdateCustomer(int ID, JsonObject jsonObject, Context context) {
        RestApiService restApiService = RetrofitInstance.getApiService(context);
        Call<JsonObject> call = restApiService.updateUser(ID,jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {

                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    public LiveData<JsonObject> getResponseLiveData() {
        return dataset;
    }
}
