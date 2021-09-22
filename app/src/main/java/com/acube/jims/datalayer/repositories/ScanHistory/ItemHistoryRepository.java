package com.acube.jims.datalayer.repositories.ScanHistory;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Invoice.ResponseInvoiceList;
import com.acube.jims.datalayer.models.ScanHistory.ResponseScanHistory;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemHistoryRepository {
    private Application application;
    private MutableLiveData<List<ResponseScanHistory>> dataset;

    public ItemHistoryRepository() {
        dataset = new MutableLiveData<>();
    }

    public void ItemHistory(String Auth, JsonObject jsonObject) {
        RestApiService restApiService = RetrofitInstance.getApiService();


        Call<List<ResponseScanHistory>> call = restApiService.ItemHistory(Auth, jsonObject);
        call.enqueue(new Callback<List<ResponseScanHistory>>() {
            @Override
            public void onResponse(Call<List<ResponseScanHistory>> call, Response<List<ResponseScanHistory>> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {

                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseScanHistory>> call, Throwable t) {
                dataset.setValue(null);
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });

    }

    public LiveData<List<ResponseScanHistory>> getResponseLiveData() {
        return dataset;
    }
}
