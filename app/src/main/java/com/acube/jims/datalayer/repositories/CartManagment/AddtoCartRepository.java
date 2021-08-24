package com.acube.jims.datalayer.repositories.CartManagment;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
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

    public void AddtoCart(String cartID,String Auth, String CustomerID, String EmployeeId, String ItemId, String type,String qty,String serialno) {
        RestApiService restApiService = RetrofitInstance.getApiService();
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("cartListNo", cartID);
        jsonObject.addProperty("customerID", CustomerID);
        jsonObject.addProperty("employeeID", EmployeeId);
        jsonObject.addProperty("serialNumber", serialno);
        jsonObject.addProperty("itemID", ItemId);
        jsonObject.addProperty("qty", qty);


        Call<ResponseAddtoCart> call = restApiService.AddtoCart(Auth, type, jsonObject);
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
