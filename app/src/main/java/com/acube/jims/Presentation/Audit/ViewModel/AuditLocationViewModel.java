package com.acube.jims.Presentation.Audit.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.models.Audit.ResponseLocationList;
import com.acube.jims.datalayer.repositories.Audit.AuditLocationRepository;
import com.acube.jims.datalayer.repositories.Audit.AuditRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AuditLocationViewModel extends AndroidViewModel {
    public AuditLocationRepository repository;
    private LiveData<List<ResponseLocationList>> liveData;

    public AuditLocationViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void Audit(String Auth, JsonObject jsonObject) {
        repository.AuditDetails(Auth,jsonObject);
    }

    public void init() {
        repository = new AuditLocationRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseLocationList>> getLiveData() {
        return liveData;
    }
}