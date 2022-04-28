package com.acube.jims.presentation.DeviceRegistration.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceRegistration;
import com.acube.jims.datalayer.repositories.DeviceRegistration.AddDeviceDetailsRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

public class AddDeviceViewModel extends AndroidViewModel {
    public AddDeviceDetailsRepository repository;
    private LiveData<ResponseDeviceRegistration> liveData;

    public AddDeviceViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void AddDeviceRegistrationDetails(String Token,JsonObject jsonObject) {
        repository.RegisterDevice(Token,jsonObject);
    }

    public void init() {
        repository = new AddDeviceDetailsRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseDeviceRegistration> getLiveData() {
        return liveData;
    }
}