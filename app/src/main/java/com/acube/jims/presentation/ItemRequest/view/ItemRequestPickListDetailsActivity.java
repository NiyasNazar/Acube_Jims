package com.acube.jims.presentation.ItemRequest.view;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.utils.AppUtility;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ActivityItemRequestPickListDetailsBinding;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.ItemRequest.ResponseItemRequestDetails;
import com.acube.jims.presentation.ItemRequest.adapter.PicklistDetailsAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemRequestPickListDetailsActivity extends BaseActivity {
    ActivityItemRequestPickListDetailsBinding binding;
    String picklistno;
    int customerid;
    GridLayoutManager gridLayoutManager;
    int trayID;
    int deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_request_pick_list_details);
        initToolBar(binding.toolbarApp, "Item Request Details", true);
        trayID = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "TrayID");
        deviceID = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "DeviceID");
        Context context = new ContextThemeWrapper(ItemRequestPickListDetailsActivity.this, R.style.AppTheme2);
        customerid = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        picklistno = getIntent().getStringExtra("picklistno");
        binding.requestcompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("itemRequestNo", picklistno);
                jsonObject.addProperty("customerID", customerid);
                jsonObject.addProperty("trayID", trayID);
                jsonObject.addProperty("deviceID", deviceID);

                new MaterialAlertDialogBuilder(context)

                        .setTitle("Close Picklist")
                        .setMessage("Are you sure you want to mark this picklist completed?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                UpdatePicklist(jsonObject);


                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        })
                        .show();
            }
        });

        if (new AppUtility(ItemRequestPickListDetailsActivity.this).isTablet(getApplicationContext())) {
            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
            binding.recyvcatalog.setLayoutManager(gridLayoutManager);

        } else {
            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            binding.recyvcatalog.setLayoutManager(gridLayoutManager);

            //binding.recyvcatalog.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        if (customerid == 0) {
            getPicklistforSalesman(picklistno);
        } else {

        }


    }

    private void UpdatePicklist(JsonObject jsonObject) {
        showProgressDialog();
        RetrofitInstance.getApiService().markpicklistCompleted(LocalPreferences.getToken(getApplicationContext()), jsonObject)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        hideProgressDialog();
                        if (response.body() != null && response.code() == 200) {

                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        hideProgressDialog();
                    }
                });
    }

    private void getPicklistforSalesman(String picklistno) {
        RetrofitInstance.getApiService().GetItemRequestDetailsbyRequestNo(LocalPreferences.getToken(getApplicationContext()), picklistno, String.valueOf(customerid))
                .enqueue(new Callback<List<ResponseItemRequestDetails>>() {
                    @Override
                    public void onResponse(Call<List<ResponseItemRequestDetails>> call, Response<List<ResponseItemRequestDetails>> response) {
                        if (response.body() != null && response.code() == 200) {
                            List<ResponseItemRequestDetails> dataset = response.body();
                            binding.recyvcatalog.setAdapter(new PicklistDetailsAdapter(getApplicationContext(), dataset));


                        }
                    }

                    @Override
                    public void onFailure(Call<List<ResponseItemRequestDetails>> call, Throwable t) {

                    }
                });
    }
}