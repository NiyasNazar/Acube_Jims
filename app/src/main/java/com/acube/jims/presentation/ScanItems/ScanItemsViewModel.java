package com.acube.jims.presentation.ScanItems;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.repositories.Common.ItemDetailsFromServerRepository;
import com.google.gson.JsonObject;

import java.util.List;

public class ScanItemsViewModel  extends AndroidViewModel {
    public ItemDetailsFromServerRepository repository;
    private LiveData<List<ResponseItems>> liveData;

    public ScanItemsViewModel(@NonNull Application application) {
        super(application);
    }


    public void getcompareItems(String Token, JsonObject jsonObject) {
        repository.ResponseItems(Token, jsonObject);

    }

    public void init() {
        repository = new ItemDetailsFromServerRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseItems>> getLiveData() {
        return liveData;
    }
}