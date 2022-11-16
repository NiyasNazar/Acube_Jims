package com.acube.jims.presentation.ScanHistory;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.ScanHistory.ResponseScanHistory;
import com.acube.jims.datalayer.repositories.ScanHistory.ItemHistoryRepository;
import com.google.gson.JsonObject;

import java.util.List;

public class ItemHistoryViewModel extends AndroidViewModel {
    public ItemHistoryRepository repository;
    private LiveData<List<ResponseScanHistory>> liveData;

    public ItemHistoryViewModel(@NonNull Application application) {
        super(application);
    }


    public void ItemHistory(String Token, JsonObject jsonObject, Context context) {
        repository.ItemHistory(Token, jsonObject,context);

    }

    public void init() {
        repository = new ItemHistoryRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseScanHistory>> getLiveData() {
        return liveData;
    }
}