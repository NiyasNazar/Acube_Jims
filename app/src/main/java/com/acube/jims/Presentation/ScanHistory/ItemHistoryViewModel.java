package com.acube.jims.Presentation.ScanHistory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.Presentation.ScanItems.ResponseItems;
import com.acube.jims.datalayer.models.ScanHistory.ResponseScanHistory;
import com.acube.jims.datalayer.repositories.Common.ItemDetailsFromServerRepository;
import com.acube.jims.datalayer.repositories.ScanHistory.ItemHistoryRepository;
import com.google.gson.JsonObject;

import java.util.List;

public class ItemHistoryViewModel extends AndroidViewModel {
    public ItemHistoryRepository repository;
    private LiveData<List<ResponseScanHistory>> liveData;

    public ItemHistoryViewModel(@NonNull Application application) {
        super(application);
    }


    public void ItemHistory(String Token, JsonObject jsonObject) {
        repository.ItemHistory(Token, jsonObject);

    }

    public void init() {
        repository = new ItemHistoryRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseScanHistory>> getLiveData() {
        return liveData;
    }
}