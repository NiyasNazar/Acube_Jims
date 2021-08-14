package com.acube.jims.Presentation.Catalogue.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.repositories.Catalogue.CatalogueRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CatalogViewModelNextPage extends AndroidViewModel {
    public CatalogueRepository repository;
    private LiveData<List<ResponseCatalogueListing>> liveData;

    public CatalogViewModelNextPage(@NonNull @NotNull Application application) {
        super(application);
    }

    public void FetchCatalog(String Auth,int PageNum, int PageSize, String CatID, String SubCatID,String ColorCode,String KaratCode) {
        repository.FetchCatalogueItems(Auth,PageNum, PageSize, CatID, SubCatID,ColorCode,KaratCode);
    }

    public void init() {
        repository = new CatalogueRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseCatalogueListing>> getLiveData() {
        return liveData;
    }
    public LiveData<List<ResponseCatalogueListing>> getLiveDatanewPage() {
        return liveData;
    }
}