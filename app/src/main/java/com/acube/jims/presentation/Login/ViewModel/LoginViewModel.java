package com.acube.jims.presentation.Login.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Authentication.ResponseLogin;
import com.acube.jims.datalayer.repositories.Authentication.LoginRepository;
import com.google.gson.JsonObject;

public class LoginViewModel extends AndroidViewModel {
    public LoginRepository repository;
    private LiveData<ResponseLogin> liveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);

    }

    public void Login(JsonObject jsonObject, Context context) {
        repository.Login(jsonObject,context);
    }

    public void init() {
        repository = new LoginRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseLogin> getLiveData() {
        return liveData;
    }
}
