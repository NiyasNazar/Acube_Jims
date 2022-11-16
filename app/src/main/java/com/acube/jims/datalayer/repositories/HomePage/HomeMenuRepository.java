package com.acube.jims.datalayer.repositories.HomePage;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseGetRegistered;
import com.acube.jims.datalayer.models.HomePage.HomeData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeMenuRepository {
    private Application application;
    private final MutableLiveData<List<HomeData>> dataset;

    public HomeMenuRepository() {
        dataset = new MutableLiveData<>();
    }

    public void getHomeMneu(String Token, String vaAppname, String vaRole, Context context) {

        RestApiService restApiService = RetrofitInstance.getApiService(context);
        Call<List<HomeData>> call = restApiService.getHomeMenu(Token,vaAppname,vaRole);
        call.enqueue(new Callback<List<HomeData>>() {
            @Override
            public void onResponse(Call<List<HomeData>> call, Response<List<HomeData>> response) {
                if (response.body() != null && response.code() == 200) {
                    dataset.setValue(response.body());
                } else {
                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<HomeData>> call, Throwable t) {
                dataset.setValue(null);

            }
        });

    }

    public LiveData<List<HomeData>>getResponseLiveData() {
        return dataset;
    }
}
