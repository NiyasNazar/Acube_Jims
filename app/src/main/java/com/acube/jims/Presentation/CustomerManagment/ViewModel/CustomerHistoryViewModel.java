package com.acube.jims.Presentation.CustomerManagment.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Authentication.ResponseCheckCustomer;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerHistory;
import com.acube.jims.datalayer.remote.dbmodel.CustomerHistory;
import com.acube.jims.datalayer.repositories.Authentication.CheckCustomerRepository;
import com.acube.jims.datalayer.repositories.Customer.CustomerHistoryRepository;
import com.google.gson.JsonObject;

public class CustomerHistoryViewModel extends AndroidViewModel {
    private CustomerHistoryRepository repository;
    private LiveData<ResponseCustomerHistory> liveData;

    public CustomerHistoryViewModel(@NonNull Application application) {
        super(application);

    }

    public void CustomerHistory(String token, String customerID) {
        repository.CustomerHistory(token, customerID);
    }

    public void init() {
        repository = new CustomerHistoryRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseCustomerHistory> getCustomerLiveData() {
        return liveData;
    }
}
