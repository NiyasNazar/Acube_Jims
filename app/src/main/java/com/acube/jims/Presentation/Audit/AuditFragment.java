package com.acube.jims.Presentation.Audit;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.Audit.ViewModel.AuditLocationDetailsModel;
import com.acube.jims.Presentation.Audit.ViewModel.AuditLocationViewModel;
import com.acube.jims.Presentation.Audit.ViewModel.AuditUploadViewModel;
import com.acube.jims.Presentation.Audit.ViewModel.AuditViewModel;
import com.acube.jims.Presentation.Audit.adapter.AuditLocationadapter;
import com.acube.jims.Presentation.Report.View.reports.FoundReportActivity;
import com.acube.jims.Presentation.Report.View.reports.LocationMistmatchReport;
import com.acube.jims.Presentation.Report.View.reports.MisiingReport;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.AuditFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Audit.AuditScanUpload;
import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.models.Audit.ResponseLocationList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuditFragment extends BaseFragment implements AuditLocationadapter.PassId {

    private AuditViewModel mViewModel;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.audit_fragment, container, false);

        return binding.getRoot();
    }

    String warehouseID, Employeename, companyID;
    List<ResponseAudit> datasetaudits;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AuditViewModel.class);
        auditLocationViewModel = new ViewModelProvider(this).get(AuditLocationViewModel.class);
        auditLocationViewModedetails = new ViewModelProvider(this).get(AuditLocationDetailsModel.class);
        auditUploadViewModel = new ViewModelProvider(this).get(AuditUploadViewModel.class);
        auditLocationViewModel.init();
        auditUploadViewModel.init();
        mViewModel.init();
        auditLocationViewModedetails.init();
        datasetaudits = new ArrayList<>();

        companyID = LocalPreferences.retrieveStringPreferences(getActivity(), "CompanyID");
        warehouseID = LocalPreferences.retrieveStringPreferences(getActivity(), "warehouseId");
        Employeename = LocalPreferences.retrieveStringPreferences(getActivity(), "EmployeeName");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auditID", "");
        jsonObject.addProperty("companyID", companyID);
        jsonObject.addProperty("warehouseID", warehouseID);
        binding.recyvaduditlocations.setLayoutManager(new LinearLayoutManager(getActivity()));
        mViewModel.Audit(LocalPreferences.getToken(getActivity()), jsonObject);
        mViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseAudit>>() {
            @Override
            public void onChanged(List<ResponseAudit> responseAudits) {
                if (responseAudits != null) {
                    datasetaudits = responseAudits;
                    ArrayAdapter<ResponseAudit> arrayAdapter = new ArrayAdapter<ResponseAudit>(getActivity(), android.R.layout.simple_spinner_item, responseAudits);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.edAuditid.setAdapter(arrayAdapter);
                }
            }
        });
        binding.edAuditid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                auditid = parent.getItemAtPosition(position).toString();
                showProgressDialog();
                totalstock = datasetaudits.get(position).getTotalStock();
                missing = datasetaudits.get(position).getMissing();
                found = datasetaudits.get(position).getFound();
                LocalPreferences.storeIntegerPreference(getActivity(), "totalstock", totalstock);
                LocalPreferences.storeIntegerPreference(getActivity(), "missing", missing);
                LocalPreferences.storeIntegerPreference(getActivity(), "found", found);
                locationmismatch = datasetaudits.get(position).getLocationMismatch();
                if (totalstock == missing) {
                    missing = 0;
                    binding.tvmissing.setText("" + missing);

                } else {
                    binding.tvmissing.setText("" + missing);

                }
                binding.tvtotalstock.setText("" + totalstock);
                binding.tvlocationmismatch.setText("" + locationmismatch);
                binding.tvfound.setText("" + found);


                Toast.makeText(getActivity(), "items" + auditid, Toast.LENGTH_SHORT).show();
                JsonObject jsonObject1 = new JsonObject();
                jsonObject1.addProperty("auditID", auditid);
                LocalPreferences.storeStringPreference(getActivity(), "auditID", auditid);
                jsonObject1.addProperty("companyID", companyID);
                jsonObject1.addProperty("warehouseID", warehouseID);
                auditLocationViewModel.Audit(LocalPreferences.getToken(getActivity()), jsonObject1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.cdvfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FoundReportActivity.class).putExtra("locationid", locationid));

            }
        });

        binding.cdvmissing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MisiingReport.class).putExtra("locationid", locationid));

            }
        });

        binding.cdvlocationmismatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LocationMistmatchReport.class).putExtra("locationid", locationid));

            }
        });

        binding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformScan();

            }
        });

        mViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseAudit>>() {
            @Override
            public void onChanged(List<ResponseAudit> responseAudits) {
                if (responseAudits != null) {


                }

            }
        });
        auditLocationViewModedetails.getLiveData().observe(getActivity(), new Observer<List<ResponseLocationList>>() {
            @Override
            public void onChanged(List<ResponseLocationList> responseLocationLists) {
                if (responseLocationLists != null) {
                    totalstock = responseLocationLists.get(0).getTotalStock();
                    missing = responseLocationLists.get(0).getMissing();
                    found = responseLocationLists.get(0).getFound();
                    locationmismatch = responseLocationLists.get(0).getLocationMismatch();
                    LocalPreferences.storeIntegerPreference(getActivity(), "totalstock", totalstock);
                    LocalPreferences.storeIntegerPreference(getActivity(), "missing", missing);
                    LocalPreferences.storeIntegerPreference(getActivity(), "found", found);
                    LocalPreferences.storeIntegerPreference(getActivity(), "locationmismatch", locationmismatch);
                    if (totalstock == missing) {
                        missing = 0;
                        binding.tvmissing.setText("" + missing);

                    } else {
                        binding.tvmissing.setText("" + missing);

                    }
                    binding.tvtotalstock.setText("" + totalstock);
                    binding.tvlocationmismatch.setText("" + locationmismatch);
                    binding.tvfound.setText("" + found);
                }
            }
        });
        auditUploadViewModel.getLiveData().observe(getActivity(), new Observer<AuditScanUpload>() {
            @Override
            public void onChanged(AuditScanUpload auditScanUpload) {
                hideProgressDialog();
                if (auditScanUpload != null) {
                    totalstock = auditScanUpload.getTotalStock();
                    missing = auditScanUpload.getMissing();
                    found = auditScanUpload.getFound();
                    locationmismatch = auditScanUpload.getLocationMismatch();
                    LocalPreferences.storeIntegerPreference(getActivity(), "totalstock", totalstock);
                    LocalPreferences.storeIntegerPreference(getActivity(), "missing", missing);
                    LocalPreferences.storeIntegerPreference(getActivity(), "found", found);
                    LocalPreferences.storeIntegerPreference(getActivity(), "locationmismatch", locationmismatch);
                    if (totalstock == missing) {
                        missing = 0;
                        binding.tvmissing.setText("" + missing);

                    } else {
                        binding.tvmissing.setText("" + missing);

                    }
                    binding.tvtotalstock.setText("" + totalstock);
                    binding.tvlocationmismatch.setText("" + locationmismatch);
                    binding.tvfound.setText("" + found);

                }

            }
        });
        auditLocationViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseLocationList>>() {
            @Override
            public void onChanged(List<ResponseLocationList> responseLocationLists) {
                hideProgressDialog();
                if (responseLocationLists != null) {
                    String date = ParseDate(responseLocationLists.get(0).getToBeAuditedOn());
                    binding.tvdate.setText("Date : " + date);
                    binding.recyvaduditlocations.setAdapter(new AuditLocationadapter(getActivity(), responseLocationLists, AuditFragment.this::passid));
                }

            }
        });
    }

    public String ParseDate(String lastvisitdate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(lastvisitdate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        return formattedDate;
    }

    private void PerformScan() {
        String TrayMacAddress = LocalPreferences.retrieveStringPreferences(getActivity(), "TrayMacAddress");

        try {
            Intent res = new Intent();
            String mPackage = "com.acube.smarttray";// package name
            String mClass = ".InventoryAuditActivity";//the activity name which return results*/
            // String mPackage = "com.example.acubetest";// package name
            //   String mClass = ".Audit";//the activity name which return results
            res.putExtra("url", AppConstants.BASE_URL);
            res.putExtra("macAddress", TrayMacAddress);
            res.putExtra("jsonSerialNo", "json");
            res.setComponent(new ComponentName(mPackage, mPackage + mClass));
            someActivityResultLauncher.launch(res);
        } catch (Exception e) {
            Log.d("TAG", "replaceFragment: ");
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        showProgressDialog();
                        // Here, no request code
                        Intent data = result.getData();
                        if (data != null) {
                            String json = data.getStringExtra("jsonSerialNo");
                            Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();

                            JsonObject itemsobject = null;
                            JsonArray itemsarray = new JsonArray();
                            JsonObject body = new JsonObject();

                            try {
                                JSONArray jsonArray = new JSONArray(json);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject explrObject = jsonArray.getJSONObject(i);


                                    String serialno = explrObject.getString("SerialNo");
                                    itemsobject = new JsonObject();
                                    itemsobject.addProperty("serialNo", serialno);
                                    Log.d("onActivityResult", "onActivityResult: " + serialno);
                                    itemsarray.add(itemsobject);
                                }
                                String locationID = LocalPreferences.retrieveStringPreferences(getActivity(), "locationID");
                                body.addProperty("auditID", auditid);
                                body.addProperty("warehouseID", warehouseID);
                                body.addProperty("locationID", locationID);
                                body.addProperty("scannedBy", Employeename);
                                body.add("items", itemsarray);
                                Log.d("onActivityResult", "onActivityResult: " + body.toString());
                                auditUploadViewModel.Audit(LocalPreferences.getToken(getActivity()), body);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        Log.d("onActivityResult", "onActivityResult: " + data.getStringExtra("jsonSerialNo"));
                        //doSomeOperations();
                    }
                }
            });

    @Override
    public void onResume() {
        super.onResume();
        totalstock = LocalPreferences.retrieveIntegerPreferences(getActivity(), "totalstock");
        missing = LocalPreferences.retrieveIntegerPreferences(getActivity(), "missing");
        found = LocalPreferences.retrieveIntegerPreferences(getActivity(), "found");
        locationmismatch = LocalPreferences.retrieveIntegerPreferences(getActivity(), "locationmismatch");

        if (totalstock == missing) {
            missing = 0;
            binding.tvmissing.setText("" + missing);

        } else {
            binding.tvmissing.setText("" + missing);

        }
        binding.tvtotalstock.setText("" + totalstock);
        binding.tvlocationmismatch.setText("" + locationmismatch);
        binding.tvfound.setText("" + found);
    }

    @Override
    public void passid(String id) {
        locationid = id;

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("auditID", auditid);
        jsonObject1.addProperty("locationID", id);

        jsonObject1.addProperty("companyID", companyID);
        jsonObject1.addProperty("warehouseID", warehouseID);
        auditLocationViewModedetails.Audit(LocalPreferences.getToken(getActivity()), jsonObject1);
    }
}