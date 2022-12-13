package com.acube.jims.presentation.Audit;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.datalayer.models.Audit.AuditItem;
import com.acube.jims.datalayer.models.Audit.AuditLocation;
import com.acube.jims.datalayer.models.Audit.AuditSnapShot;
import com.acube.jims.datalayer.models.Audit.AuditSubCategory;
import com.acube.jims.datalayer.models.Audit.Store;
import com.acube.jims.datalayer.models.warehouse.ResponseWareHouse;
import com.acube.jims.datalayer.remote.db.AuditCate;
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
import com.acube.jims.utils.MyValueFormatter;
import com.acube.jims.utils.OnSingleClickListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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
    int storeID = 0;
    int subcatcategoryId = 0;

    List<AuditLocation> dataset;
    List<Store> datasetStore;
    List<AuditSubCategory> datasetsubcat;

    List<AuditCate> datasetcat;
    JSONObject jsonObjectserials;
    boolean handheld, DeviceReg;

    String TrayMacAddress;
    List<PieEntry> pieEntryList;
    PieData pieData;
    int total, missing = 0, extra = 0, mismatch = 0, found = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audit_scan);
        auditID = getIntent().getStringExtra("auditID");
        initToolBar(binding.toolbarApp.toolbar, "Audit Scan", true);
        dataset = new ArrayList<>();
        datasetStore = new ArrayList<>();
        TrayMacAddress = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "TrayMacAddress");
        handheld = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "handheld");
        DeviceReg = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "DeviceReg");
        datasetcat = new ArrayList<>();
        datasetsubcat = new ArrayList<>();
        binding.tvauditID.setText(auditID);


        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getDownloadedStore(auditID).observe(this, new Observer<List<Store>>() {
            @Override
            public void onChanged(List<Store> localstore) {
                for (int i = 0; i < localstore.size(); i++) {
                    binding.tvStore.setText(localstore.get(i).getWarehouseName());
                }


            }
        });
        ///
        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().gettotalAgainistaudit(auditID, systemLocationID, categoryId, 0, 1, subcatcategoryId, storeID).observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.tvweight.setText("Grs.Wt " + String.format("%.2f", aDouble) + " g");

            }
        });


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


        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getCategory(auditID).observe(this, new Observer<List<AuditCate>>() {
            @Override
            public void onChanged(List<AuditCate> localAudits) {
                Log.d("onChanged", "onChanged: " + localAudits.size());
                if (localAudits != null) {
                    datasetcat = localAudits;

                    if (datasetcat != null && datasetcat.size() > 1) {
                        AuditCate localCategory = new AuditCate();
                        localCategory.setCategoryName("All");
                        localCategory.setCategoryId(0);
                        datasetcat.add(localCategory);
                        Collections.sort(datasetcat, new Comparator() {
                            @Override
                            public int compare(Object o1, Object o2) {
                                AuditCate p1 = (AuditCate) o1;
                                AuditCate p2 = (AuditCate) o2;
                                return p1.getCategoryName().compareToIgnoreCase(p2.getCategoryName());
                            }
                        });
                    }

                    Log.d("onChanged", "onChanged: " + datasetcat.size());
                    ArrayAdapter<AuditCate> arrayAdapter = new ArrayAdapter<AuditCate>(AuditScanActivity.this, android.R.layout.simple_spinner_item, datasetcat);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spincategory.setAdapter(arrayAdapter);
                }
            }
        });


        binding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(), AuditReadingActivity.class)
                        .putExtra("systemLocationID", systemLocationID)
                        .putExtra("storeID", storeID)
                        .putExtra("categoryId", categoryId)
                        .putExtra("subcatID", 0)
                        .putExtra("url", AppConstants.BASE_URL)
                        .putExtra("macAddress", TrayMacAddress)
                        .putExtra("auditID", auditID));

                // PerformScan();


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
                systemLocationID = dataset.get(position).getLocationId();
                //updateCount();
                updateCount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getDownloadedLocation(auditID).observe(this, new Observer<List<AuditLocation>>() {
            @Override
            public void onChanged(List<AuditLocation> localAudits) {
                Log.d("onChanged", "onChanged: " + localAudits.size());
                if (localAudits != null) {
                    dataset = localAudits;
                    if (dataset.size() > 0) {

                        if (dataset != null && dataset.size() > 1) {
                            AuditLocation datasetall = new AuditLocation();
                            datasetall.setLocationName("All");
                            datasetall.setLocationId(0);
                            dataset.add(datasetall);
                            Collections.sort(dataset, new Comparator() {
                                @Override
                                public int compare(Object o1, Object o2) {
                                    AuditLocation p1 = (AuditLocation) o1;
                                    AuditLocation p2 = (AuditLocation) o2;
                                    return p1.getLocationName().compareToIgnoreCase(p2.getLocationName());
                                }
                            });
                        }

                    }


                    Log.d("onChanged", "onChanged: " + localAudits.size());
                    ArrayAdapter<AuditLocation> arrayAdapter = new ArrayAdapter<AuditLocation>(AuditScanActivity.this, android.R.layout.simple_spinner_item, localAudits);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinlocations.setAdapter(arrayAdapter);
                }
            }
        });

        binding.spincategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = datasetcat.get(position).getCategoryId();

                loadSubcat(categoryId);
                updateCount();
                // updateCount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinsubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subcatcategoryId = datasetsubcat.get(position).getSubCategoryId();
                updateCount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //updateCount();
    }

    private void loadSubcat(int categoryId) {

        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getDownloadedSubcatCategory(auditID,categoryId).observe(this, new Observer<List<AuditSubCategory>>() {
            @Override
            public void onChanged(List<AuditSubCategory> localstore) {
                Log.d("onChanged", "onChanged: " + localstore.size());
                if (localstore != null) {
                    datasetsubcat = localstore;


                    if (datasetsubcat != null && datasetsubcat.size() > 1) {
                        AuditSubCategory datasetall = new AuditSubCategory();
                        datasetall.setSubCategoryName("All");
                        datasetall.setSubCategoryId(0);
                        datasetsubcat.add(datasetall);
                        Collections.sort(datasetsubcat, new Comparator() {
                            @Override
                            public int compare(Object o1, Object o2) {
                                AuditSubCategory p1 = (AuditSubCategory) o1;
                                AuditSubCategory p2 = (AuditSubCategory) o2;
                                return p1.getSubCategoryName().compareToIgnoreCase(p2.getSubCategoryName());
                            }
                        });
                    }


                    ArrayAdapter<AuditSubCategory> arrayAdapter = new ArrayAdapter<AuditSubCategory>(AuditScanActivity.this, android.R.layout.simple_spinner_item, localstore);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinsubcategory.setAdapter(arrayAdapter);
                }
            }
        });
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

   /* private void updateCount() {
        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getallcountbycat(auditID, 0, 1, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.tvtotalstock.setText("Total Stock\n" + integer);
                    }
                });
        if (categoryId == 0) {


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


    }*/

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
                                        //CheckSerialnumExist(dataset);
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
            runOnUiThread(this::checkexists);
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
                String query = "INSERT INTO InventoryAudit (auditID,systemLocationID, scanLocationID,serialNumber,status,categoryID) Select '" + auditID + "','" + systemLocationID + "','" + systemLocationID + "'," + "SerialNo,'2','0' from TemDataSerial where SerialNo NOT IN(Select serialNumber from InventoryAudit where auditID ='" + auditID + "')";
                Boolean insert = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().unkwowninsert((new SimpleSQLiteQuery(query)));

            }

            runOnUiThread(() -> {
                hideProgressDialog();
                //  updateCount();
            });
        }).start();
    }

    private void updateCount() {

        new Thread(() -> {
            String query = "SELECT COUNT(*) FROM AuditSnapShot WHERE auditID ='A0410220002' AND status ='0' AND (subCategoryId='0' OR '0'='0') AND (locationId='0' OR '0'='0') AND  (itemId='0' OR '0'='0') AND  (warehouseId='0' OR '0'='0')";
            //Integer insert = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().check((new SimpleSQLiteQuery(query)));
            //   Log.d("TAG", "updateCount: " + insert);
            runOnUiThread(() -> {
                hideProgressDialog();
                //  updateCount();
            });
        }).start();


        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getallcountbycat(auditID, 0, 1, systemLocationID, categoryId, subcatcategoryId, storeID).observe(AuditScanActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.tvtotalstock.setText(String.valueOf(integer));
                        total = integer;
                        Log.d("TOtaldata", "audit:" + auditID);
                        Log.d("TOtaldata", "loc:" + systemLocationID);
                        Log.d("TOtaldata", "cat:" + categoryId);
                        Log.d("TOtaldata", "store:" + storeID);

                    }
                });


        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 1, systemLocationID, categoryId, subcatcategoryId, storeID).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.tvfound.setText(String.valueOf(integer));
                        found = integer;
                    }
                });
        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 0, systemLocationID, categoryId, subcatcategoryId, storeID).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.tvmissing.setText(String.valueOf(integer));
                        missing = integer;
                    }
                });
        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 2, systemLocationID, categoryId, subcatcategoryId, storeID).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.tvunknown.setText(String.valueOf(integer));
                        extra = integer;

                    }
                });
        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 3, systemLocationID, categoryId, subcatcategoryId, storeID).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.tvlocationmismatch.setText(String.valueOf(integer));
                        mismatch = integer;

                    }
                });

        setChart(binding.totalchart, total, 0, "TOTAL", "", ColorTemplate.JOYFUL_COLORS);
        //  setChart(binding.foundchart, total, found, "TOTAL", "FOUND", "#EB984E");
        //  setChart(binding.missingchart, total, missing, "TOTAL", "MISSING", "#17A589");
        //  setChart(binding.extrachart, total, extra, "TOTAL", "EXTRA","#1B4F72");
        //  setChart(binding.mismatch, total, mismatch, "TOTAL", "MISMATCH", "#3498DB");

    }

    private void setChart(PieChart pieChart, int total, int value, String totallabel, String label, int[] colorfulColors) {
        pieEntryList = new ArrayList<>();
        pieChart.setUsePercentValues(false);
        if (value > 0) {
            pieChart.setMinAngleForSlices(45f);

        }
        pieChart.setEntryLabelColor(Color.WHITE);


        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setTextColor(Color.BLACK);
        pieChart.getLegend().setTextSize(12f);
        pieChart.getLegend().setFormSize(15f);

        pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        pieChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        pieChart.setDrawSliceText(false);
       /* if (label.equalsIgnoreCase("")) {
            pieEntryList.add(new PieEntry(total, totallabel));
        } else {
            pieEntryList.add(new PieEntry(total, totallabel));
            pieEntryList.add(new PieEntry(value, label));
        }
*/
        int[] colors;
        ArrayList<Integer> color = new ArrayList<>();
        if (total != 0) {
            pieEntryList.add(new PieEntry(total, "TOTAL"));
            color.add(ContextCompat.getColor(this, R.color.Total));
        }

        if (missing != 0) {
            pieEntryList.add(new PieEntry(missing, "MISSING"));
            color.add(ContextCompat.getColor(this, R.color.Missing));

        }

        if (found != 0) {
            pieEntryList.add(new PieEntry(found, "FOUND"));
            color.add(ContextCompat.getColor(this, R.color.Found));
        }

        if (extra != 0) {
            pieEntryList.add(new PieEntry(extra, "EXTRA"));
            color.add(ContextCompat.getColor(this, R.color.Unknown));
        }
        if (mismatch != 0) {
            pieEntryList.add(new PieEntry(mismatch, "MISMATCH"));
            color.add(ContextCompat.getColor(this, R.color.Mismatch));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");
        pieDataSet.setColors(color);
        pieDataSet.setValueTextSize(15f);
        pieDataSet.setValueTextColor(Color.WHITE);

        // pieChart.setDrawEntryLabels(true);


        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });

        pieChart.animateX(2000);
        pieChart.invalidate();
    }

    @Override
    protected void onResume() {
        updateCount();
        super.onResume();
    }
}
