package com.acube.jims.presentation.Audit;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.acube.jims.BaseActivity;
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
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

            String companyID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "CompanyID");
            String warehouseID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "warehouseId");
            String Employeename = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "EmployeeName");
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("warehouseID", warehouseID);
            binding.editTlocation.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    binding.editaudit.show();

                }
            });



            binding.recyvaduditlocations.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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

            mViewModel.getLiveData().observe(this, new Observer<List<ResponseAudit>>() {
                @Override
                public void onChanged(List<ResponseAudit> responseAudits) {
                    hideProgressDialog();
                    if (responseAudits != null) {
                        List<AuditResults>dataset=responseAudits.get(0).getAuditResultsList();
                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {

                                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().insert(dataset);
                            }
                        });
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
                    binding.editTlocation.setText(auditid);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            binding.downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (auditid.equalsIgnoreCase("")) {

                    } else {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("auditID", auditid);
                        jsonObject.addProperty("companyID", "");
                        jsonObject.addProperty("warehouseID", "");
                        jsonObject.addProperty("locationID", 0);
                        jsonObject.addProperty("categoryID", 0);
                        jsonObject.addProperty("subCategoryID", 0);
                        jsonObject.addProperty("karatID", 0);
                        jsonObject.addProperty("tabType", "");
                        jsonObject.addProperty("serialNo", "");
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

                    hideProgressDialog();
                }
            });


        }
    }


    @Override
    public void passid(String id) {

    }

    @Override
    public void replaceFragment(String menuname) {

    }

    @Override
    public void passlist(List<Integer> dataset) {

    }

    @Override
    public void scanaction(String auditID, String toBeAuditedOn, String remark) {
        startActivity(new Intent(getApplicationContext(), AuditScanActivity.class).putExtra("auditID", auditID));

    }
}