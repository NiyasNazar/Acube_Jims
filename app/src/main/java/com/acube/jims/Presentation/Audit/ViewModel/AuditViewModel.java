package com.acube.jims.Presentation.Audit.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.repositories.Audit.AuditRepository;
import com.acube.jims.datalayer.repositories.CartManagment.AddtoCartRepository;
import com.google.gson.JsonArray;
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