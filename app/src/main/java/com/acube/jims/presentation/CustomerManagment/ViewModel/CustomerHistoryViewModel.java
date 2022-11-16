package com.acube.jims.presentation.CustomerManagment.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerHistory;
import com.acube.jims.datalayer.repositories.Customer.CustomerHistoryRepository;

public class CustomerHistoryViewModel extends AndroidViewModel {
    private CustomerHistoryRepository repository;
    private LiveData<ResponseCustomerHistory> liveData;

    public CustomerHistoryViewModel(@NonNull Application application) {
        super(application);

    }

    public void CustomerHistory(String token, int customerID, Context context) {
        repository.CustomerHistory(token, customerID,context);
    }

    public void init() {
        repository = new CustomerHistoryRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseCustomerHistory> getCustomerLiveData() {
        return liveData;
    }
}
