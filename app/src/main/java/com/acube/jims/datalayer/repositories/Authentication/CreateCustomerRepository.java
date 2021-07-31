package com.acube.jims.datalayer.repositories.Authentication;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.BaseActivity;
import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Authentication.ResponseCheckCustomer;
import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCustomerRepository  {
    private Application application;
    private MutableLiveData<ResponseCreateCustomer> dataset;

    public CreateCustomerRepository() {
        dataset = new MutableLiveData<>();
    }

    public void CreateCustomer(JsonObject jsonObject) {
        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<ResponseCreateCustomer> call = restApiService.createUser(jsonObject);
        call.enqueue(new Callback<ResponseCreateCustomer>() {
            @Override
            public void onResponse(Call<ResponseCreateCustomer> call, Response<ResponseCreateCustomer> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {

                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseCreateCustomer> call, Throwable t) {

            }
        });

    }

    public LiveData<ResponseCreateCustomer> getResponseLiveData() {
        return dataset;
    }
}
