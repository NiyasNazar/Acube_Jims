package com.acube.jims.Presentation.Catalogue.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceRegistration;
import com.acube.jims.datalayer.repositories.Catalogue.CatalogueRepository;
import com.acube.jims.datalayer.repositories.DeviceRegistration.AddDeviceDetailsRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CatalogViewModel extends AndroidViewModel {
    public CatalogueRepository repository;
    private LiveData<List<ResponseCatalogueListing>> liveData;

    public CatalogViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void FetchCatalog(String PageNum, String PageSize, String CatID, String SubCatID) {
        repository.FetchCatalogueItems(PageNum, PageSize, CatID, SubCatID);
    }

    public void init() {
        repository = new CatalogueRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseCatalogueListing>> getLiveData() {
        return liveData;
    }
}