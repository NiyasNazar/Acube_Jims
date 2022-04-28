package com.acube.jims.presentation.Analytics.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.Analytics.ResponseAnalyticsGraph;
import com.acube.jims.datalayer.models.Analytics.ResponseAnalyticsSummary;
import com.acube.jims.datalayer.models.Analytics.ResponseCustomerServed;
import com.acube.jims.datalayer.models.Analytics.ResponseItemWiseAnalytics;
import com.acube.jims.datalayer.repositories.Analytics.AnalyticsRepository;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

public class AnalyticsViewModel extends AndroidViewModel {
    public AnalyticsRepository repository;
    private LiveData<ResponseAnalyticsSummary> liveData;
    private LiveData<ResponseAnalyticsGraph> graphLiveData;
    private LiveData<ResponseCustomerServed> responseCustomerServedLiveData;
    private LiveData<ResponseItemWiseAnalytics> responseItemWiseAnalyticsMutableLiveData;

    public AnalyticsViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void AnalyticSummary(String Auth, JsonObject jsonObject) {
        repository.AnalyticsSummary(Auth, jsonObject);
    }

    public void AnalyticGraph(String Auth, JsonObject jsonObject) {
        repository.Analyticsgraph(Auth, jsonObject);
    }

    public void CustomerServed(String Auth, JsonObject jsonObject) {
        repository.CustomersServed(Auth, jsonObject);
    }

    public void ItemWiseAnalytics(String Auth, JsonObject jsonObject) {
        repository.ItemWiseAnalytics(Auth, jsonObject);
    }

    public void init() {
        repository = new AnalyticsRepository();
        liveData = repository.getResponseLiveData();
        graphLiveData = repository.graphLiveData();
        responseCustomerServedLiveData = repository.customerServedLiveData();
        responseItemWiseAnalyticsMutableLiveData = repository.getItemwiseData();
    }


    public LiveData<ResponseAnalyticsSummary> getLiveData() {
        return liveData;
    }

    public LiveData<ResponseAnalyticsGraph> getGraphLiveData() {
        return graphLiveData;
    }

    public LiveData<ResponseCustomerServed> customerServedLiveData() {
        return responseCustomerServedLiveData;
    }


    public LiveData<ResponseItemWiseAnalytics> getItemwiseLiveData() {
        return responseItemWiseAnalyticsMutableLiveData;
    }
}