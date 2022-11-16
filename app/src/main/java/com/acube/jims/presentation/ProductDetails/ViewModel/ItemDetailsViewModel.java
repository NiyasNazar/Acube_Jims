package com.acube.jims.presentation.ProductDetails.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogDetails;
import com.acube.jims.datalayer.repositories.Catalogue.ItemDetailsRepository;

import org.jetbrains.annotations.NotNull;

public class ItemDetailsViewModel extends AndroidViewModel {
    public ItemDetailsRepository repository;
    private LiveData<ResponseCatalogDetails> liveData;

    public ItemDetailsViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void FetchItemDetails(String Auth,String ID,Context context) {
        repository.FetchItemDetails(Auth,ID,context);

    }
    public void FetchoutofsctockItemDetails(String Auth,String ID, Context context) {
        repository.FetchoutofsctockItemDetails(Auth,ID,context);

    }
    public void init() {
        repository = new ItemDetailsRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseCatalogDetails> getLiveData() {
        return liveData;
    }

}