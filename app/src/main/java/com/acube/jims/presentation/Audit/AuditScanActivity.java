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
import com.acube.jims.datalayer.models.Audit.AuditSubCategory;
import com.acube.jims.datalayer.models.Audit.Store;
import com.acube.jims.datalayer.models.warehouse.ResponseWareHouse;
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
    int itemID = 0;
    List<AuditLocation> dataset;
    List<Store> datasetStore;
    List<AuditItem> datasetAuditItem;

    List<AuditSubCategory> datasetcat;
    JSONObject jsonObjectserials;
    boolean handheld;

    String TrayMacAddress;
    List<PieEntry> pieEntryList;
    PieData pieData;
    int total, missing, extra, mismatch,found;

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

        datasetcat = new ArrayList<>();
        datasetAuditItem = new ArrayList<>();


        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getDownloadedStore().observe(this, new Observer<List<Store>>() {
            @Override
            public void onChanged(List<Store> localstore) {
                Log.d("onChanged", "onChanged: " + localstore.size());
                if (localstore != null) {
                    datasetStore = localstore;
                    Store datasetall = new Store();
                    datasetall.setWarehouseName("All");
                    datasetall.setWarehouseId(0);
                    datasetStore.add(datasetall);
                    if (datasetStore != null && datasetStore.size() > 0) {
                        Collections.sort(datasetStore, new Comparator() {
                            @Override
                            public int compare(Object o1, Object o2) {
                                Store p1 = (Store) o1;
                                Store p2 = (Store) o2;
                                return p1.getWarehouseName().compareToIgnoreCase(p2.getWarehouseName());
                            }
                        });
                    }


                    ArrayAdapter<Store> arrayAdapter = new ArrayAdapter<Store>(AuditScanActivity.this, android.R.layout.simple_spinner_item, localstore);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinstore.setAdapter(arrayAdapter);
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


        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getDownloadedCategory().observe(this, new Observer<List<AuditSubCategory>>() {
            @Override
            public void onChanged(List<AuditSubCategory> localAudits) {
                Log.d("onChanged", "onChanged: " + localAudits.size());
                if (localAudits != null) {
                    AuditSubCategory localCategory = new AuditSubCategory();

                    datasetcat = localAudits;

                    localCategory.setSubCategoryCode("All");
                    localCategory.setSubCategoryId(0);
                    datasetcat.add(localCategory);
                    if (datasetcat != null && datasetcat.size() > 0) {
                        Collections.sort(datasetcat, new Comparator() {
                            @Override
                            public int compare(Object o1, Object o2) {
                                AuditSubCategory p1 = (AuditSubCategory) o1;
                                AuditSubCategory p2 = (AuditSubCategory) o2;
                                return p1.getSubCategoryCode().compareToIgnoreCase(p2.getSubCategoryCode());
                            }
                        });
                    }

                    Log.d("onChanged", "onChanged: " + datasetcat.size());
                    ArrayAdapter<AuditSubCategory> arrayAdapter = new ArrayAdapter<AuditSubCategory>(AuditScanActivity.this, android.R.layout.simple_spinner_item, datasetcat);
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
                        .putExtra("itemID", itemID)
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
        binding.spinstore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                storeID = datasetStore.get(position).getWarehouseId();
                LoadLocationorZone(storeID);
                updateCount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spincategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = datasetcat.get(position).getSubCategoryId();
                loadItem(categoryId);
                updateCount();
                // updateCount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinitem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemID = datasetAuditItem.get(position).getItemId();
                updateCount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //updateCount();
    }

    private void loadItem(int categoryId) {

        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getItems(categoryId).observe(this, new Observer<List<AuditItem>>() {
            @Override
            public void onChanged(List<AuditItem> localstore) {
                Log.d("onChanged", "onChanged: " + localstore.size());
                if (localstore != null) {
                    datasetAuditItem = localstore;
                    AuditItem datasetall = new AuditItem();
                    datasetall.setItemCode("All");
                    datasetall.setItemId(0);
                    datasetAuditItem.add(datasetall);

                    if (datasetAuditItem != null && datasetAuditItem.size() > 0) {
                        Collections.sort(datasetAuditItem, new Comparator() {
                            @Override
                            public int compare(Object o1, Object o2) {
                                AuditItem p1 = (AuditItem) o1;
                                AuditItem p2 = (AuditItem) o2;
                                return p1.getItemCode().compareToIgnoreCase(p2.getItemCode());
                            }
                        });
                    }


                    ArrayAdapter<AuditItem> arrayAdapter = new ArrayAdapter<AuditItem>(AuditScanActivity.this, android.R.layout.simple_spinner_item, localstore);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinitem.setAdapter(arrayAdapter);
                }
            }
        });
    }


    private void LoadLocationorZone(int ID) {
        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getDownloadedLocation(ID).observe(this, new Observer<List<AuditLocation>>() {
            @Override
            public void onChanged(List<AuditLocation> localAudits) {
                Log.d("onChanged", "onChanged: " + localAudits.size());
                if (localAudits != null) {
                    dataset = localAudits;
                    AuditLocation datasetall = new AuditLocation();
                    datasetall.setLocationName("All");
                    datasetall.setLocationId(0);
                    dataset.add(datasetall);
                    if (dataset != null && dataset.size() > 0) {
                        Collections.sort(dataset, new Comparator() {
                            @Override
                            public int compare(Object o1, Object o2) {
                                AuditLocation p1 = (AuditLocation) o1;
                                AuditLocation p2 = (AuditLocation) o2;
                                return p1.getLocationName().compareToIgnoreCase(p2.getLocationName());
                            }
                        });
                    }


                    Log.d("onChanged", "onChanged: " + localAudits.size());
                    ArrayAdapter<AuditLocation> arrayAdapter = new ArrayAdapter<AuditLocation>(AuditScanActivity.this, android.R.layout.simple_spinner_item, localAudits);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinlocations.setAdapter(arrayAdapter);
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
            Integer insert = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().check((new SimpleSQLiteQuery(query)));
            Log.d("TAG", "updateCount: "+insert);
            runOnUiThread(() -> {
                hideProgressDialog();
                //  updateCount();
            });
        }).start();



        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getallcountbycat(auditID, 0, 1, systemLocationID, categoryId, itemID, storeID).observe(AuditScanActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.tvtotalstock.setText(String.valueOf(integer));
                        total = integer;
                        Log.d("TOtaldata", "audit:" + auditID);
                        Log.d("TOtaldata", "loc:" + systemLocationID);
                        Log.d("TOtaldata", "cat:" + categoryId);
                        Log.d("TOtaldata", "item:" + itemID);
                        Log.d("TOtaldata", "store:" + storeID);

                    }
                });



        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 1, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.tvfound.setText(String.valueOf(integer));
                        found = integer;
                    }
                });
        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 0, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.tvmissing.setText(String.valueOf(integer));
                        missing = integer;
                    }
                });
        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 2, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.tvunknown.setText(String.valueOf(integer));
                        extra = integer;

                    }
                });
        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 3, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.tvlocationmismatch.setText(String.valueOf(integer));
                        mismatch = integer;

                    }
                });

        setChart(binding.statuspiechart, total, missing, extra, mismatch,found, ColorTemplate.COLORFUL_COLORS);

    }

    private void setChart(PieChart pieChart, int total, int missing, int extra, int mismatch,int found, int... colors) {
        pieEntryList = new ArrayList<>();
        pieChart.setUsePercentValues(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setTextColor(Color.BLACK);
        pieChart.setDrawSliceText(false);
        pieEntryList.add(new PieEntry(total, "TOTAL"));
        pieEntryList.add(new PieEntry(missing, "MISSING"));
        pieEntryList.add(new PieEntry(found, "FOUND"));
        pieEntryList.add(new PieEntry(extra, "EXTRA"));
        pieEntryList.add(new PieEntry(mismatch, "MISMATCH"));

        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(11f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });


        pieChart.invalidate();
    }

    @Override
    protected void onResume() {
        updateCount();
        super.onResume();
    }
}
