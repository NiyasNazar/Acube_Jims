package com.acube.jims.datalayer.repositories.Authentication;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.BaseActivity;
import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Authentication.ResponseCheckCustomer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckCustomerRepository  {
    private Application application;
    private final MutableLiveData<ResponseCheckCustomer> dataset;

    public CheckCustomerRepository() {
        dataset = new MutableLiveData<>();
    }

    public void CheckCustomer(String vaphonenum, Context context) {

        RestApiService restApiService = RetrofitInstance.getApiService(context);
        Call<ResponseCheckCustomer> call = restApiService.CheckUserExists(vaphonenum);
        call.enqueue(new Callback<ResponseCheckCustomer>() {
            @Override
            public void onResponse(Call<ResponseCheckCustomer> call, Response<ResponseCheckCustomer> response) {
                if (response.body() != null && response.code() == 200) {
                    dataset.setValue(response.body());
                } else {
                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseCheckCustomer> call, Throwable t) {
                dataset.setValue(null);

            }
        });

    }

    public LiveData<ResponseCheckCustomer> getResponseLiveData() {
        return dataset;
    }
}
