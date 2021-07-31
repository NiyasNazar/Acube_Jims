package com.acube.jims.Presentation.ProductDetails.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogDetails;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.repositories.Catalogue.CatalogueRepository;
import com.acube.jims.datalayer.repositories.Catalogue.ItemDetailsRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemDetailsViewModel extends AndroidViewModel {
    public ItemDetailsRepository repository;
    private LiveData<ResponseCatalogDetails> liveData;

    public ItemDetailsViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void FetchItemDetails(String ID) {
        repository.FetchItemDetails(ID);
    }

    public void init() {
        repository = new ItemDetailsRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseCatalogDetails> getLiveData() {
        return liveData;
    }

}