package com.acube.jims.presentation.CartManagment.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.repositories.CartManagment.AddtoCartRepository;
import com.google.gson.JsonArray;

import org.jetbrains.annotations.NotNull;

public class AddtoCartViewModel extends AndroidViewModel {
    public AddtoCartRepository repository;
    private LiveData<ResponseAddtoCart> liveData;

    public AddtoCartViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void AddtoCart(String Auth,String type, JsonArray jsonArray) {
        repository.AddtoCart(Auth,type,jsonArray);
    }

    public void init() {
        repository = new AddtoCartRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseAddtoCart> getLiveData() {
        return liveData;
    }

}