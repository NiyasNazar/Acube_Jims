package com.acube.jims.datalayer.repositories.Analytics;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Analytics.ResponseAnalyticsGraph;
import com.acube.jims.datalayer.models.Analytics.ResponseAnalyticsSummary;
import com.acube.jims.datalayer.models.Analytics.ResponseCustomerServed;
import com.acube.jims.datalayer.models.Analytics.ResponseItemWiseAnalytics;
import com.acube.jims.datalayer.models.ScanHistory.ResponseScanHistory;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyticsRepository {
    private Application application;
    private MutableLiveData<ResponseAnalyticsSummary> dataset;
    private MutableLiveData<ResponseAnalyticsGraph> responseAnalyticsGraphMutableLiveData;
    private MutableLiveData<ResponseCustomerServed> responseCustomerServedMutableLiveData;
    private MutableLiveData<ResponseItemWiseAnalytics> responseItemWiseAnalyticsMutableLiveData;

    public AnalyticsRepository() {
        dataset = new MutableLiveData<>();
        responseAnalyticsGraphMutableLiveData = new MutableLiveData<>();
        responseCustomerServedMutableLiveData = new MutableLiveData<>();
        responseItemWiseAnalyticsMutableLiveData = new MutableLiveData<>();
    }

    public void ItemWiseAnalytics(String Auth, JsonObject jsonObject) {
        RestApiService restApiService = RetrofitInstance.getApiService();


        Call<ResponseItemWiseAnalytics> call = restApiService.getGetItemWiseAnalytics(Auth, jsonObject);
        call.enqueue(new Callback<ResponseItemWiseAnalytics>() {
            @Override
            public void onResponse(Call<ResponseItemWiseAnalytics> call, Response<ResponseItemWiseAnalytics> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    responseItemWiseAnalyticsMutableLiveData.setValue(response.body());
                } else {

                    responseItemWiseAnalyticsMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseItemWiseAnalytics> call, Throwable t) {
                responseItemWiseAnalyticsMutableLiveData.setValue(null);
            }
        });
    }


    public void AnalyticsSummary(String Auth, JsonObject jsonObject) {
        RestApiService restApiService = RetrofitInstance.getApiService();


        Call<ResponseAnalyticsSummary> call = restApiService.getAnalyticsSummary(Auth, jsonObject);
        call.enqueue(new Callback<ResponseAnalyticsSummary>() {
            @Override
            public void onResponse(Call<ResponseAnalyticsSummary> call, Response<ResponseAnalyticsSummary> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {

                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAnalyticsSummary> call, Throwable t) {
                dataset.setValue(null);
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });

    }
    public void CustomersServed(String Auth, JsonObject jsonObject) {
        RestApiService restApiService = RetrofitInstance.getApiService();


        Call<ResponseCustomerServed> call = restApiService.getCustomerServed(Auth, jsonObject);
        call.enqueue(new Callback<ResponseCustomerServed>() {
            @Override
            public void onResponse(Call<ResponseCustomerServed> call, Response<ResponseCustomerServed> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    responseCustomerServedMutableLiveData.setValue(response.body());
                } else {

                    responseCustomerServedMutableLiveData.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseCustomerServed> call, Throwable t) {
                responseCustomerServedMutableLiveData.setValue(null);
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });

    }
    public void Analyticsgraph(String Auth, JsonObject jsonObject) {
        RestApiService restApiService = RetrofitInstance.getApiService();


        Call<ResponseAnalyticsGraph> call = restApiService.getAnalyticsGraph(Auth, jsonObject);
        call.enqueue(new Callback<ResponseAnalyticsGraph>() {
            @Override
            public void onResponse(Call<ResponseAnalyticsGraph> call, Response<ResponseAnalyticsGraph> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    responseAnalyticsGraphMutableLiveData.setValue(response.body());
                } else {

                    responseAnalyticsGraphMutableLiveData.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAnalyticsGraph> call, Throwable t) {
                responseAnalyticsGraphMutableLiveData.setValue(null);
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });

    }

    public LiveData<ResponseAnalyticsSummary> getResponseLiveData() {
        return dataset;
    }

    public LiveData<ResponseAnalyticsGraph> graphLiveData() {
        return responseAnalyticsGraphMutableLiveData;
    }

    public LiveData<ResponseCustomerServed> customerServedLiveData() {
        return responseCustomerServedMutableLiveData;
    }

    public LiveData<ResponseItemWiseAnalytics> getItemwiseData() {
        return responseItemWiseAnalyticsMutableLiveData;
    }
}
