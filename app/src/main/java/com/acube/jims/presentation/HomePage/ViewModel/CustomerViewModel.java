package com.acube.jims.presentation.HomePage.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerListing;
import com.acube.jims.datalayer.repositories.HomePage.CustomerRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomerViewModel extends AndroidViewModel {
    public CustomerRepository repository;
    private LiveData<List<ResponseCustomerListing>> liveData;

    public CustomerViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void getCustomerSearch(String Token, String keyword) {
        repository.getCustomer(Token, keyword);

    }

    public void init() {
        repository = new CustomerRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseCustomerListing>> getLiveData() {
        return liveData;
    }
}