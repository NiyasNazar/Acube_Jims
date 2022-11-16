package com.acube.jims.datalayer.repositories.Favorites;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Cart.ResponseCart;
import com.acube.jims.datalayer.models.ConsignmentLine;
import com.acube.jims.datalayer.models.Favorites.ResponseFavorites;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewFavoritesRepository {
    private Application application;
    private MutableLiveData<List<ResponseFavorites>>  dataset;

    public ViewFavoritesRepository() {
        dataset = new MutableLiveData<>();
    }

    public void ViewCart(String Auth, String CustomerID, Context context) {
        RestApiService restApiService = RetrofitInstance.getApiService(context);


        Call<List<ResponseFavorites>>  call = restApiService.ViewFavorites(Auth, CustomerID);
        call.enqueue(new Callback<List<ResponseFavorites>> () {
            @Override
            public void onResponse(Call<List<ResponseFavorites>>  call, Response<List<ResponseFavorites>>  response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {
                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseFavorites>> call, Throwable t) {
                dataset.setValue(null);
            }
        });

    }

    public LiveData<List<ResponseFavorites>>  getResponseLiveData() {
        return dataset;
    }
}
