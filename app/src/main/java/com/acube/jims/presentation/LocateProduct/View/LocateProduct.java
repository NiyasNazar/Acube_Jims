package com.acube.jims.presentation.LocateProduct.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.databinding.DataBindingUtil;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.acube.jims.BaseActivity;
import com.acube.jims.presentation.Audit.AuditReadingActivity;
import com.acube.jims.presentation.reading.LocateScanActivity;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.presentation.LocateProduct.adapter.LocateItemAdapter;
import com.acube.jims.R;
import com.acube.jims.databinding.LocateProductFragmentBinding;
import com.acube.jims.datalayer.models.LocateProduct.LocateItem;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.utils.ReaderUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.interfaces.IUHF;
import com.rscja.deviceapi.interfaces.IUHFLocationCallback;
import com.skydoves.progressview.ProgressViewOrientation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LocateProduct extends BaseActivity {

    private LocateProductViewModel mViewModel;
    public RFIDWithUHFUART mReader;

    public static LocateProduct newInstance() {
        return new LocateProduct();
    }

    String serialNumber;
    LocateProductFragmentBinding binding;
    JSONObject jsonObjectserials;
    private ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.locate_product_fragment);

        binding.progressView1.setOrientation(ProgressViewOrientation.VERTICAL);
        binding.recylocateitems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().locateItemsDao().getAll().observe(this, new Observer<List<LocateItem>>() {
            @Override
            public void onChanged(List<LocateItem> locateItems) {
                binding.recylocateitems.setAdapter(new LocateItemAdapter(getApplicationContext(), locateItems));
            }
        });

        binding.btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serialnos = binding.edLocate.getText().toString();


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
                    LocateItem locateItem = new LocateItem();
                    locateItem.setSerialnumber(serialnos);
                    SaveItems(locateItem);

                   /* List<String> convertedList = Arrays.asList(serialnos.split(",", -1));
                    Log.d("TAG", "onClick: " + convertedList);
                    LocateItem locateItem;
                    for (int i = 0; i < convertedList.size(); i++) {


                    }*/

                }
            }
        });

    }

    private void getItems() {
        class GetTasks extends AsyncTask<Void, Void, List<LocateItem>> {

            @Override
            protected List<LocateItem> doInBackground(Void... voids) {
              /*  List<LocateItem> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .locateItemsDao().getAll();*/
                return null;
            }

            @Override
            protected void onPostExecute(List<LocateItem> responseItems) {
                super.onPostExecute(responseItems);
                Log.d("markItemSale", "markItemSale: " + responseItems.size());


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void LocateItems() {
        class GetTasks extends AsyncTask<Void, Void, List<LocateItem>> {

            @Override
            protected List<LocateItem> doInBackground(Void... voids) {
            /*    List<LocateItem> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .locateItemsDao().getAll();*/
                return null;
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
                    //   someActivityResultLauncher.launch(res);
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
                //getItems();

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
                //getItems();

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

    private void startLocation() {
        binding.waveLoadingView.setAnimDuration(1000);

        boolean result = mReader.startLocation(LocateProduct.this, "53524E30313330377C000000", IUHF.Bank_EPC, 32, new IUHFLocationCallback() {
            @Override
            public void getLocationValue(int Value) {
                Log.d("getLocationValue", "getLocationValue: " + Value);
                if (Value > 0 && Value < 40) {
                    //  binding.progressView1.getHighlightView().setColor(Color.RED);
                    binding.waveLoadingView.setWaveColor(Color.RED);

                } else if (Value > 40 && Value < 60) {
                    binding.waveLoadingView.setWaveColor(Color.MAGENTA);

                    //  binding.progressView1.getHighlightView().setColor(Color.MAGENTA);
                } else if (Value > 60 && Value < 80) {
                    binding.waveLoadingView.setWaveColor(Color.YELLOW);
                    //  binding.progressView1.getHighlightView().setColor(Color.YELLOW);
                } else if (Value > 80) {
                    binding.waveLoadingView.setWaveColor(Color.GREEN);

                    //   binding.progressView1.getHighlightView().setColor(Color.GREEN);
                }
                // binding.progressView1.setProgress(Value);
                //   binding.waveView.setProgress(Value);
                // binding.waveLoadingView.startAnimation();

                binding.waveView.setProgress(Value);
            }
        });


    }

    @Override
    protected void onDestroy() {
        ReaderUtils.freeSound();
        if (mReader != null) {
            mReader.free();
        }

        super.onDestroy();
    }

    public void ReaderInit() {
        try {
            mReader = RFIDWithUHFUART.getInstance();
            if (mReader != null) {
                new InitTask().execute();
            }
        } catch (Exception ex) {
            showerror(ex.getMessage());
            return;
        }


    }

    public class InitTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            mReader.free();
            return mReader.init();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            hideProgressDialog();
            if (!result) {
                Toast.makeText(LocateProduct.this, "Initialization fail", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LocateProduct.this, "Initialization Success", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showProgressDialog();

        }
    }

}