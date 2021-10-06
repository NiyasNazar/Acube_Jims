package com.acube.jims.Presentation.Report.View.reports;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acube.jims.BaseActivity;
import com.acube.jims.Presentation.Audit.ViewModel.AuditUploadViewModel;
import com.acube.jims.Presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.Presentation.Report.adapter.Foundadapter;
import com.acube.jims.Presentation.Report.adapter.Missingadapter;
import com.acube.jims.Presentation.Report.adapter.Reportadapter;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.ActivityReportViewbycatBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Audit.AuditScanUpload;
import com.acube.jims.datalayer.models.Audit.Found;
import com.acube.jims.datalayer.models.Audit.Missing;
import com.acube.jims.datalayer.models.Audit.ResponseReport;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MisiingReport extends BaseActivity implements Missingadapter.PassId {
    ActivityReportViewbycatBinding binding;
    private ReportViewModel mViewModel;
    Missingadapter reportadapter;
    String locationid, companyID, warehouseID, AuditID, Employeename;
    AuditUploadViewModel auditUploadViewModel;
    List<String> compareitemlist;
    String commaseparatedlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_report_viewbycat);
        binding.recyvfound.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        mViewModel.init();
        auditUploadViewModel = new ViewModelProvider(this).get(AuditUploadViewModel.class);
        auditUploadViewModel.init();
        locationid = getIntent().getStringExtra("locationid");
        companyID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "CompanyID");
        warehouseID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "warehouseId");
        AuditID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "auditID");
        Employeename = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "EmployeeName");

        binding.backlayout.tvFragname.setText("Missing");
        showProgressDialog();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auditID", AuditID);
        jsonObject.addProperty("companyID", companyID);
        jsonObject.addProperty("warehouseID", warehouseID);
        jsonObject.addProperty("locationID", Integer.parseInt(locationid));

        binding.cdvlocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformScan();

            }
        });
        binding.checkBoxselectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    reportadapter.selectAll();
                    binding.checkBoxselectall.setText("Unselect All");


                } else {
                    reportadapter.unselectall();
                    binding.checkBoxselectall.setText("Select All");
                }

            }
        });


        mViewModel.FetchInvoice(LocalPreferences.getToken(getApplicationContext()), jsonObject);
        mViewModel.getLiveData().observe(this, new Observer<ResponseReport>() {
            @Override
            public void onChanged(ResponseReport responseReport) {
                hideProgressDialog();
                if (responseReport != null) {
                    List<Missing> datsetfound = responseReport.getMissing();
                    binding.tvTotaldata.setText("Total Items : " + datsetfound.size());
                    reportadapter = new Missingadapter(getApplicationContext(), datsetfound, MisiingReport.this);
                    binding.recyvfound.setAdapter(reportadapter);
                    if (reportadapter.getItemCount() == 0) {
                        binding.tvNotfound.setVisibility(View.VISIBLE);
                    } else {

                        binding.tvNotfound.setVisibility(View.GONE);
                    }
                }
            }
        });
        auditUploadViewModel.getLiveData().observe(this, new Observer<AuditScanUpload>() {
            @Override
            public void onChanged(AuditScanUpload auditScanUpload) {
                hideProgressDialog();
                if (auditScanUpload != null) {
                    LocalPreferences.storeIntegerPreference(getApplicationContext(), "totalstock", auditScanUpload.getTotalStock());
                    LocalPreferences.storeIntegerPreference(getApplicationContext(), "missing", auditScanUpload.getMissing());
                    LocalPreferences.storeIntegerPreference(getApplicationContext(), "found", auditScanUpload.getFound());
                    LocalPreferences.storeIntegerPreference(getApplicationContext(), "locationmismatch", auditScanUpload.getLocationMismatch());
                    finish();
                }
            }
        });
    }

    @Override
    public void passid(String id, Integer locid) {

    }

    @Override
    public void compareitems(List<String> comparelist) {
        compareitemlist = new ArrayList<>();
        if (comparelist.size() > 1) {
            //   binding.bottomlayt.setVisibility(View.VISIBLE);
        } else {
            // binding.bottomlayt.setVisibility(View.GONE);
        }
        compareitemlist = comparelist;
    }

    private void PerformScan() {
        String TrayMacAddress = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "TrayMacAddress");

        StringBuilder str = new StringBuilder("");
        for (String eachstring : compareitemlist) {
            str.append(eachstring).append(",");
        }
        commaseparatedlist = str.toString();
        if (commaseparatedlist.length() > 0)
            commaseparatedlist
                    = commaseparatedlist.substring(
                    0, commaseparatedlist.length() - 1);
        Log.d(TAG, "PerformScan: "+commaseparatedlist);


        try {
            Intent res = new Intent();
             String mPackage = "com.acube.smarttray";// package name
             String mClass = ".MissingAuditActivity";//the activity name which return results*/
         //   String mPackage = "com.example.acubetest";// package name
          //  String mClass = ".Audit";//the activity name which return results
            res.putExtra("url", AppConstants.BASE_URL);
            res.putExtra("macAddress", TrayMacAddress);
            res.putExtra("jsonSerialNo", commaseparatedlist);
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
                            Toast.makeText(getApplicationContext(), "" + json, Toast.LENGTH_LONG).show();

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
                                body.addProperty("auditID", AuditID);
                                body.addProperty("warehouseID", warehouseID);
                                body.addProperty("locationID", locationid);
                                body.addProperty("scannedBy", Employeename);
                                body.add("items", itemsarray);
                                Log.d("onActivityResult", "onActivityResult: " + body.toString());
                                auditUploadViewModel.Audit(LocalPreferences.getToken(getApplicationContext()), body);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        Log.d("onActivityResult", "onActivityResult: " + data.getStringExtra("jsonSerialNo"));
                        //doSomeOperations();
                    }
                }
            });

}