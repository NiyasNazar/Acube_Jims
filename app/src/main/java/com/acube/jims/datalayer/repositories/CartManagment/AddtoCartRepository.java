package com.acube.jims.datalayer.repositories.CartManagment;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddtoCartRepository {
    private Application application;
    private MutableLiveData<ResponseAddtoCart> dataset;

    public AddtoCartRepository() {
        dataset = new MutableLiveData<>();
    }

    public void AddtoCart(String Auth, String type , JsonArray jsonArray) {
        RestApiService restApiService = RetrofitInstance.getApiService();



        Call<ResponseAddtoCart> call = restApiService.AddtoCart(Auth, type, jsonArray);
        call.enqueue(new Callback<ResponseAddtoCart>() {
            @Override
            public void onResponse(Call<ResponseAddtoCart> call, Response<ResponseAddtoCart> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {

                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAddtoCart> call, Throwable t) {

            }
        });

    }

    public LiveData<ResponseAddtoCart> getResponseLiveData() {
        return dataset;
    }
}
