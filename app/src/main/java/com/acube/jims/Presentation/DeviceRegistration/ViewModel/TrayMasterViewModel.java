package com.acube.jims.Presentation.DeviceRegistration.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceRegistration;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseTrayMaster;
import com.acube.jims.datalayer.repositories.DeviceRegistration.AddDeviceDetailsRepository;
import com.acube.jims.datalayer.repositories.DeviceRegistration.FetchTrayMasterRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TrayMasterViewModel extends AndroidViewModel {
    public FetchTrayMasterRepository repository;
    private LiveData<List<ResponseTrayMaster>> liveData;

    public TrayMasterViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void AddDeviceRegistrationDetails(String Token) {
        repository.TrayMaster(Token);
    }

    public void init() {
        repository = new FetchTrayMasterRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseTrayMaster>> getLiveData() {
        return liveData;
    }
}