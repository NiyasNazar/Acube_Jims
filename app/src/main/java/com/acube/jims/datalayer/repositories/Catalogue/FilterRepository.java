package com.acube.jims.datalayer.repositories.Catalogue;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.Filter.ResponseFetchFilters;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterRepository {
    private Application application;
    private final MutableLiveData<ResponseFetchFilters> dataset;

    public FilterRepository() {
        dataset = new MutableLiveData<>();
    }

    public void FetchFilterItems() {
        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<ResponseFetchFilters> call = restApiService.getFilters();
        call.enqueue(new Callback<ResponseFetchFilters>() {
            @Override
            public void onResponse(Call<ResponseFetchFilters> call, Response<ResponseFetchFilters> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {

                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseFetchFilters> call, Throwable t) {

            }
        });

    }

    public LiveData<ResponseFetchFilters> getResponseLiveData() {
        return dataset;
    }
}
