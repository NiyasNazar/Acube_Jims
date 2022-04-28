package com.acube.jims.presentation.Audit.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Audit.AuditScanUpload;
import com.acube.jims.datalayer.repositories.Audit.AuditUploadRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

public class AuditUploadViewModel extends AndroidViewModel {
    public AuditUploadRepository repository;
    private LiveData<AuditScanUpload> liveData;

    public AuditUploadViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void Audit(String Auth, JsonObject jsonObject) {
        repository.AuditUpload(Auth,jsonObject);
    }

    public void init() {
        repository = new AuditUploadRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<AuditScanUpload> getLiveData() {
        return liveData;
    }
}