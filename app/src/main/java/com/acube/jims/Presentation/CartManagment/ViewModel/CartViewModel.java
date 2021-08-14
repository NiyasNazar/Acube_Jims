package com.acube.jims.Presentation.CartManagment.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.acube.jims.datalayer.models.Cart.ResponseCart;
import com.acube.jims.datalayer.repositories.CartManagment.AddtoCartRepository;
import com.acube.jims.datalayer.repositories.CartManagment.ViewCartRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

public class CartViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    public ViewCartRepository repository;
    private LiveData<ResponseCart> liveData;

    public CartViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void ViewCart(String Auth, String CustomerID) {
        repository.ViewCart(Auth, CustomerID);
    }

    public void init() {
        repository = new ViewCartRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseCart> getLiveData() {
        return liveData;
    }
}