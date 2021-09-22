package com.acube.jims.datalayer.repositories.Report;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Audit.ResponseReport;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerListing;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportRepository {
    private Application application;
    private final MutableLiveData<ResponseReport> dataset;

    public ReportRepository() {
        dataset = new MutableLiveData<>();
    }

    public void FetchReport(String Token, JsonObject jsonObject) {

        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<ResponseReport> call = restApiService.FetchReport(Token, jsonObject);
        call.enqueue(new Callback<ResponseReport>() {
            @Override
            public void onResponse(Call<ResponseReport> call, Response<ResponseReport> response) {
                if (response.body() != null && response.code() == 200) {
                    dataset.setValue(response.body());
                } else {
                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseReport> call, Throwable t) {
                dataset.setValue(null);

            }
        });

    }

    public LiveData<ResponseReport> getResponseLiveData() {
        return dataset;
    }
}
