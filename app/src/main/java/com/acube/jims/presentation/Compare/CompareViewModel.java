package com.acube.jims.presentation.Compare;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Compare.ResponseCompare;
import com.acube.jims.datalayer.repositories.Compare.CompareRepository;
import com.google.gson.JsonObject;

import java.util.List;

public class CompareViewModel extends AndroidViewModel {
    public CompareRepository repository;
    private LiveData<List<ResponseCompare>> liveData;

    public CompareViewModel(@NonNull Application application) {
        super(application);
    }


    public void getcompareItems(String Token, JsonObject jsonObject) {
        repository.CompareItems(Token, jsonObject);

    }

    public void init() {
        repository = new CompareRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseCompare>> getLiveData() {
        return liveData;
    }
}