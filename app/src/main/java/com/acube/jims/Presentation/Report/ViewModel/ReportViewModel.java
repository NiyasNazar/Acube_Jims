package com.acube.jims.Presentation.Report.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.acube.jims.datalayer.models.Audit.ResponseReport;
import com.acube.jims.datalayer.models.Invoice.ResponseInvoiceList;
import com.acube.jims.datalayer.repositories.Invoice.InvoiceListRepository;
import com.acube.jims.datalayer.repositories.Report.ReportRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReportViewModel extends AndroidViewModel {
    public ReportRepository repository;
    private LiveData<ResponseReport> liveData;

    public ReportViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void FetchReports(String Auth, JsonObject jsonObject) {
        repository.FetchReport(Auth,jsonObject);
    }

    public void init() {
        repository = new ReportRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseReport> getLiveData() {
        return liveData;
    }

}