package com.acube.jims.datalayer.repositories.Catalogue;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogDetails;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsRepository {
    private Application application;
    private MutableLiveData<ResponseCatalogDetails> dataset;

    public ItemDetailsRepository() {
        dataset = new MutableLiveData<>();
    }

    public void FetchItemDetails(String Auth, String ID) {
        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<ResponseCatalogDetails> call = restApiService.getItemDetails(Auth,ID);
        call.enqueue(new Callback<ResponseCatalogDetails>() {
            @Override
            public void onResponse(Call<ResponseCatalogDetails> call, Response<ResponseCatalogDetails> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {

                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseCatalogDetails> call, Throwable t) {
                dataset.setValue(null);
            }
        });

    }

    public LiveData<ResponseCatalogDetails> getResponseLiveData() {
        return dataset;
    }
}
