package com.acube.jims.presentation.Favorites.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.repositories.Favorites.AddtoFavoritesRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

public class AddtoFavoritesViewModel extends AndroidViewModel {
    public AddtoFavoritesRepository repository;
    private LiveData<JsonObject> liveData;

    public AddtoFavoritesViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void AddtoFavorites(String Auth, String CustomerID, String EmployeeId, String ItemId, String type,String viewedOn,String serialno) {
        repository.AddtoFavorites(Auth, CustomerID, EmployeeId, ItemId, viewedOn, type,serialno);
    }

    public void init() {
        repository = new AddtoFavoritesRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<JsonObject> getLiveData() {
        return liveData;
    }

}