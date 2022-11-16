package com.acube.jims.presentation.Audit;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityAuditReadingBinding;
import com.acube.jims.databinding.ActivityAuditScanBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Audit.AuditSnapShot;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.remote.db.AppDatabase;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.Audit.adapter.AuditScannedAdapter;
import com.acube.jims.presentation.DeviceRegistration.View.DeviceRegistrationFragment;
import com.acube.jims.presentation.ReadingRangeSettings;
import com.acube.jims.presentation.Report.View.reports.FoundReportActivity;
import com.acube.jims.presentation.Report.View.reports.MisiingReport;
import com.acube.jims.presentation.Report.View.reports.TotalStockReport;
import com.acube.jims.presentation.reading.FindMissingReadingActivity;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.utils.OnSingleClickListener;
import com.acube.jims.utils.ReaderUtils;
import com.acube.jims.utils.Utils;
import com.acube.jims.utils.ViewDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.ConnectionStatus;
import com.rscja.deviceapi.interfaces.ConnectionStatusCallback;
import com.rscja.deviceapi.interfaces.ScanBTCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class AuditReadingActivity extends BaseActivity {
    ActivityAuditReadingBinding binding;
    public RFIDWithUHFUART mReader;
    public boolean isScanning = false;
    private HashMap<String, String> map;
    private ArrayList<HashMap<String, String>> tagList;
    private HashSet<String> hashSetTags;
    String auditID, macAddress, url;
    int systemLocationID, storeID, categoryId, itemID, subcatID;
    Boolean handheld;
    public RFIDWithUHFBLE uhf = RFIDWithUHFBLE.getInstance();
    public BluetoothDevice mDevice = null;
    BTStatus btStatus = new BTStatus();
    public String remoteBTName = "";
    public String remoteBTAdd = "";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            UHFTAGInfo info = (UHFTAGInfo) msg.obj;
            addDataToList(info.getEPC());
        }
    };
    private boolean mScanning, DeviceReg;
    String date = "";
    private static final long SCAN_PERIOD = 30000; //10 seconds

    ArrayList<TemDataSerial> dataset;
    public BluetoothAdapter mBtAdapter = null;
    private static final int ACCESS_FINE_LOCATION_PERMISSION_REQUEST = 100;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST = 101;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 102;
    private static final int REQUEST_ACTION_LOCATION_SETTINGS = 3;
    private Handler mBlHandler = new Handler();
    ViewDialog alert;
    int flag = 1;
    float blevalue, handheldvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_audit_reading);
        initToolBar(binding.toolbarApp.toolbar, "Inventory Scan", true);
        auditID = getIntent().getStringExtra("auditID");
        macAddress = getIntent().getStringExtra("macAddress");
        url = getIntent().getStringExtra("url");


        alert = new ViewDialog();
        systemLocationID = getIntent().getIntExtra("systemLocationID", 0);
        storeID = getIntent().getIntExtra("storeID", 0);
        categoryId = getIntent().getIntExtra("categoryId", 0);
        subcatID = getIntent().getIntExtra("subcatID", 0);
        itemID = getIntent().getIntExtra("itemID", 0);
        handheld = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "handheld");
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        DeleteTemp();
        binding.toolbarApp.settings.setVisibility(View.VISIBLE);
        binding.toolbarApp.settings.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReadingRangeSettings.class));
            }
        });
        binding.toolbarApp.imvlist.setVisibility(View.VISIBLE);
        if (handheld) {
            ReaderInit();
            checkLocationEnable();
        } else {
            checkLocationEnable();

            uhf.init(getApplicationContext());
        }

        updateCount();
        binding.toolbarApp.imvlist.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (binding.expandcount.getVisibility() == View.VISIBLE) {
                    binding.expandcount.setVisibility(View.GONE);
                    binding.seriallayout.setVisibility(View.VISIBLE);
                    binding.toolbarApp.imvlist.setImageResource(R.drawable.ic_view_tile_svgrepo_com);

                } else {
                    binding.expandcount.setVisibility(View.VISIBLE);
                    binding.seriallayout.setVisibility(View.GONE);
                    binding.toolbarApp.imvlist.setImageResource(R.drawable.ic_list_svgrepo_com);
                }

            }
        });

        Log.d("subversions", "updateCount: " + systemLocationID);

        ReaderUtils.initSound(AuditReadingActivity.this);
        binding.scannedrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getDownloadedAuditsbyAuditId(auditID).observe(this, new Observer<List<AuditSnapShot>>() {
            @Override
            public void onChanged(List<AuditSnapShot> inventoryAudits) {
                binding.scannedrecyclerview.setAdapter(new AuditScannedAdapter(getApplicationContext(), inventoryAudits));
            }
        });


        binding.BtInventory.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                if (handheld) {
                    startThread();
                } else {
                    if (DeviceReg) {
                        if (uhf.getConnectStatus() == ConnectionStatus.CONNECTED) {

                            readBlutoothTags();
                        } else {
                            alert.showDialog(AuditReadingActivity.this, "Connecting to " + macAddress);


                            if (checkLocationEnable()) {
                                showBluetoothDevice(false);
                            }
                        }
                    } else {
                        startActivity(new Intent(getApplicationContext(), DeviceRegistrationFragment.class));
                    }


                }

            }
        });
        binding.cdvtotalstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(), TotalStockReport.class)
                        .putExtra("systemLocationID", systemLocationID)
                        .putExtra("storeID", storeID)
                        .putExtra("categoryId", categoryId)
                        .putExtra("itemID", itemID)
                        .putExtra("url", AppConstants.BASE_URL)
                        .putExtra("auditID", auditID));


            }
        });


        binding.cdvmissing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(), MisiingReport.class)
                        .putExtra("systemLocationID", systemLocationID)
                        .putExtra("storeID", storeID)
                        .putExtra("categoryId", categoryId)
                        .putExtra("itemID", itemID)
                        .putExtra("url", AppConstants.BASE_URL)

                        .putExtra("auditID", auditID));


            }
        });

        binding.cdvfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(), FoundReportActivity.class)
                        .putExtra("systemLocationID", systemLocationID)
                        .putExtra("storeID", storeID)
                        .putExtra("flag", 1)

                        .putExtra("categoryId", categoryId)
                        .putExtra("itemID", itemID)
                        .putExtra("url", AppConstants.BASE_URL)
                        .putExtra("auditID", auditID));


            }
        });
        binding.cdvunknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(), FoundReportActivity.class)
                        .putExtra("systemLocationID", systemLocationID)
                        .putExtra("storeID", storeID)
                        .putExtra("flag", 2)
                        .putExtra("categoryId", categoryId)
                        .putExtra("itemID", itemID)
                        .putExtra("url", AppConstants.BASE_URL)
                        .putExtra("auditID", auditID));


            }
        });

    }

    private void showBluetoothDevice(boolean isHistory) {
        try {
            if (mBtAdapter == null) {
                showerror("Bluetooth is not available");
                return;
            }
            if (!mBtAdapter.isEnabled()) {
                Log.i(TAG, "onClick - BT not enabled yet");
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                bluetoothrequest.launch(enableIntent);
            } else {


                scanLeDevice(true);
                //  cancelDisconnectTimer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void scanLeDevice(final boolean enable) {

        try {


            if (enable) {
                //scanning bluetooth device for period of time
                mBlHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScanning = false;
                        uhf.stopScanBTDevices();

                    }
                }, SCAN_PERIOD);

                mScanning = true;
                uhf.startScanBTDevices(new ScanBTCallback() {
                    @Override
                    public void getDevices(final BluetoothDevice bluetoothDevice, final int rssi, byte[] bytes) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("MACADDRESS", "address ------------" + macAddress);
                                System.out.println("CHECK DATA RSSI ---- " + rssi);

                                //  MyDevice myDevice = new MyDevice(bluetoothDevice.getAddress(), bluetoothDevice.getName());

                                //  myDevice.addDevice(myDevice, rssi);

                                uhf.stopScanBTDevices();

                                String address = macAddress;
//                            String address = "D1:54:76:3C:4F:00";
//                             String address ="C6:7D:E6:E8:99:D0";
                                if (!TextUtils.isEmpty(address)) {
                                    setResult(Activity.RESULT_OK);
                                    mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
                                    // tvAddress.setText(String.format("%s(%s)\nconnecting", mDevice.getName(), address));
                                    connect(address);

                                } else {
                                    showerror("invalid_bluetooth_address");
                                }
                            }
                        });
                    }
                });

            } else {
                mScanning = false;
                uhf.stopScanBTDevices();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void connect(String deviceAddress) {
        if (uhf.getConnectStatus() == ConnectionStatus.CONNECTED) {
            alert.dismiss();


        } else if (uhf.getConnectStatus() == ConnectionStatus.CONNECTING) {
            showsuccess("Connecting" + macAddress);
        } else {
            uhf.connect(deviceAddress, btStatus);
        }
    }

    private void readBlutoothTags() {
        uhf.setPower((int) blevalue);
        if (uhf.startInventoryTag()) {
            dataset = new ArrayList<>();

            binding.BtInventory.setText("Stop");
            isScanning = true;
            //  setViewEnabled(false);
            new BleTagThread().start();
        } else {
            stopBLEInventory();
//					mContext.playSound(2);
        }


    }

    private boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    private boolean checkLocationEnable() {
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, ACCESS_FINE_LOCATION_PERMISSION_REQUEST);
                result = false;
            }
        }
        if (!isLocationEnabled()) {
            Utils.alert(this, R.string.get_location_permission, getString(R.string.tips_open_the_ocation_permission), R.drawable.ic_icon_jewelry, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, REQUEST_ACTION_LOCATION_SETTINGS);
                }
            });
        }
        return result;
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
                Toast.makeText(AuditReadingActivity.this, "Initialization fail", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AuditReadingActivity.this, "Initialization Success", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showProgressDialog();

        }
    }

    @Override
    protected void onDestroy() {
        ReaderUtils.freeSound();
        if (mReader != null) {
            mReader.free();
        }
        if (uhf != null) {
            uhf.free();
        }
        super.onDestroy();
    }

    public void startThread() {

        mReader.setPower((int) handheldvalue);
        if (mReader.startInventoryTag()) {
            dataset = new ArrayList<>();

            binding.BtInventory.setText("Stop");
            isScanning = true;
            //  setViewEnabled(false);


            new TagThread().start();
        } else {
            stopInventory();
//					mContext.playSound(2);
        }

    }


    private void stopInventory() {
        if (isScanning) {
            isScanning = false;
            // setViewEnabled(true);
            if (mReader.stopInventory()) {
                binding.BtInventory.setText("Start");
            } else {
                showerror("Inventory Stop Failed");
            }
        }
    }

    private void stopBLEInventory() {
        if (isScanning) {
            isScanning = false;
            // setViewEnabled(true);
            if (uhf.stopInventory()) {
                binding.BtInventory.setText("Start");
            } else {
                showerror("Inventory Stop Failed");
            }
        }
    }

    class TagThread extends Thread {
        private HashSet<String> hashSetTags;

        TagThread() {
            hashSetTags = new HashSet<>();
        }

        public void run() {
            UHFTAGInfo uhftagInfo;
            Message msg;
            while (isScanning) {
                uhftagInfo = mReader.readTagFromBuffer();
                if (uhftagInfo != null) {
                    String epc = uhftagInfo.getEPC();

                    if (this.hashSetTags.contains(epc)) {
                        Log.i("tagExists", "EPC Already Exists:" + epc);

                    } else {
                        this.hashSetTags.add(epc);
                        msg = handler.obtainMessage();
                        msg.obj = uhftagInfo;
                        handler.sendMessage(msg);

                    }


                }
            }
        }
    }

    private void addDataToList(String epc) {
        Log.d("addDataToList", "addDataToList: " + epc);
        String epcCode = HexToString(epc);

        if (epcCode.startsWith(getPrefix()) && epcCode.endsWith(getSuffix())) {
            epcCode = epcCode.substring(1, epcCode.lastIndexOf(getSuffix()));

            //   Log.d("addDataToList", "addDataToList: " + HexToString(epc));
            InsertTempserials(new TemDataSerial(epcCode, auditID));
            ReaderUtils.playSound(1);
        }
    }


    private void InsertTempserials(TemDataSerial dataset) {


        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().insertTemporarySerialNosAudit(dataset);
            runOnUiThread(this::checkexists);
        }).start();

    }

    private void checkexists() {

        Log.d("CheckExist", "checkexists: " + auditID + systemLocationID + categoryId + subcatID + storeID + date);

        String username = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "EmployeeName");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        }

        new Thread(() -> {
            int check = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().updateAudit(auditID, systemLocationID, categoryId, subcatID, storeID, date, username);
            Log.d("TAG", "checkexists: " + check);
            String query = "INSERT INTO AuditSnapShot (auditID,locationId, scanLocationID,serialNumber,status,subCategoryId) Select '" + auditID + "','" + systemLocationID + "','" + systemLocationID + "'," + "SerialNo,'2','0' from TemDataSerial where SerialNo NOT IN(Select serialNumber from AuditSnapShot where auditID ='" + auditID + "')";
            Boolean insert = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().unkwowninsert((new SimpleSQLiteQuery(query)));
         /*   if (categoryId != 0) {
                Log.d("TAG", "checkexistswithoucat: ");
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().checkauditscanwithcat(auditID, systemLocationID, categoryId);

            } else {
                Log.d("TAG", "checkexistscat: ");

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().checkauditscan(auditID, systemLocationID);
                String query = "INSERT INTO InventoryAudit (auditID,systemLocationID, scanLocationID,serialNumber,status,categoryID) Select '" + auditID + "','" + systemLocationID + "','" + systemLocationID + "'," + "SerialNo,'2','0' from TemDataSerial where SerialNo NOT IN(Select serialNumber from InventoryAudit where auditID ='" + auditID + "')";
                Boolean insert = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().unkwowninsert((new SimpleSQLiteQuery(query)));

            }*/

            runOnUiThread(() -> {
                hideProgressDialog();
                updateCount();
            });
        }).start();
    }

    private void updateCount() {
        Log.d("Subversions", "updateCount: " + "loc" + auditID + "cat" + categoryId + "item" + itemID + "storeID" + storeID);
        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getallcountbycat(auditID, 0, 1, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.tvtotalstock.setText(String.valueOf(integer));
                        binding.tvtotal.setText("Total \n " + integer);

                        binding.tvtotalpcs.setText("Pcs " + integer);
                    }

                });
        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getSum(auditID, 0, 1, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<Double>() {
                    @Override
                    public void onChanged(Double integer) {
                        binding.tvtotalweight.setText("Grs.Wt " + String.format("%.2f", integer) + " g");
                    }

                });

        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 1, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.tvfound.setText(String.valueOf(integer));
                        binding.found.setText("Found \n " + integer);
                        binding.tvfoundtotalpcs.setText("Pcs " + integer);

                    }
                });

        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getSum(auditID, 1, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<Double>() {
                    @Override
                    public void onChanged(Double integer) {
                        binding.tvfoundtotalweight.setText("Grs.Wt " + String.format("%.2f", integer) + " g");


                    }
                });

        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getSum(auditID, 0, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<Double>() {
                    @Override
                    public void onChanged(Double integer) {
                        binding.tvtotalmissingweight.setText("Grs.Wt " + String.format("%.2f", integer) + " g");


                    }
                });


        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 0, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.missing.setText("Missing \n " + integer);
                        binding.tvmissing.setText(String.valueOf(integer));
                        binding.tvtotalmissingpcs.setText("Pcs " + integer);
                    }
                });
        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 2, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.unknown.setText("Unknown \n " + integer);

                        binding.tvunknown.setText(String.valueOf(integer));
                        binding.tvtotalunknownpcs.setText("Pcs " + integer);

                    }
                });

        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getSum(auditID, 2, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<Double>() {
                    @Override
                    public void onChanged(Double integer) {
                        binding.tvtotalunknownweight.setText("Grs.Wt " + String.format("%.2f", integer) + " g");


                    }
                });

        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getcount(auditID, 3, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.locationmismatch.setText("Mismatch \n " + integer);

                        binding.tvlocationmismatch.setText(String.valueOf(integer));
                        binding.tvtotalmismatchpcs.setText("Pcs " + integer);

                    }
                });

        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().auditDownloadDao().getSum(auditID, 3, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<Double>() {
                    @Override
                    public void onChanged(Double integer) {
                        binding.tvtotalmismatchweight.setText("Grs.Wt " + String.format("%.2f", integer) + " g");


                    }
                });

    }

    class BTStatus implements ConnectionStatusCallback<Object> {
        @Override
        public void getStatus(final ConnectionStatus connectionStatus, final Object device1) {
            runOnUiThread(new Runnable() {
                public void run() {
                    BluetoothDevice device = (BluetoothDevice) device1;
                    remoteBTName = "";
                    remoteBTAdd = "";
                    if (connectionStatus == ConnectionStatus.CONNECTED) {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        remoteBTName = device.getName();
                        remoteBTAdd = device.getAddress();
                        alert.dismiss();
                        showsuccess("Bluetooth Connected Succesfully");


                    } else if (connectionStatus == ConnectionStatus.DISCONNECTED) {

                    }


                }
            });
        }
    }


    ActivityResultLauncher<Intent> bluetoothrequest = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        showsuccess("Bluetooth has turned on");
                    } else {
                        showerror("Problem in BT Turning ON ");
                    }
                }
            });

    class BleTagThread extends Thread {
        private HashSet<String> hashSetTags;

        BleTagThread() {
            hashSetTags = new HashSet<>();
        }

        public void run() {
            UHFTAGInfo uhftagInfo;
            Message msg;
            while (isScanning) {
                uhftagInfo = uhf.readTagFromBuffer();
                if (uhftagInfo != null) {
                    String epc = uhftagInfo.getEPC();

                    if (this.hashSetTags.contains(epc)) {
                        Log.i("tagExists", "EPC Already Exists:" + epc);

                    } else {
                        this.hashSetTags.add(epc);
                        msg = handler.obtainMessage();
                        msg.obj = uhftagInfo;
                        handler.sendMessage(msg);

                    }


                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DeviceReg = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "DeviceReg");
        macAddress = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "TrayMacAddress");
        blevalue = LocalPreferences.retrieveReadFloatPreferences(getApplicationContext(), "handheldble");
        handheldvalue = LocalPreferences.retrieveReadFloatPreferences(getApplicationContext(), "handheldvalue");
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}