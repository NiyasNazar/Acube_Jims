package com.acube.jims.datalayer.repositories.DeviceRegistration;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Authentication.ResponseCheckCustomer;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseGetRegistered;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetRegistrationRepository {
    private Application application;
    private final MutableLiveData<ResponseGetRegistered> dataset;

    public GetRegistrationRepository() {
        dataset = new MutableLiveData<>();
    }

    public void CheckDeviceRegistration(String Token,String vaMacaddress) {

        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<ResponseGetRegistered> call = restApiService.getRegisteredDetails(Token,vaMacaddress);
        call.enqueue(new Callback<ResponseGetRegistered>() {
            @Override
            public void onResponse(Call<ResponseGetRegistered> call, Response<ResponseGetRegistered> response) {
                if (response.body() != null && response.code() == 200) {
                    dataset.setValue(response.body());
                } else {
                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseGetRegistered> call, Throwable t) {
                dataset.setValue(null);

            }
        });

    }

    public LiveData<ResponseGetRegistered> getResponseLiveData() {
        return dataset;
    }
}
