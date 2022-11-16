package com.acube.jims.datalayer.repositories.Catalogue;

import android.app.Application;
import android.content.Context;

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
    private final MutableLiveData<List<ResponseFetchFilters> > dataset;

    public FilterRepository() {
        dataset = new MutableLiveData<>();
    }

    public void FetchFilterItems(String Auth, Context context) {
        RestApiService restApiService = RetrofitInstance.getApiService(context);
        Call<List<ResponseFetchFilters> > call = restApiService.getFilters(Auth);
        call.enqueue(new Callback<List<ResponseFetchFilters> >() {
            @Override
            public void onResponse(Call<List<ResponseFetchFilters> > call, Response<List<ResponseFetchFilters> > response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {

                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseFetchFilters> > call, Throwable t) {

            }
        });

    }

    public LiveData<List<ResponseFetchFilters> > getResponseLiveData() {
        return dataset;
    }
}
