package com.acube.jims.Presentation.Login.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Login.ResponseCheckCustomer;
import com.acube.jims.datalayer.repositories.Authentication.CheckCustomerRepository;

public class CheckCustomerViewModel extends AndroidViewModel {
    private CheckCustomerRepository repository;
    private LiveData<ResponseCheckCustomer> liveData;

    public CheckCustomerViewModel(@NonNull Application application) {
        super(application);
     repository = new CheckCustomerRepository(application);
    }

    public void CheckUserExists(String vaphone) {

        repository.CheckCustomer(vaphone);
    }

    public LiveData<ResponseCheckCustomer> getCustomerLiveData() {
        return liveData;
    }
}
