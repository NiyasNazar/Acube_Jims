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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ActivityAuditScanBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Audit.AuditResults;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.datalayer.remote.db.LocalAuditLocation;
import com.acube.jims.datalayer.remote.db.LocalCategory;
import com.acube.jims.presentation.Report.View.reports.FoundReportActivity;
import com.acube.jims.presentation.Report.View.reports.MisiingReport;
import com.acube.jims.presentation.Report.View.reports.TotalStockReport;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AuditScanActivity extends BaseActivity {
    ActivityAuditScanBinding binding;
    String auditID;
    int systemLocationID = 0;
    int categoryId = 0;
    List<LocalAuditLocation> dataset;
    List<LocalCategory> datasetcat;
    JSONObject jsonObjectserials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audit_scan);
        auditID = getIntent().getStringExtra("auditID");
        initToolBar(binding.toolbarApp.toolbar, "Audit Scan", true);
        dataset = new ArrayList<>();
        datasetcat = new ArrayList<>();

        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getDownloadedLocation().observe(this, new Observer<List<LocalAuditLocation>>() {
            @Override
            public void onChanged(List<LocalAuditLocation> localAudits) {
                Log.d("onChanged", "onChanged: " + localAudits.size());
                if (localAudits != null) {
                    dataset = localAudits;
                    Log.d("onChanged", "onChanged: " + localAudits.size());
                    ArrayAdapter<LocalAuditLocation> arrayAdapter = new ArrayAdapter<LocalAuditLocation>(AuditScanActivity.this, android.R.layout.simple_spinner_item, localAudits);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinlocations.setAdapter(arrayAdapter);
                }
            }
        });

        ///

        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getDownloadedAuditss(auditID).observe(this, new Observer<List<AuditResults>>() {
            @Override
            public void onChanged(List<AuditResults> auditResults) {
                JSONArray jsonArray = new JSONArray();

                for (int i = 0; i < auditResults.size(); i++) {

                    try {
                        jsonObjectserials = new JSONObject();
                        String serialNumber = auditResults.get(i).getSerialNumber();
                        jsonObjectserials.put("SerialNo", serialNumber);
                        jsonArray.put(jsonObjectserials);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    String filepath = save(getApplicationContext(), jsonArray.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });


        //


        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getDownloadedCategory().observe(this, new Observer<List<LocalCategory>>() {
            @Override
            public void onChanged(List<LocalCategory> localAudits) {
                Log.d("onChanged", "onChanged: " + localAudits.size());
                if (localAudits != null) {
                    datasetcat = localAudits;
                    LocalCategory localCategory = new LocalCategory();
                    localCategory.setCategoryName("All");
                    localCategory.setCategoryID(0);
                    datasetcat.add(localCategory);
                    Collections.sort(datasetcat, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            LocalCategory p1 = (LocalCategory) o1;
                            LocalCategory p2 = (LocalCategory) o2;
                            return p1.getCategoryName().compareToIgnoreCase(p2.getCategoryName());
                        }
                    });


                    Log.d("onChanged", "onChanged: " + localAudits.size());
                    ArrayAdapter<LocalCategory> arrayAdapter = new ArrayAdapter<LocalCategory>(AuditScanActivity.this, android.R.layout.simple_spinner_item, localAudits);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spincategory.setAdapter(arrayAdapter);
                }
            }
        });


        binding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (systemLocationID == 0) {
                    showerror("Select a location to continue");

                } else {
                    PerformScan();
                }


            }

        });
        binding.tvtotalstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TotalStockReport.class).putExtra("auditID", auditID));

            }
        });
        binding.tvmissing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MisiingReport.class).putExtra("auditID", auditID));
            }
        });
        binding.tvunknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FoundReportActivity.class).putExtra("auditID", auditID).putExtra("flag", 2));
            }
        });
        binding.tvfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FoundReportActivity.class).putExtra("auditID", auditID).putExtra("flag", 1));

            }
        });

        binding.spinlocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                systemLocationID = dataset.get(position).getSystemLocationID();
                updateCount();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spincategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = datasetcat.get(position).getCategoryID();
                updateCount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        updateCount();
    }

    public String save(Context context, String jsonString) throws IOException {

      /*  String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/folder");
        myDir.mkdirs();
        String fname = "file";
        File file = new File (myDir, fname);*/
        String filepath = null;

        File rootFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File jsonFile = new File(rootFolder, "AcubeJimsLocate.json");
        Log.d("Savedata", "save: " + jsonFile.getPath());
        FileWriter writer = new FileWriter(jsonFile);
        writer.write(jsonString);
        writer.close();
        //or IOUtils.closeQuietly(writer);
        return jsonFile.getAbsolutePath();
    }

    private void updateCount() {
        if (categoryId == 0) {
            DatabaseClient.getInstance(getApplicationContext()).
                    getAppDatabase().auditDownloadDao().getallcount(auditID, 0, 1, systemLocationID).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    binding.tvtotalstock.setText("Total Stock\n" + integer);
                }
            });

            DatabaseClient.getInstance(getApplicationContext()).
                    getAppDatabase().auditDownloadDao().getcount(auditID, 1, systemLocationID).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    binding.tvfound.setText("Found\n" + integer);
                }
            });
            DatabaseClient.getInstance(getApplicationContext()).
                    getAppDatabase().auditDownloadDao().getcount(auditID, 2, systemLocationID).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    binding.tvunknown.setText("Unknown\n" + integer);
                }
            });
            DatabaseClient.getInstance(getApplicationContext()).
                    getAppDatabase().auditDownloadDao().getcount(auditID, 3, systemLocationID).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    binding.tvlocationmismatch.setText("Mismatch\n" + integer);
                }
            });
            DatabaseClient.getInstance(getApplicationContext()).
                    getAppDatabase().auditDownloadDao().getcount(auditID, 0, systemLocationID).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    binding.tvmissing.setText("Missing\n" + integer);
                }
            });
        } else {
            DatabaseClient.getInstance(getApplicationContext()).
                    getAppDatabase().auditDownloadDao().getallcountbycat(auditID, 0, 1, systemLocationID, categoryId).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    binding.tvtotalstock.setText("Total Stock\n" + integer);
                }
            });

            DatabaseClient.getInstance(getApplicationContext()).
                    getAppDatabase().auditDownloadDao().getcountbycat(auditID, 1, systemLocationID, categoryId).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    binding.tvfound.setText("Found\n" + integer);
                }
            });
            DatabaseClient.getInstance(getApplicationContext()).
                    getAppDatabase().auditDownloadDao().getcountbycat(auditID, 2, systemLocationID, categoryId).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    binding.tvunknown.setText("Unknown\n" + integer);
                }
            });
            DatabaseClient.getInstance(getApplicationContext()).
                    getAppDatabase().auditDownloadDao().getcountbycat(auditID, 3, systemLocationID, categoryId).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    binding.tvlocationmismatch.setText("Mismatch\n" + integer);
                }
            });
            DatabaseClient.getInstance(getApplicationContext()).
                    getAppDatabase().auditDownloadDao().getcountbycat(auditID, 0, systemLocationID, categoryId).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    binding.tvmissing.setText("Missing\n" + integer);
                }
            });
        }


    }

    private void PerformScan() {
        String TrayMacAddress = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "TrayMacAddress");

        try {
            Intent res = new Intent();
            String mPackage = "com.acube.smarttray";// package name
            String mClass = ".SmartTrayReading";//the activity name which return results*/
            //   String mPackage = "com.example.acubetest";// package name
            //    String mClass = ".Audit";//the activity name which return results
            res.putExtra("type", "AUDIT");
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
                                //  Log.d("ResponseFile", "onActivityResult: " + filepath);

                             //   String filepath = "/storage/emulated/0/Download/AcubeJimsLocate.json";
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

                                    Log.d("TAG", "filenotexist: " + dataset.size());

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
            if (categoryId != 0) {
                Log.d("TAG", "checkexistswithoucat: ");
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().checkauditscanwithcat(auditID, systemLocationID, categoryId);

            } else {
                Log.d("TAG", "checkexistscat: ");

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().checkauditscan(auditID, systemLocationID);
                String query = "INSERT INTO InventoryAudit (auditID,systemLocationID, scanLocationID,serialNumber,status,categoryID) Select '" + auditID + "','"+systemLocationID+"','" + systemLocationID + "'," + "SerialNo,'2','0' from TemDataSerial where SerialNo NOT IN(Select serialNumber from InventoryAudit where auditID ='" + auditID + "')";
                Boolean insert = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().unkwowninsert((new SimpleSQLiteQuery(query)));

            }

            runOnUiThread(() -> {
                hideProgressDialog();
                updateCount();
            });
        }).start();
    }
}