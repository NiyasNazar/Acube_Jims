package com.acube.jims.presentation.Quotation;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Invoice.SaleSuccess;
import com.acube.jims.datalayer.repositories.Invoice.MarkSaleRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

public class SaleViewModel extends AndroidViewModel {
    public MarkSaleRepository repository;
    private LiveData<SaleSuccess> liveData;

    public SaleViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void FetchInvoice(String Auth, JsonObject jsonObject, Context context) {
        repository.MarkSale(Auth,jsonObject,context);
    }

    public void init() {
        repository = new MarkSaleRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<SaleSuccess> getLiveData() {
        return liveData;
    }

}