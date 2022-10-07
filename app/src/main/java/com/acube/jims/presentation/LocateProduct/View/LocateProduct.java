package com.acube.jims.presentation.LocateProduct.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.acube.jims.BaseActivity;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.presentation.LocateProduct.adapter.LocateItemAdapter;
import com.acube.jims.R;
import com.acube.jims.databinding.LocateProductFragmentBinding;
import com.acube.jims.datalayer.models.LocateProduct.LocateItem;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LocateProduct extends BaseActivity {

    private LocateProductViewModel mViewModel;

    public static LocateProduct newInstance() {
        return new LocateProduct();
    }

    String serialNumber;
    LocateProductFragmentBinding binding;
    JSONObject jsonObjectserials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.locate_product_fragment);
        binding.recylocateitems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocateItems();
            }
        });
        initToolBar(binding.toolbarApp.toolbar, "Locate Item", true);

        Context context = new ContextThemeWrapper(LocateProduct.this, R.style.AppTheme2);

        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(context)

                        .setTitle("Clear Data")
                        .setMessage("Are you sure you want to clear all data?")
                        .setPositiveButton("CLEAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DeleteAll();


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
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edLocate.setError(null);
                String serialnos = binding.edLocate.getText().toString();
                if (serialnos.equalsIgnoreCase("")) {
                    binding.edLocate.setError("Field Empty");

                } else {
                    List<String> convertedList = Arrays.asList(serialnos.split(",", -1));
                    Log.d("TAG", "onClick: " + convertedList);
                    LocateItem locateItem;
                    for (int i = 0; i < convertedList.size(); i++) {
                        locateItem = new LocateItem();
                        locateItem.setSerialnumber(convertedList.get(i));
                        SaveItems(locateItem);

                    }

                }
            }
        });

    }

    private void getItems() {
        class GetTasks extends AsyncTask<Void, Void, List<LocateItem>> {

            @Override
            protected List<LocateItem> doInBackground(Void... voids) {
                List<LocateItem> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .locateItemsDao().getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<LocateItem> responseItems) {
                super.onPostExecute(responseItems);
                Log.d("markItemSale", "markItemSale: " + responseItems.size());
                binding.recylocateitems.setAdapter(new LocateItemAdapter(getApplicationContext(), responseItems));


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void LocateItems() {
        class GetTasks extends AsyncTask<Void, Void, List<LocateItem>> {

            @Override
            protected List<LocateItem> doInBackground(Void... voids) {
                List<LocateItem> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .locateItemsDao().getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<LocateItem> locateItems) {
                super.onPostExecute(locateItems);
                String TrayMacAddress = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "TrayMacAddress");
                JSONArray jsonArray = new JSONArray();

                try {
                    for (int i = 0; i < locateItems.size(); i++) {
                        jsonObjectserials = new JSONObject();
                        serialNumber = locateItems.get(i).getSerialnumber();
                        jsonObjectserials.put("SerialNo", serialNumber);
                        jsonArray.put(jsonObjectserials);
                    }
                    String filepath = save(getApplicationContext(), jsonArray.toString());
                    /*    if (filepath == null) {SINGLELOCATE
                     *//*//*storage/emulated/0/Android/data/com.acube.jms/files/Missing.json*//*
                    } else {*/

                    Log.d("TrayMacAddress", "onPostExecute: " + TrayMacAddress);
                    Intent res = new Intent();
                    String mPackage = "com.acube.smarttray";// package name
                    String mClass = ".SmartTrayReading";//the activity name which return results*/
                    //  String mPackage = "com.example.acubetest";// package name
                    //  String mClass = ".MainActivity";//the activity name which return results
                    res.putExtra("token", LocalPreferences.getToken(getApplicationContext()));
                    res.putExtra("type", "SINGLELOCATE");
                    res.putExtra("url", AppConstants.BASE_URL);
                    res.putExtra("macAddress", TrayMacAddress);
                    res.putExtra("jsonSerialNo", serialNumber);
                    res.setComponent(new ComponentName(mPackage, mPackage + mClass));
                    someActivityResultLauncher.launch(res);
                    //   }
                } catch (Exception e) {
                    Toast.makeText(LocateProduct.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void DeleteAll() {
        class SavePlan extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .locateItemsDao()
                        .deleteall();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getItems();

            }
        }

        SavePlan st = new SavePlan();
        st.execute();
    }

    public void SaveItems(LocateItem items) {
        class SavePlan extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .locateItemsDao()
                        .insert(items);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                binding.edLocate.setText("");
                getItems();

            }
        }

        SavePlan st = new SavePlan();
        st.execute();
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        Intent data = result.getData();
                        if (data != null) {
                        }
                        Log.d("onActivityResult", "onActivityResult: " + data.getStringExtra("jsonSerialNo"));
                        //doSomeOperations();
                    }
                }
            });


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
}