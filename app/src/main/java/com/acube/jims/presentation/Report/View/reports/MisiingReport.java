package com.acube.jims.presentation.Report.View.reports;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.acube.jims.BaseActivity;
import com.acube.jims.datalayer.models.Audit.AuditReportItems;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.Audit.ViewModel.AuditUploadViewModel;
import com.acube.jims.presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.presentation.Report.adapter.Missingadapter;
import com.acube.jims.R;
import com.acube.jims.utils.AppUtility;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ActivityReportViewbycatBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MisiingReport extends BaseActivity implements Missingadapter.PassId {
    ActivityReportViewbycatBinding binding;
    private ReportViewModel mViewModel;
    Missingadapter reportadapter;
    String  companyID, warehouseID, AuditID, Employeename;
    AuditUploadViewModel auditUploadViewModel;
    List<String> compareitemlist;
    String commaseparatedlist;
    int locationid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_report_viewbycat);
        if (new AppUtility(MisiingReport.this).isTablet(MisiingReport.this)){
            binding.recyvfound.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        }else{
            binding.recyvfound.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        }
        AuditID=getIntent().getStringExtra("auditID");
        initToolBar(binding.toolbarApp.toolbar,"Missing",true);



        locationid = getIntent().getIntExtra("locationid",0);
        companyID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "CompanyID");
        warehouseID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "warehouseId");
        Employeename = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "EmployeeName");
        Log.d("AuditID", "onCreate: "+AuditID);
        showProgressDialog();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auditID", AuditID);
        jsonObject.addProperty("companyID", companyID);
        jsonObject.addProperty("warehouseID", warehouseID);
        jsonObject.addProperty("locationID", locationid);

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

        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getMissing(AuditID,0,0).observe(this, new Observer<List<AuditReportItems>>() {
            @Override
            public void onChanged(List<AuditReportItems> auditReportItems) {
                hideProgressDialog();
                if (auditReportItems != null) {
                    binding.tvTotaldata.setText("Total Items : " + auditReportItems.size());
                    reportadapter = new Missingadapter(getApplicationContext(), auditReportItems, MisiingReport.this);
                    binding.recyvfound.setAdapter(reportadapter);
                    if (reportadapter.getItemCount() == 0) {
                        binding.tvNotfound.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvNotfound.setVisibility(View.GONE);
                    }
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
        Log.d(TAG, "PerformScan: " + commaseparatedlist);


        try {
            Intent res = new Intent();
            String mPackage = "com.acube.smarttray";// package name
            String mClass = ".SmartTrayReading";//the activity name which return results*/
            //   String mPackage = "com.example.acubetest";// package name
            //    String mClass = ".Audit";//the activity name which return results
            res.putExtra("type", "MISSING");
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


                        new Thread(() -> {
                            Intent data = result.getData();
                            if (data != null) {
                                //    String filepath = data.getStringExtra("jsonSerialNo");
                                //  Log.d("ResponseFile", "onActivityResult: " + filepath);

                                String filepath = "/storage/emulated/0/Download/AcubeJimsLocate.json";
                                File file = new File(filepath);
                                if (file.exists()) {
                                    FileReader fileReader = null;
                                    try {
                                        fileReader = new FileReader(filepath);
                                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                                        StringBuilder stringBuilder = new StringBuilder();
                                        String line = bufferedReader.readLine();
                                        while (line != null) {
                                            stringBuilder.append(line).append("\n");
                                            line = bufferedReader.readLine();
                                        }
                                        bufferedReader.close();
                                        String response = stringBuilder.toString();
                                        //    AppDatabase.getInstance(getApplicationContext()).auditDownloadDao().inserdummy(tempSerials);
                                        JSONArray obj = new JSONArray(response);
                                        ArrayList<TemDataSerial> dataset = new Gson().fromJson(obj.toString(), new TypeToken<List<TemDataSerial>>() {
                                        }.getType());
                                        CheckSerialnumExist(dataset);
                                        Log.d("TAG", "onActivityResult: " + dataset.size());

                                    } catch (FileNotFoundException e) {
                                        Log.d("TAG", "filenotexist: " + e.getMessage());

                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        Log.d("TAG", "filenotexist: " + e.getMessage());

                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        Log.d("TAG", "filenotexist: " + e.getMessage());


                                        e.printStackTrace();
                                    }

                                } else {


                                }
                                Log.d("aspdata", "onActivityResult: " + data.getStringExtra("jsonSerialNo"));


                            }


                            runOnUiThread(() -> {
                                hideProgressDialog();


                            });
                        }).start();
                        // Here, no request code


                    }
                }
            });
    private void CheckSerialnumExist(ArrayList<TemDataSerial> dataset) {


        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().insertTemporarySerialNos(dataset);
            runOnUiThread(() -> {
                checkexists();
            });
        }).start();

    }
    private void checkexists() {
        int systemID = 0;
        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().checkauditscan(AuditID, locationid);


            runOnUiThread(() -> {
                hideProgressDialog();
            });
        }).start();
    }


}