package com.acube.jims.Presentation.Quotation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Invoice.ResponseInvoiceList;
import com.acube.jims.datalayer.repositories.Invoice.InvoiceListRepository;
import com.acube.jims.datalayer.repositories.Invoice.MarkSaleRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SaleViewModel extends AndroidViewModel {
    public MarkSaleRepository repository;
    private LiveData<JsonObject> liveData;

    public SaleViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void FetchInvoice(String Auth,JsonObject jsonObject) {
        repository.MarkSale(Auth,jsonObject);
    }

    public void init() {
        repository = new MarkSaleRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<JsonObject> getLiveData() {
        return liveData;
    }

}