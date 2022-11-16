package com.acube.jims.presentation.Quotation;

import android.app.Application;
import android.content.Context;

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

    public void FetchInvoice(String Auth, String [] array, Context context) {
        repository.InvoiceList(Auth,array,context);
    }

    public void init() {
        repository = new InvoiceListRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<ResponseInvoiceList>> getLiveData() {
        return liveData;
    }

}