package com.acube.jims.datalayer.repositories.Compare;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Authentication.ResponseCheckCustomer;
import com.acube.jims.datalayer.models.Compare.ResponseCompare;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompareRepository {
    private Application application;
    private final MutableLiveData<List<ResponseCompare>> dataset;

    public CompareRepository() {
        dataset = new MutableLiveData<>();
    }

    public void CompareItems(String header, JsonObject jsonObject, Context context) {

        RestApiService restApiService = RetrofitInstance.getApiService(context);
        Call<List<ResponseCompare>> call = restApiService.compareList(header,jsonObject);
        call.enqueue(new Callback<List<ResponseCompare>>() {
            @Override
            public void onResponse(Call<List<ResponseCompare>> call, Response<List<ResponseCompare>> response) {
                if (response.body() != null && response.code() == 200) {
                    dataset.setValue(response.body());
                } else {
                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseCompare>> call, Throwable t) {
                dataset.setValue(null);

            }
        });

    }

    public LiveData<List<ResponseCompare>> getResponseLiveData() {
        return dataset;
    }
}
