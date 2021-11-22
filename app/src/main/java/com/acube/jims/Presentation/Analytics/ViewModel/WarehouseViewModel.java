package com.acube.jims.Presentation.Analytics.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Analytics.ResponseAnalyticsGraph;
import com.acube.jims.datalayer.models.Analytics.ResponseAnalyticsSummary;
import com.acube.jims.datalayer.models.warehouse.ResponseWareHouse;
import com.acube.jims.datalayer.repositories.Analytics.AnalyticsRepository;
import com.acube.jims.datalayer.repositories.Warehouse.WarehouseRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WarehouseViewModel extends AndroidViewModel {
    public WarehouseRepository repository;
    private LiveData<List<ResponseWareHouse>> liveData;


    public WarehouseViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void FetchWareHouses(String Auth) {
        repository.FetchWareHouses(Auth);
    }


    public void init() {
        repository = new WarehouseRepository();
        liveData = repository.getResponseLiveData();

    }


    public LiveData<List<ResponseWareHouse>> getLiveData() {
        return liveData;
    }

}