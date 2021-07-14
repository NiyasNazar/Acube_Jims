package com.acube.jims.Presentation.Login.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.acube.jims.datalayer.models.Authentication.ResponseLogin;
import com.acube.jims.datalayer.repositories.Authentication.CreateCustomerRepository;
import com.acube.jims.datalayer.repositories.Authentication.LoginRepository;
import com.google.gson.JsonObject;

public class LoginViewModel extends AndroidViewModel {
    public LoginRepository repository;
    private LiveData<ResponseLogin> liveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);

    }

    public void Login(JsonObject jsonObject) {
        repository.Login(jsonObject);
    }

    public void init() {
        repository = new LoginRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseLogin> getLiveData() {
        return liveData;
    }
}
