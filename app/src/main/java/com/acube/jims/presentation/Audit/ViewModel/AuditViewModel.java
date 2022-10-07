package com.acube.jims.presentation.Audit.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Audit.AuditResults;
import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.models.Audit.ResponseErpAuditDownload;
import com.acube.jims.datalayer.repositories.Audit.AuditRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AuditViewModel extends AndroidViewModel {
    public AuditRepository repository;
    private LiveData<List<ResponseAudit>> liveDataheader;
    private LiveData<ResponseErpAuditDownload>liveData;

    public AuditViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void AuditHeader(String Auth, JsonObject jsonObject) {
        repository.AuditHeader(Auth,jsonObject);
    }
    public void AuditDetails(String Auth, JsonObject jsonObject) {
        repository.AuditDetails(Auth,jsonObject);
    }

    public void init() {
        repository = new AuditRepository();
        liveDataheader = repository.getResponseLiveData();
        liveData = repository.getResponseLiveDataDetails();
    }


    public LiveData<List<ResponseAudit>> getLiveDataHeader() {
        return liveDataheader;
    }
    public LiveData<ResponseErpAuditDownload> getLiveData() {
        return liveData;
    }
}