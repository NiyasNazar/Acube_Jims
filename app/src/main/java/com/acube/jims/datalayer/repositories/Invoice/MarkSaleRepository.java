package com.acube.jims.datalayer.repositories.Invoice;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Invoice.ResponseInvoiceList;
import com.acube.jims.datalayer.models.Invoice.SaleSuccess;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarkSaleRepository {
    private Application application;
    private MutableLiveData<SaleSuccess> dataset;

    public MarkSaleRepository() {
        dataset = new MutableLiveData<>();
    }

    public void MarkSale(String Auth, JsonObject JsonObject) {
        RestApiService restApiService = RetrofitInstance.getApiService();


        Call<SaleSuccess> call = restApiService.markSaleOrQuotation(Auth, JsonObject);
        call.enqueue(new Callback<SaleSuccess>() {
            @Override
            public void onResponse(Call<SaleSuccess> call, Response<SaleSuccess> response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {

                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<SaleSuccess> call, Throwable t) {
                dataset.setValue(null);
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });

    }

    public LiveData<SaleSuccess> getResponseLiveData() {
        return dataset;
    }
}
