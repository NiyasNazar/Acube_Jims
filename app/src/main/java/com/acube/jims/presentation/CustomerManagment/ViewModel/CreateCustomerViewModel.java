package com.acube.jims.presentation.CustomerManagment.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.acube.jims.datalayer.repositories.Authentication.CreateCustomerRepository;
import com.google.gson.JsonObject;

public class CreateCustomerViewModel extends AndroidViewModel {
    private CreateCustomerRepository repository;
    private LiveData<ResponseCreateCustomer> liveData;

    public CreateCustomerViewModel(@NonNull Application application) {
        super(application);

    }

    public void CreateCustomer(String Auth, JsonObject jsonObject, Context context) {
        repository.CreateCustomer(Auth,jsonObject,context);
    }

    public void init() {
        repository = new CreateCustomerRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseCreateCustomer> getCustomerLiveData() {
        return liveData;
    }
}
