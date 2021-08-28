package com.acube.jims.Presentation.CustomerManagment.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Authentication.ResponseCheckCustomer;
import com.acube.jims.datalayer.repositories.Authentication.CheckCustomerRepository;
import com.acube.jims.datalayer.repositories.Customer.CustomerLogoutRepository;
import com.google.gson.JsonObject;

public class CustomerLogoutViewModel extends AndroidViewModel {
    private CustomerLogoutRepository repository;
    private LiveData<JsonObject> liveData;

    public CustomerLogoutViewModel(@NonNull Application application) {
        super(application);

    }

    public void CustomerLogout(String Auth,JsonObject jsonObject) {
        repository.CustomerLogout(Auth,jsonObject);
    }

    public void init() {
        repository = new CustomerLogoutRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<JsonObject> getCustomerLiveData() {
        return liveData;
    }
}
