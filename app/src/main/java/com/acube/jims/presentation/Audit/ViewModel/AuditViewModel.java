package com.acube.jims.presentation.Audit.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.repositories.Audit.AuditRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AuditViewModel extends AndroidViewModel {
    public AuditRepository repository;
    private LiveData<List<ResponseAudit>> liveData;

    public AuditViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void Audit(String Auth, JsonObject jsonObject) {
        repository.AuditDetails(Auth,jsonObject);
    }

    public void init() {
        repository = new AuditRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseAudit>> getLiveData() {
        return liveData;
    }
}