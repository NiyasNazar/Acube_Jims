package com.acube.jims.presentation.Analytics.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.warehouse.ResponseWareHouse;
import com.acube.jims.datalayer.repositories.Warehouse.WarehouseRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WarehouseViewModel extends AndroidViewModel {
    public WarehouseRepository repository;
    private LiveData<List<ResponseWareHouse>> liveData;


    public WarehouseViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void FetchWareHouses(String Auth, Context context) {
        repository.FetchWareHouses(Auth,context);
    }


    public void init() {
        repository = new WarehouseRepository();
        liveData = repository.getResponseLiveData();

    }


    public LiveData<List<ResponseWareHouse>> getLiveData() {
        return liveData;
    }

}