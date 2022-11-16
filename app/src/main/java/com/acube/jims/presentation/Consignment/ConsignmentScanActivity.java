package com.acube.jims.presentation.Consignment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityAuditReadingBinding;
import com.acube.jims.databinding.ActivityConsignmentBinding;
import com.acube.jims.databinding.ActivityConsignmentScanBinding;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Audit.AuditSnapShot;
import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.models.ConsignmentLine;
import com.acube.jims.datalayer.models.OfflineConsignment;
import com.acube.jims.datalayer.models.ResponseConsignment;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.Audit.AuditFragment;
import com.acube.jims.presentation.Audit.AuditReadingActivity;
import com.acube.jims.presentation.Audit.adapter.AuditScannedAdapter;
import com.acube.jims.presentation.Consignment.adapter.ConsigScannedadapter;
import com.acube.jims.presentation.DeviceRegistration.View.DeviceRegistrationFragment;
import com.acube.jims.presentation.ReadingRangeSettings;
import com.acube.jims.presentation.Report.View.reports.FoundReportActivity;
import com.acube.jims.presentation.Report.View.reports.MisiingReport;
import com.acube.jims.presentation.Report.View.reports.TotalStockReport;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.utils.OnSingleClickListener;
import com.acube.jims.utils.ReaderUtils;
import com.acube.jims.utils.Utils;
import com.acube.jims.utils.ViewDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.ConnectionStatus;
import com.rscja.deviceapi.interfaces.ConnectionStatusCallback;
import com.rscja.deviceapi.interfaces.ScanBTCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsignmentScanActivity extends BaseActivity implements ConsigScannedadapter.PassId {
    ActivityConsignmentScanBinding binding;
    public RFIDWithUHFUART mReader;
    public boolean isScanning = false;
    private HashMap<String, String> map;
    private ArrayList<HashMap<String, String>> tagList;
    private HashSet<String> hashSetTags;
    String auditID, macAddress, url;
    int systemLocationID, storeID, categoryId, itemID;
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
    List<ConsignmentLine> consignmentLinesdata;
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
    List<OfflineConsignment> consigndataset;
    String consignmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_consignment_scan);
        initToolBar(binding.toolbarApp.toolbar, "Inventory Scan", true);
        auditID = getIntent().getStringExtra("auditID");
        macAddress = getIntent().getStringExtra("macAddress");
        url = getIntent().getStringExtra("url");


        alert = new ViewDialog();
        systemLocationID = getIntent().getIntExtra("systemLocationID", 0);
        storeID = getIntent().getIntExtra("storeID", 0);
        categoryId = getIntent().getIntExtra("categoryId", 0);
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
        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().homeMenuDao().getofflineconsignment().observe(this, new Observer<List<OfflineConsignment>>() {
            @Override
            public void onChanged(List<OfflineConsignment> consignmentLines) {
                consigndataset = consignmentLines;
                ArrayAdapter<OfflineConsignment> arrayAdapter = new ArrayAdapter<OfflineConsignment>(ConsignmentScanActivity.this, android.R.layout.simple_spinner_item, consignmentLines);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinconsignments.setAdapter(arrayAdapter);
            }
        });
        binding.spinconsignments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                consignmentID = consigndataset.get(i).getConsignmentId();
                showlist(consignmentID);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (handheld) {
            ReaderInit();
            checkLocationEnable();
        } else {
            checkLocationEnable();

            uhf.init(getApplicationContext());
        }

        updateCount();

        binding.BtReset.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                new AlertDialog.Builder(ConsignmentScanActivity.this)
                        .setTitle("Reset")
                        .setMessage("Are you sure you want to reset all entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(() -> {
                                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().homeMenuDao().resetconsigmnment(consignmentID, 50);

                                    runOnUiThread(() -> {

                                        updateCount();
                                    });
                                }).start();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
        binding.Btupload.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (consignmentID.equalsIgnoreCase("")) {
                    showerror("Select a consignment");

                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ConsignmentScanActivity.this);
                    alert.setTitle("Upload Consignment");
                    // this is set the view from XML inside AlertDialog
                    // disallow cancel of AlertDialog on click of back button and outside touch
                    alert.setCancelable(false);
                    alert.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show());

                    alert.setPositiveButton("Proceed", (dialog, which) -> {


                        JsonObject body = new JsonObject();
                        JsonArray jsonArray = new JsonArray();
                        body.addProperty("consignmentId", consignmentID);
                        JsonObject serial;
                        for (int i = 0; i < consignmentLinesdata.size(); i++) {
                            serial = new JsonObject();
                            serial.addProperty("serialNumber", consignmentLinesdata.get(i).getSerialNumber());
                            serial.addProperty("status", consignmentLinesdata.get(i).getStatus());
                            jsonArray.add(serial);
                        }
                        body.add("consignmentLines", jsonArray);
                        uploadconsignment(body);


                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                }


            }
        });


        Log.d("subversions", "updateCount: " + systemLocationID);

        ReaderUtils.initSound(ConsignmentScanActivity.this);
        binding.scannedrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


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
                            alert.showDialog(ConsignmentScanActivity.this, "Connecting to " + macAddress);


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


    }

    private void uploadconsignment(JsonObject body) {
        showProgressDialog();
        RetrofitInstance.getApiService(getApplicationContext()).uploadconsigment(LocalPreferences.getToken(getApplicationContext()), body).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    if (response.body()) {
                        new Thread(() -> {
                            // do background stuff here
                            DatabaseClient.getInstance(ConsignmentScanActivity.this).getAppDatabase().homeMenuDao().deleteconsignmentId(consignmentID);
                            runOnUiThread(() -> {
                                showsuccess("Uploaded Successfully");
                                // OnPostExecute stuff here
                            });
                        }).start();
                    }


                } else {
                    showerror("Failed");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                hideProgressDialog();
                showerror("Failed");

            }
        });
    }

    private void showlist(String consignmentID) {
        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().homeMenuDao().getconsignmentId(consignmentID).observe(this, new Observer<List<ConsignmentLine>>() {
            @Override
            public void onChanged(List<ConsignmentLine> consignmentLines) {
                consignmentLinesdata = new ArrayList<>();
                consignmentLinesdata = consignmentLines;
                binding.scannedrecyclerview.setAdapter(new ConsigScannedadapter(getApplicationContext(), consignmentLines, ConsignmentScanActivity.this));
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

    @Override
    public void passid(String id, String serial) {
        new AlertDialog.Builder(ConsignmentScanActivity.this)
                .setTitle("Mark as sold")
                .setMessage("Are you sure you want to mark  this entry as sold?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updatestatus(id, serial);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void updatestatus(String id, String serial) {

        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().homeMenuDao().updatesalestatus(id, 60, serial);


            runOnUiThread(() -> {

                updateCount();
            });
        }).start();
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
                Toast.makeText(ConsignmentScanActivity.this, "Initialization fail", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ConsignmentScanActivity.this, "Initialization Success", Toast.LENGTH_SHORT).show();
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

        if (epcCode.startsWith("S")) {
            epcCode = epcCode.substring(1, epcCode.lastIndexOf("|"));

            //   Log.d("addDataToList", "addDataToList: " + HexToString(epc));
            InsertTempserials(new TemDataSerial(epcCode, auditID));
            ReaderUtils.playSound(1);
        }
    }

    public String HexToString(String hex) {
        hex = hex.replaceAll("^(00)+", "");
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return new String(bytes);
    }

    private void InsertTempserials(TemDataSerial dataset) {


        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().insertTemporarySerialNosAudit(dataset);
            runOnUiThread(this::checkexists);
        }).start();

    }

    private void checkexists() {

        String username = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "EmployeeName");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        }

        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().homeMenuDao().updateconsignment(consignmentID, 40);
            String query = "INSERT INTO ConsignmentLine (consignmentId, serialNumber,status) Select '" + consignmentID + "'," + "SerialNo,'70' from TemDataSerial where SerialNo NOT IN(Select serialNumber from ConsignmentLine where consignmentId ='" + consignmentID + "')";
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
        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().homeMenuDao().getallcountbycat(consignmentID, 50, 40, 60,30).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.tvtotal.setText("Total  \n " + integer);
                        Log.d("count", "onChanged: "+integer);

                    }

                });


        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().homeMenuDao().getcount(consignmentID, 40).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.found.setText("Found  \n " + integer);

                    }
                });


        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().homeMenuDao().getcount(consignmentID, 50).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.missing.setText("Missing  \n " + integer);
                    }
                });
        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().homeMenuDao().getcount(consignmentID, 70).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.unknown.setText("Unknown \n " + integer);


                    }
                });


        DatabaseClient.getInstance(getApplicationContext()).
                getAppDatabase().homeMenuDao().getcount(consignmentID, 60).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        binding.locationmismatch.setText("Sold \n " + integer);


                    }
                });


    }

    class BTStatus implements ConnectionStatusCallback<Object> {
        @Override
        public void getStatus(final ConnectionStatus connectionStatus, final Object device1) {
            runOnUiThread(new Runnable() {
                @SuppressLint("MissingPermission")
                public void run() {
                    BluetoothDevice device = (BluetoothDevice) device1;
                    remoteBTName = "";
                    remoteBTAdd = "";
                    if (connectionStatus == ConnectionStatus.CONNECTED) {
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