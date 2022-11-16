package com.acube.jims.datalayer.repositories.Favorites;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddtoFavoritesRepository {
    private Application application;
    private MutableLiveData<JsonObject> dataset;

    public AddtoFavoritesRepository() {
        dataset = new MutableLiveData<>();
    }

    public void AddtoFavorites(String Auth, String CustomerID, String EmployeeId, String itemID, String viewedOn, String type, String serialno, Context context) {
        RestApiService restApiService = RetrofitInstance.getApiService(context);
        JsonObject jsonObject = new JsonObject();


        jsonObject.addProperty("customerID", CustomerID);
        jsonObject.addProperty("employeeID", EmployeeId);
        jsonObject.addProperty("viewedOn", "");
        jsonObject.addProperty("serialNumber", serialno);
        jsonObject.addProperty("itemID", itemID);


        Call<JsonObject> call = restApiService.AddtoFavorites(Auth, type, jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {

                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dataset.setValue(null);
            }
        });

    }

    public LiveData<JsonObject> getResponseLiveData() {
        return dataset;
    }
}
