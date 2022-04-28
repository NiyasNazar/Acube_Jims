package com.acube.jims.presentation.Favorites.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Favorites.ResponseFavorites;
import com.acube.jims.datalayer.repositories.Favorites.ViewFavoritesRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    public ViewFavoritesRepository repository;
    private LiveData<List<ResponseFavorites>>  liveData;

    public FavoritesViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void ViewCart(String Auth, String CustomerID) {
        repository.ViewCart(Auth, CustomerID);
    }

    public void init() {
        repository = new ViewFavoritesRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseFavorites>>  getLiveData() {
        return liveData;
    }
}