package com.acube.jims.presentation.CustomerManagment.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.repositories.Customer.CustomerLogoutRepository;
import com.google.gson.JsonObject;

public class CustomerLogoutViewModel extends AndroidViewModel {
    private CustomerLogoutRepository repository;
    private LiveData<JsonObject> liveData;

    public CustomerLogoutViewModel(@NonNull Application application) {
        super(application);

    }

    public void CustomerLogout(String Auth, JsonObject jsonObject, Context context) {
        repository.CustomerLogout(Auth,jsonObject,context);
    }

    public void init() {
        repository = new CustomerLogoutRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<JsonObject> getCustomerLiveData() {
        return liveData;
    }
}
