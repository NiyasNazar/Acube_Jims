package com.acube.jims.presentation.Catalogue.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.repositories.Catalogue.CatalogueRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CatalogViewModel extends AndroidViewModel {
    public CatalogueRepository repository;
    private LiveData<List<ResponseCatalogueListing>> liveData;
    private LiveData<List<ResponseCatalogueListing>> liveDataSummary;

    public CatalogViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void FetchCatalog(String Auth, int PageNum, int PageSize, String CatID, String SubCatID, String ColorCode, String KaratCode, String MinWeight,String MaxWeight, String priceMin, String priceMax, String gender,int ID,int customerID ) {
        repository.FetchCatalogueItems(Auth, PageNum, PageSize, CatID, SubCatID, ColorCode, KaratCode,  MinWeight, MaxWeight, priceMin, priceMax, gender,ID,customerID);
    }

    public void FetchCatalogSummary(String Auth, int PageNum, int PageSize, String CatID, String SubCatID, String ColorCode, String KaratCode, String MinWeight,String MaxWeight, String priceMin, String priceMax, String gender,int customerID) {
        repository.FetchCatalogueSummary(Auth, PageNum, PageSize, CatID, SubCatID, ColorCode, KaratCode, MinWeight,MaxWeight, priceMin, priceMax, gender,customerID);
    }

    public void init() {
        repository = new CatalogueRepository();
        liveData = repository.getResponseLiveData();
        liveDataSummary = repository.getResponseLiveDataSummary();
    }


    public LiveData<List<ResponseCatalogueListing>> getLiveData() {
        return liveData;
    }

    public LiveData<List<ResponseCatalogueListing>> getLiveDataSummary() {
        return liveDataSummary;
    }

}