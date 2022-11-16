package com.acube.jims.presentation.Audit;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.acube.jims.BaseActivity;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Audit.AuditItem;
import com.acube.jims.datalayer.models.Audit.AuditLocation;
import com.acube.jims.datalayer.models.Audit.AuditSnapShot;
import com.acube.jims.datalayer.models.Audit.AuditSubCategory;
import com.acube.jims.datalayer.models.Audit.ResponseErpAuditDownload;
import com.acube.jims.datalayer.models.Audit.Store;
import com.acube.jims.datalayer.models.LocateProduct.LocateItem;
import com.acube.jims.presentation.Audit.ViewModel.AuditLocationDetailsModel;
import com.acube.jims.presentation.Audit.ViewModel.AuditLocationViewModel;
import com.acube.jims.presentation.Audit.ViewModel.AuditUploadViewModel;
import com.acube.jims.presentation.Audit.ViewModel.AuditViewModel;
import com.acube.jims.presentation.Audit.adapter.AuditAdapter;
import com.acube.jims.presentation.Audit.adapter.AuditLocationadapter;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.AuditFragmentBinding;
import com.acube.jims.datalayer.models.Audit.AuditResults;
import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.datalayer.remote.db.LocalAudit;
import com.acube.jims.utils.OnSingleClickListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rscja.team.qcom.deviceapi.J;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuditFragment extends BaseActivity implements AuditLocationadapter.PassId, AuditAdapter.FragmentTransition {

    private AuditViewModel mViewModel;
    int totalscanneditems;

    public static AuditFragment newInstance() {
        return new AuditFragment();
    }

    AuditFragmentBinding binding;
    AuditLocationViewModel auditLocationViewModel;

    AuditLocationDetailsModel auditLocationViewModedetails;
    String auditid = "";
    AuditUploadViewModel auditUploadViewModel;
    int found, missing, locationmismatch, totalstock;
    String locationid = "0";
    List<ResponseAudit> dataset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        {
            binding = DataBindingUtil.setContentView(this, R.layout.audit_fragment);
            initToolBar(binding.toolbarApp.toolbar, "Inventory Audit", true);

            mViewModel = new ViewModelProvider(this).get(AuditViewModel.class);
            auditLocationViewModel = new ViewModelProvider(this).get(AuditLocationViewModel.class);
            auditLocationViewModedetails = new ViewModelProvider(this).get(AuditLocationDetailsModel.class);
            auditUploadViewModel = new ViewModelProvider(this).get(AuditUploadViewModel.class);
            auditLocationViewModel.init();
            auditUploadViewModel.init();
            mViewModel.init();
            auditLocationViewModedetails.init();
            showProgressDialog();
            binding.editaudit.setTitle("Select Audit");

            String companyID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "CompanyID");
            String warehouseID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "warehouseId");
            String Employeename = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "EmployeeName");
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("auditID", "");



            binding.recyvaduditlocations.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            binding.closeAudit.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (auditid.equalsIgnoreCase("")) {
                        showerror("Please select an audit");

                    } else {
                        showalert();
                    }


                }
            });
            mViewModel.AuditHeader(LocalPreferences.getToken(getApplicationContext()), jsonObject);
            mViewModel.getLiveDataHeader().observe(this, new Observer<List<ResponseAudit>>() {
                @Override
                public void onChanged(List<ResponseAudit> responseAudits) {
                    hideProgressDialog();
                    if (responseAudits != null) {
                        dataset = new ArrayList<>();
                        dataset = responseAudits;
                        ArrayAdapter<ResponseAudit> arrayAdapter = new ArrayAdapter<ResponseAudit>(AuditFragment.this, android.R.layout.simple_spinner_item, responseAudits);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.editaudit.setAdapter(arrayAdapter);
                    }
                }
            });

            mViewModel.getLiveData().observe(this, new Observer<ResponseErpAuditDownload>() {
                @Override
                public void onChanged(ResponseErpAuditDownload responseAudits) {
                    hideProgressDialog();
                    if (responseAudits != null) {
                        List<AuditItem> dataset = responseAudits.getItems();
                        List<AuditSubCategory> datasetAuditSubCategory = responseAudits.getSubCategories();
                        List<Store> datasetStore = responseAudits.getStores();
                        List<AuditSnapShot> datasetAuditSnapShot = responseAudits.getAuditSnapShot();
                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().insertSnapShot(datasetAuditSnapShot);

                            }
                        });
                        hideProgressDialog();
                    }
                }
            });
            DatabaseClient.getInstance(this).getAppDatabase().auditDownloadDao().getDownloadedAudits().observe(this, new Observer<List<LocalAudit>>() {
                @Override
                public void onChanged(List<LocalAudit> localAudits) {
                    if (localAudits != null) {
                        binding.recyvaduditlocations.setAdapter(new AuditAdapter(AuditFragment.this, localAudits, AuditFragment.this));

                    }
                }
            });
            binding.editaudit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    auditid = dataset.get(position).getAuditID();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            binding.downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (auditid.equalsIgnoreCase("")) {
                        showerror("Please select an audit");

                    } else {
                        showProgressDialog();
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("auditID", auditid);

                        mViewModel.AuditDetails(LocalPreferences.getToken(getApplicationContext()), jsonObject);


                    }



                  /*  for (int i = 0; i < auditResultsList.size(); i++) {
                        inventoryAudit = new AuditResults();
                        inventoryAudit.setAuditID(auditResultsList.get(i).getAuditID());
                        inventoryAudit.setItemName(auditResultsList.get(i).getItemName());
                        inventoryAudit.setCategoryID(auditResultsList.get(i).getCategoryID());
                        inventoryAudit.setCategoryName(auditResultsList.get(i).getCategoryName());

                        inventoryAudit.setSerialNumber(auditResultsList.get(i).getSerialNumber());
                        inventoryAudit.setToBeAuditedOn(auditResultsList.get(i).getToBeAuditedOn());
                        inventoryAudit.setSystemLocationID(auditResultsList.get(i).getSystemLocationID());
                        inventoryAudit.setSystemLocationName(auditResultsList.get(i).getSystemLocationName());
                        inventoryAudit.setRemark(auditResultsList.get(i).getRemark());
                        inventoryAudit.setStatus(0);
                        filteredaudit.add(inventoryAudit);
                    }*/
             /*       Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {

                            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().insert(filteredaudit);
                        }
                    });
*/


                }
            });


        }
    }

    private void showalert() {
        new MaterialAlertDialogBuilder(AuditFragment.this)
                .setTitle("Close Audit")
                .setMessage("Are you sure you want to close this audit?")
                .setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        closeAudit(auditid);

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void closeAudit(String auditid) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auditID", auditid);
        RetrofitInstance.getApiService(getApplicationContext()).closeanaudit(LocalPreferences.getToken(AuditFragment.this), jsonObject)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().closeAudit(auditid);

        }).start();

        mViewModel.AuditHeader(LocalPreferences.getToken(getApplicationContext()), jsonObject);

    }


    @Override
    public void passid(String id) {

    }

    @Override
    public void replaceFragment(String menuname) {

    }

    @Override
    public void passlist(String auditid) {
        new MaterialAlertDialogBuilder(AuditFragment.this)
                .setTitle("Upload Audit")
                .setMessage("Are you sure you want to upload this audit?")
                .setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UploadAudit(auditid);

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }


    private void UploadAudit(String auditid) {
        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getallauditforupload(auditid).observe(this, new Observer<List<AuditSnapShot>>() {
            @Override
            public void onChanged(List<AuditSnapShot> locateItems) {
                JsonObject body = new JsonObject();
                JsonArray jsonArray = new JsonArray();
                if (locateItems != null && locateItems.size() != 0) {
                    for (int i = 0; i < locateItems.size(); i++) {
                        JsonObject jsonObject = new JsonObject();
                        body.addProperty("auditID", locateItems.get(i).getAuditID());
                        body.addProperty("scannedBy", locateItems.get(i).getScannedBy());
                        body.addProperty("totalTimeTaken", 0);
                        jsonObject.addProperty("serialNo", locateItems.get(i).getSerialNumber());
                        jsonObject.addProperty("remark", locateItems.get(i).getRemarks());
                        jsonObject.addProperty("locationId", locateItems.get(i).getLocationId());
                        jsonObject.addProperty("auditId", locateItems.get(i).getAuditID());
                        jsonArray.add(jsonObject);


                    }
                    body.add("items", jsonArray);
                    Log.d("UploadArray", "onChanged: ");
                    postAuditData(body, auditid);

                } else {

                }

            }
        });
    }

    private void postAuditData(JsonObject body, String auditid) {
        showProgressDialog();
        RetrofitInstance.getApiService(getApplicationContext()).UploadAuditData(LocalPreferences.getToken(getApplicationContext()), body).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    deleteAudit(auditid);

                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                hideProgressDialog();

            }
        });
    }

    private void deleteAudit(String auditid) {
        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().deleteaudit(auditid);

            runOnUiThread(() -> {


            });
        }).start();

    }

    @Override
    public void scanaction(String auditID, String toBeAuditedOn, String remark) {
        startActivity(new Intent(getApplicationContext(), AuditScanActivity.class).putExtra("auditID", auditID));

    }
}