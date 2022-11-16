package com.acube.jims.presentation.DeviceRegistration.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceUpdation;
import com.acube.jims.datalayer.repositories.DeviceRegistration.UpdateRegistrationRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

public class UpdateDeviceViewModel extends AndroidViewModel {
    public UpdateRegistrationRepository repository;
    private LiveData<ResponseDeviceUpdation> liveData;

    public UpdateDeviceViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void UpdateDeviceRegistrationDetails(String Token, String key, JsonObject jsonObject, Context context) {
        repository.UpdateDeviceRegistration(Token, key, jsonObject,context);
    }

    public void init() {
        repository = new UpdateRegistrationRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseDeviceUpdation> getLiveData() {
        return liveData;
    }
}