package com.acube.jims.datalayer.repositories.Audit;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Audit.AuditScanUpload;
import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuditUploadRepository {
    private Application application;
    private final MutableLiveData<AuditScanUpload> dataset;

    public AuditUploadRepository() {
        dataset = new MutableLiveData<>();
    }

    public void AuditUpload(String header, JsonObject jsonObject) {

        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<AuditScanUpload> call = restApiService.AuditUpload(header, jsonObject);
        call.enqueue(new Callback<AuditScanUpload>() {
            @Override
            public void onResponse(Call<AuditScanUpload> call, Response<AuditScanUpload> response) {
                if (response.body() != null && response.code() == 200) {
                    dataset.setValue(response.body());
                } else {
                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<AuditScanUpload> call, Throwable t) {
                dataset.setValue(null);

            }
        });

    }

    public LiveData<AuditScanUpload> getResponseLiveData() {
        return dataset;
    }
}
