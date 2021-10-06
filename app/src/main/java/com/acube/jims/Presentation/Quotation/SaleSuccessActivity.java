package com.acube.jims.Presentation.Quotation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.acube.jims.BaseActivity;
import com.acube.jims.Presentation.HomePage.View.HomePageActivity;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleSuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_success);

        String GuestCustomerID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "GuestCustomerID");
        RestApiService restApiService = RetrofitInstance.getApiService();
        restApiService.ClearCart(LocalPreferences.getToken(getApplicationContext()), GuestCustomerID).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.code() == 200) {


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    public void goHome(View view) {
        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
        finishAffinity();
    }
}