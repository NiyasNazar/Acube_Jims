package com.acube.jims.datalayer.repositories.HomePage;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerListing;
import com.acube.jims.datalayer.models.HomePage.HomeData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerRepository {
    private Application application;
    private final MutableLiveData<List<ResponseCustomerListing>> dataset;

    public CustomerRepository() {
        dataset = new MutableLiveData<>();
    }

    public void getCustomer(String Token, String keyword, Context context) {

        RestApiService restApiService = RetrofitInstance.getApiService(context);
        Call<List<ResponseCustomerListing>> call = restApiService.getCustomer(Token,keyword);
        call.enqueue(new Callback<List<ResponseCustomerListing>>() {
            @Override
            public void onResponse(Call<List<ResponseCustomerListing>> call, Response<List<ResponseCustomerListing>> response) {
                if (response.body() != null && response.code() == 200) {
                    dataset.setValue(response.body());
                } else {
                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseCustomerListing>> call, Throwable t) {
                dataset.setValue(null);

            }
        });

    }

    public LiveData<List<ResponseCustomerListing>>getResponseLiveData() {
        return dataset;
    }
}
