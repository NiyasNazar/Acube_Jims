package com.acube.jims.datalayer.repositories.Invoice;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Invoice.ResponseInvoiceList;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceListRepository {
    private Application application;
    private MutableLiveData<List<ResponseInvoiceList> > dataset;

    public InvoiceListRepository() {
        dataset = new MutableLiveData<>();
    }

    public void InvoiceList(String Auth, String [] array) {
        RestApiService restApiService = RetrofitInstance.getApiService();



        Call<List<ResponseInvoiceList>> call = restApiService.InvoiceItems(Auth, array);
        call.enqueue(new Callback<List<ResponseInvoiceList> >() {
            @Override
            public void onResponse(Call<List<ResponseInvoiceList> >call, Response<List<ResponseInvoiceList> >response) {
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    dataset.setValue(response.body());
                } else {

                    dataset.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseInvoiceList> > call, Throwable t) {
                dataset.setValue(null);
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });

    }

    public LiveData<List<ResponseInvoiceList> >getResponseLiveData() {
        return dataset;
    }
}
