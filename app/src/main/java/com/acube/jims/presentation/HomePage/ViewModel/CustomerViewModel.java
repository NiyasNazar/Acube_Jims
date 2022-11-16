package com.acube.jims.presentation.HomePage.ViewModel;

import android.app.Application;
import android.content.Context;

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

    public void getCustomerSearch(String Token, String keyword, Context context) {
        repository.getCustomer(Token, keyword,context);

    }

    public void init() {
        repository = new CustomerRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseCustomerListing>> getLiveData() {
        return liveData;
    }
}