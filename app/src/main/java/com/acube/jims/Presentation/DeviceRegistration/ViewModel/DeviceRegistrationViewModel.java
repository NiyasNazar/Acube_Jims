package com.acube.jims.Presentation.DeviceRegistration.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.DeviceRegistration.ResponseGetRegistered;
import com.acube.jims.datalayer.repositories.DeviceRegistration.GetRegistrationRepository;

import org.jetbrains.annotations.NotNull;

public class DeviceRegistrationViewModel extends AndroidViewModel {
    public GetRegistrationRepository repository;
    private LiveData<ResponseGetRegistered> liveData;

    public DeviceRegistrationViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void GetDeviceRegistrationDetails(String Token,String vaMacaddress) {
        repository.CheckDeviceRegistration(Token,vaMacaddress);
    }

    public void init() {
        repository = new GetRegistrationRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseGetRegistered> getLiveData() {
        return liveData;
    }
}