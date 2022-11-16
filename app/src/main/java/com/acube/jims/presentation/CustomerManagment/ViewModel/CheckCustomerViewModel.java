package com.acube.jims.presentation.CustomerManagment.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Authentication.ResponseCheckCustomer;
import com.acube.jims.datalayer.repositories.Authentication.CheckCustomerRepository;

public class CheckCustomerViewModel extends AndroidViewModel {
    private CheckCustomerRepository repository;
    private LiveData<ResponseCheckCustomer> liveData;

    public CheckCustomerViewModel(@NonNull Application application) {
        super(application);

    }

    public void CheckUserExists(String vaphone, Context context) {
        repository.CheckCustomer(vaphone,context);
    }

    public void init() {
        repository = new CheckCustomerRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseCheckCustomer> getCustomerLiveData() {
        return liveData;
    }
}
