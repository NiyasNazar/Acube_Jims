package com.acube.jims.presentation.Report.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Audit.ResponseReport;
import com.acube.jims.datalayer.repositories.Report.ReportRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

public class ReportViewModel extends AndroidViewModel {
    public ReportRepository repository;
    private LiveData<ResponseReport> liveData;

    public ReportViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void FetchReports(String Auth, JsonObject jsonObject, Context context) {
        repository.FetchReport(Auth,jsonObject,context);
    }

    public void init() {
        repository = new ReportRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<ResponseReport> getLiveData() {
        return liveData;
    }

}