package com.acube.jims.datalayer.repositories.Customer;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerHistory;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerHistoryRepository {
    private Application application;
    private final MutableLiveData<ResponseCustomerHistory> dataset;

    public CustomerHistoryRepository() {
        dataset = new MutableLiveData<>();
    }

    public void CustomerHistory(String Token, int  customerID, Context context) {

        RestApiService restApiService = RetrofitInstance.getApiService(context);
        Call<ResponseCustomerHistory> call = restApiService.FetchCustomerHistory(Token, String.valueOf(customerID));
        call.enqueue(new Callback<ResponseCustomerHistory>() {
            @Override
            public void onResponse(Call<ResponseCustomerHistory> call, Response<ResponseCustomerHistory> response) {
                if (response.body() != null && response.code() == 200) {
                    dataset.setValue(response.body());
                } else {
                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseCustomerHistory> call, Throwable t) {
                dataset.setValue(null);
                Log.d("onChanged", "onChanged: "+t.getMessage());

            }
        });

    }

    public LiveData<ResponseCustomerHistory> getResponseLiveData() {
        return dataset;
    }
}
