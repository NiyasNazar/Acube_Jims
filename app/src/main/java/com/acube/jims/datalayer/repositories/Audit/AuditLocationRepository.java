package com.acube.jims.datalayer.repositories.Audit;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.models.Audit.ResponseLocationList;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuditLocationRepository {
    private Application application;
    private final MutableLiveData<List<ResponseLocationList> >dataset;

    public AuditLocationRepository() {
        dataset = new MutableLiveData<>();
    }

    public void AuditDetails(String header, JsonObject jsonObject) {

        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<List<ResponseLocationList>> call = restApiService.AuditLocationList(header, jsonObject);
        call.enqueue(new Callback<List<ResponseLocationList>>() {
            @Override
            public void onResponse(Call<List<ResponseLocationList>> call, Response<List<ResponseLocationList>> response) {
                if (response.body() != null && response.code() == 200) {
                    dataset.setValue(response.body());
                } else {
                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseLocationList>> call, Throwable t) {
                dataset.setValue(null);

            }
        });

    }

    public LiveData<List<ResponseLocationList>> getResponseLiveData() {
        return dataset;
    }
}
