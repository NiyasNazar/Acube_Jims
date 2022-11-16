package com.acube.jims.datalayer.repositories.Authentication;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.BaseActivity;
import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Authentication.ResponseCheckCustomer;
import com.acube.jims.datalayer.models.Authentication.ResponseLogin;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private Application application;
    private final MutableLiveData<ResponseLogin> dataset;


    public LoginRepository() {
        dataset = new MutableLiveData<>();
    }

    public void Login(JsonObject jsonObject, Context context) {

        RestApiService restApiService = RetrofitInstance.getApiService(context);
        Call<ResponseLogin> call = restApiService.doLogin(jsonObject);
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
              //  Log.d("onFailure", "onFailure: " + response.errorBody()));
                if (response.body() != null && response.code() == 200) {
                    dataset.setValue(response.body());
                }else dataset.setValue(null);


            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                //   dataset.setValue(null);
                Log.d("onFailure", "onFailure: "+t.getMessage());
                dataset.setValue(null);
            }
        });

    }

    public LiveData<ResponseLogin> getResponseLiveData() {
        return dataset;
    }
}
