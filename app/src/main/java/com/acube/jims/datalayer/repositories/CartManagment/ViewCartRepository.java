package com.acube.jims.datalayer.repositories.CartManagment;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Cart.ResponseCart;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCartRepository {
    private Application application;
    private MutableLiveData<ResponseCart> dataset;

    public ViewCartRepository() {
        dataset = new MutableLiveData<>();
    }

    public void ViewCart(String Auth, String CustomerID, Context context) {
        RestApiService restApiService = RetrofitInstance.getApiService(context);


        Call<ResponseCart> call = restApiService.ViewCart(Auth, CustomerID);
        call.enqueue(new Callback<ResponseCart>() {
            @Override
            public void onResponse(Call<ResponseCart> call, Response<ResponseCart> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {
                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseCart> call, Throwable t) {
                dataset.setValue(null);
            }
        });

    }

    public LiveData<ResponseCart> getResponseLiveData() {
        return dataset;
    }
}
