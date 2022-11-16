package com.acube.jims.presentation.CustomerManagment.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.repositories.Authentication.UpdateCustomerRepository;
import com.google.gson.JsonObject;

public class UpdateCustomerViewModel extends AndroidViewModel {
    private UpdateCustomerRepository repository;
    private LiveData<JsonObject> liveData;

    public UpdateCustomerViewModel(@NonNull Application application) {
        super(application);

    }

    public void UpdateCustomer(int ID, JsonObject jsonObject, Context context) {
        repository.UpdateCustomer(ID, jsonObject,context);
    }

    public void init() {
        repository = new UpdateCustomerRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<JsonObject> getCustomerLiveData() {
        return liveData;
    }
}
