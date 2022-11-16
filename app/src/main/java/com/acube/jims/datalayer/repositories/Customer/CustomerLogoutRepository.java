package com.acube.jims.datalayer.repositories.Customer;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerListing;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerLogoutRepository {
    private Application application;
    private final MutableLiveData<JsonObject> dataset;

    public CustomerLogoutRepository() {
        dataset = new MutableLiveData<>();
    }

    public void CustomerLogout(String Token, JsonObject jsonObject, Context context) {

        RestApiService restApiService = RetrofitInstance.getApiService(context);
        Call<JsonObject> call = restApiService.CustomerLogout(Token, jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.code() == 200) {
                    dataset.setValue(response.body());
                } else {
                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dataset.setValue(null);

            }
        });

    }

    public LiveData<JsonObject> getResponseLiveData() {
        return dataset;
    }
}
