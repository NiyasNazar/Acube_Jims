package com.acube.jims.Presentation.CartManagment.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogDetails;
import com.acube.jims.datalayer.repositories.CartManagment.AddtoCartRepository;
import com.acube.jims.datalayer.repositories.Catalogue.ItemDetailsRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

public class AddtoCartViewModel extends AndroidViewModel {
    public AddtoCartRepository repository;
    private LiveData<ResponseAddtoCart> liveData;

    public AddtoCartViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void AddtoCart(String cartID,String Auth, String CustomerID, String EmployeeId, String ItemId, String type,String qty) {
        repository.AddtoCart(cartID,Auth, CustomerID, EmployeeId, ItemId, type,qty);
    }

    public void init() {
        repository = new AddtoCartRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseAddtoCart> getLiveData() {
        return liveData;
    }

}