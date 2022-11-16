package com.acube.jims.datalayer.repositories.Common;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.presentation.ScanItems.ResponseItems;
import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsFromServerRepository {
    private Application application;
    private final MutableLiveData<List<ResponseItems>> dataset;

    public ItemDetailsFromServerRepository() {
        dataset = new MutableLiveData<>();
    }

    public void ResponseItems(String header, JsonObject jsonObject, Context context) {

        RestApiService restApiService = RetrofitInstance.getApiService(context);
        Call<List<ResponseItems>> call = restApiService.ItemList(header,jsonObject);
        call.enqueue(new Callback<List<ResponseItems>>() {
            @Override
            public void onResponse(Call<List<ResponseItems>> call, Response<List<ResponseItems>> response) {
                if (response.body() != null && response.code() == 200) {
                    dataset.setValue(response.body());
                } else {
                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseItems>> call, Throwable t) {
                dataset.setValue(null);

            }
        });

    }

    public LiveData<List<ResponseItems>> getResponseLiveData() {
        return dataset;
    }
}
