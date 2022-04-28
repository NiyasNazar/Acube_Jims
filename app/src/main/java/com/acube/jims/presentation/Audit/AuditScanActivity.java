package com.acube.jims.presentation.Audit;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.ActivityAuditScanBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.datalayer.remote.db.LocalAudit;
import com.google.gson.Gson;
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

public class AuditScanActivity extends BaseActivity {
    ActivityAuditScanBinding binding;
    String auditID;
    int systemLocationID = 0;
    List<LocalAudit> dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audit_scan);
        auditID = getIntent().getStringExtra("auditID");

        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getDownloadedAudits().observe(this, new Observer<List<LocalAudit>>() {
            @Override
            public void onChanged(List<LocalAudit> localAudits) {
                Log.d("onChanged", "onChanged: " + localAudits.size());

                if (localAudits != null) {
                    dataset = localAudits;
                    Log.d("onChanged", "onChanged: " + localAudits.size());
                    ArrayAdapter<LocalAudit> arrayAdapter = new ArrayAdapter<LocalAudit>(AuditScanActivity.this, android.R.layout.simple_spinner_item, localAudits);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinlocations.setAdapter(arrayAdapter);
                }
            }
        });

        binding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        binding.spinlocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                systemLocationID = dataset.get(position).getSystemLocationID();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getallcount(auditID, 0, 1).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.tvtotalstock.setText("Total Stock\n" + integer);
            }
        });

        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 1).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.tvfound.setText("Found\n" + integer);
            }
        });
        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 2).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.tvunknown.setText("Unknown\n" + integer);
            }
        });
        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 3).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.tvlocationmismatch.setText("Mismatch\n" + integer);
            }
        });
        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 0).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.tvmissing.setText("Missing\n" + integer);
            }
        });
    }

    private void PerformScan() {
        String TrayMacAddress = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "TrayMacAddress");

        try {
            Intent res = new Intent();
            // String mPackage = "com.acube.smarttray";// package name
            //  String mClass = ".InventoryAuditActivity";//the activity name which return results*/
            String mPackage = "com.example.acubetest";// package name
            String mClass = ".Audit";//the activity name which return results
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
                                String filepath = data.getStringExtra("jsonSerialNo");
                                Log.d("ResponseFile", "onActivityResult: " + filepath);

                                //  String filepath = "/storage/emulated/0/Android/data/com.acube.jms/files/Missing.json";
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

                                    } catch (FileNotFoundException e) {

                                        e.printStackTrace();
                                    } catch (IOException e) {

                                        e.printStackTrace();
                                    } catch (JSONException e) {


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
        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().checkauditscan(auditID);
            String query = "INSERT INTO InventoryAudit (auditID, systemLocationID,SerialNo,status) Select '" + auditID + "','" + systemLocationID + "'," + "SerialNo,'2' from TemDataSerial where SerialNo NOT IN(Select SerialNo from InventoryAudit where auditID ='" + auditID + "' )";
            Boolean insert = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().unkwowninsert((new SimpleSQLiteQuery(query)));

            runOnUiThread(() -> {
                hideProgressDialog();

            });
        }).start();
    }
}