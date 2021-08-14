package com.acube.jims.Presentation.Catalogue.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.Filter.ResponseFetchFilters;
import com.acube.jims.datalayer.repositories.Catalogue.CatalogueRepository;
import com.acube.jims.datalayer.repositories.Catalogue.FilterRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FilterViewModel extends AndroidViewModel {
    public FilterRepository repository;
    private LiveData<List<ResponseFetchFilters> >liveData;

    public FilterViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void FetchFilters( String Auth) {
        repository.FetchFilterItems(Auth);
    }

    public void init() {
        repository = new FilterRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseFetchFilters> >getLiveData() {
        return liveData;
    }
}