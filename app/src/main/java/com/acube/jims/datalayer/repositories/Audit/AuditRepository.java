package com.acube.jims.datalayer.repositories.Audit;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Audit.AuditResults;
import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.models.Authentication.ResponseCheckCustomer;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuditRepository {
    private Application application;
    private final MutableLiveData<List<ResponseAudit> >dataset;
    private final MutableLiveData<List<ResponseAudit> >datasetresults;

    public AuditRepository() {
        dataset = new MutableLiveData<>();
        datasetresults = new MutableLiveData<>();
    }
    public void AuditHeader(String header, JsonObject jsonObject) {

        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<List<ResponseAudit>> call = restApiService.AuditHeader(header, jsonObject);
        call.enqueue(new Callback<List<ResponseAudit>>() {
            @Override
            public void onResponse(Call<List<ResponseAudit>> call, Response<List<ResponseAudit>> response) {
                if (response.body() != null && response.code() == 200) {
                    dataset.setValue(response.body());
                } else {
                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseAudit>> call, Throwable t) {
                dataset.setValue(null);

            }
        });

    }
    public void AuditDetails(String header, JsonObject jsonObject) {

        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<List<ResponseAudit>> call = restApiService.AuditDetails(header, jsonObject);
        call.enqueue(new Callback<List<ResponseAudit>>() {
            @Override
            public void onResponse(Call<List<ResponseAudit>> call, Response<List<ResponseAudit>> response) {
                if (response.body() != null && response.code() == 200) {
                    datasetresults.setValue(response.body());
                } else {
                    datasetresults.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseAudit>> call, Throwable t) {
                datasetresults.setValue(null);

            }
        });

    }

    public LiveData<List<ResponseAudit>> getResponseLiveData() {
        return dataset;
    }

    public LiveData<List<ResponseAudit>> getResponseLiveDataDetails() {
        return datasetresults;
    }
}
