package com.acube.jims.presentation.DeviceRegistration.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.DeviceRegistration.ResponseTrayMaster;
import com.acube.jims.datalayer.repositories.DeviceRegistration.FetchTrayMasterRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TrayMasterViewModel extends AndroidViewModel {
    public FetchTrayMasterRepository repository;
    private LiveData<List<ResponseTrayMaster>> liveData;

    public TrayMasterViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void AddDeviceRegistrationDetails(String Token, Context context) {
        repository.TrayMaster(Token,context);
    }

    public void init() {
        repository = new FetchTrayMasterRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseTrayMaster>> getLiveData() {
        return liveData;
    }
}