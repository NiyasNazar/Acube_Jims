package com.acube.jims.presentation.Quotation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Invoice.ResponseInvoiceList;
import com.acube.jims.datalayer.repositories.Invoice.InvoiceListRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InvoiceViewModel extends AndroidViewModel {
    public InvoiceListRepository repository;
    private LiveData<List<ResponseInvoiceList>> liveData;

    public InvoiceViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void FetchInvoice(String Auth, String [] array) {
        repository.InvoiceList(Auth,array);
    }

    public void init() {
        repository = new InvoiceListRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseInvoiceList>> getLiveData() {
        return liveData;
    }

}