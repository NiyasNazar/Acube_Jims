package com.acube.jims.presentation.reading;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityFindmissingReadingBinding;
import com.acube.jims.databinding.ActivityLocateScanBinding;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogDetails;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.LocateProduct.View.LocateProduct;
import com.acube.jims.presentation.SplashActivity;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.utils.OnSingleClickListener;
import com.acube.jims.utils.ReaderUtils;
import com.acube.jims.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.ConnectionStatus;
import com.rscja.deviceapi.interfaces.ConnectionStatusCallback;
import com.rscja.deviceapi.interfaces.IUHF;
import com.rscja.deviceapi.interfaces.IUHFLocationCallback;
import com.rscja.deviceapi.interfaces.ScanBTCallback;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocateScanActivity extends BaseActivity {
    ActivityLocateScanBinding binding;
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
    double val = 0.0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            UHFTAGInfo info = (UHFTAGInfo) msg.obj;
            addDataToList(info.getEPC(), mergeTidEpc(info.getTid(), info.getEPC(), info.getUser()), info.getRssi());
            Log.d("handleMessage", "handleMessage: ");
        }
    };
    LayerDrawable progressBarDrawable;
    Drawable progressDrawable;
    private boolean mScanning;
    List<TemDataSerial> temDataSerials;
    List<TemDataSerial> temDataScanned;
    private static final long SCAN_PERIOD = 30000; //10 seconds

    ArrayList<TemDataSerial> dataset;
    public BluetoothAdapter mBtAdapter = null;
    private static final int ACCESS_FINE_LOCATION_PERMISSION_REQUEST = 100;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST = 101;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 102;
    private static final int REQUEST_ACTION_LOCATION_SETTINGS = 3;
    private Handler mBlHandler = new Handler();
    String serialnos;

    HashSet<String> hashSet;
    private FindMissingReadingActivity.ScanAdapter adapter;
    String epcCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_locate_scan);
        initToolBar(binding.toolbarApp.toolbar, "Inventory Scan", true);
        auditID = getIntent().getStringExtra("auditID");
        macAddress = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "TrayMacAddress");

        url = getIntent().getStringExtra("url");
        temDataSerials = new ArrayList<>();
        temDataScanned = new ArrayList<>();
        serialnos = getIntent().getStringExtra("serialnos");
        getDetails();

        systemLocationID = getIntent().getIntExtra("systemLocationID", 0);
        storeID = getIntent().getIntExtra("storeID", 0);
        categoryId = getIntent().getIntExtra("categoryId", 0);
        itemID = getIntent().getIntExtra("itemID", 0);
        handheld = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "handheld");
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        DeleteTemp();
        if (handheld) {
            ReaderInit();
        } else {
            checkLocationEnable();
            uhf.init(getApplicationContext());
        }
        ReaderUtils.initSound(LocateScanActivity.this);
        setcolorFilter();
        binding.waveLoadingView.setProgressValue(5);
        binding.waveLoadingView.startAnimation();
        binding.waveLoadingView.setWaveColor(Color.parseColor("#C41E3A"));

        binding.waveLoadingView.setAnimDuration(2000);
        binding.waveLoadingView.setWaveBgColor(Color.parseColor("#FAFAFA"));


        binding.BtInventory.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                //   new asyncTaskUpdateProgress().execute();
                if (handheld) {
                    if (isScanning) {
                        stopLocation();
                    } else {
                        startLocation();
                    }

                } else {
                    if (isScanning){
                        stopBLELocation();
                    }else{
                        if (checkLocationEnable()) {
                            showBluetoothDevice(false);
                        }
                    }




                   /* if (!mBtAdapter.isEnabled()) {
                        Log.i(TAG, "onClick - BT not enabled yet");
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        bluetoothrequest.launch(enableIntent);
                    } else {

                            mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);
                            connect(macAddress);

                    }*/


                }

            }
        });


    }

    private void getDetails() {
        RetrofitInstance.getApiService(getApplicationContext()).getItemDetails(LocalPreferences.getToken(getApplicationContext()), serialnos).enqueue(new Callback<ResponseCatalogDetails>() {
            @Override
            public void onResponse(Call<ResponseCatalogDetails> call, Response<ResponseCatalogDetails> response) {
                if (response.body() != null) {
                    ResponseCatalogDetails responseCatalogDetails = response.body();
                    binding.tvItemName.setText(responseCatalogDetails.getSerialNumber());


                    Glide.with(getApplicationContext())
                            .load(responseCatalogDetails.getImageFilePath())
                            .placeholder(R.drawable.jwimage)
                            .error(R.drawable.jwimage)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    // log exception
                                    Log.e("TAG", "Error loading image", e);
                                    return false; // important to return false so the error placeholder can be placed
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(binding.imageView);

                }
            }

            @Override
            public void onFailure(Call<ResponseCatalogDetails> call, Throwable t) {

            }
        });


    }

    private void setcolorFilter() {

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
            //readBlutoothTags();
            startLocationBLE();

        } else if (uhf.getConnectStatus() == ConnectionStatus.CONNECTING) {
            showsuccess("Connecting" + macAddress);
        } else {
            uhf.connect(deviceAddress, btStatus);
        }
    }

    private void startLocationBLE() {
        serialnos = getPrefix() + serialnos + getSuffix();
        StringBuffer sb = new StringBuffer();
        //Converting string to character array
        char ch[] = serialnos.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            String hexString = Integer.toHexString(ch[i]);
            sb.append(hexString);
        }
        String EPC = sb.toString();
        Log.d("startLocation", "startLocation: " + EPC);
        /*  53524E30313330377C000000*/
        boolean result = uhf.startLocation(LocateScanActivity.this, EPC, IUHF.Bank_EPC, 32, new IUHFLocationCallback() {
            @Override
            public void getLocationValue(int data) {
                ReaderUtils.playSound(1);
                if (data < 30) {
                    binding.waveLoadingView.setCenterTitle("Far");
                    binding.waveLoadingView.setWaveColor(Color.parseColor("#C41E3A"));
                    binding.waveLoadingView.setProgressValue(data);
                } else {
                    binding.waveLoadingView.setProgressValue(data);

                }
                int Value = binding.waveLoadingView.getProgressValue();

                Log.d("getLocationValue", "getLocationValue: " + Value);
                if (Value > 0 && Value <= 30) {
                    binding.waveLoadingView.setCenterTitle("Far");
                    binding.waveLoadingView.setWaveColor(Color.parseColor("#C41E3A"));

                } else if (Value > 30 && Value < 60) {
                    binding.waveLoadingView.setCenterTitle("Around");

                    binding.waveLoadingView.setWaveColor(Color.parseColor("#FFA500"));
                } else if (Value > 60 && Value < 85) {
                    binding.waveLoadingView.setCenterTitle("Near");

                    binding.waveLoadingView.setWaveColor(Color.parseColor("#FFEA00"));

                } else if (Value > 85) {
                    binding.waveLoadingView.setWaveColor(Color.parseColor("#008000"));
                    binding.waveLoadingView.setCenterTitle("Found");


                }

            }
        });
        if (!result) {

            showerror("Failed please try after some time");
            return;
        } else {
            isScanning = true;
            binding.tvbuttontext.setText("Stop");
        }


    }

    private void readBlutoothTags() {
        uhf.setPower((int) 30.0);
        if (uhf.startInventoryTag()) {
            dataset = new ArrayList<>();

            binding.tvbuttontext.setText("Stop");
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
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_PERMISSION_REQUEST);
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
        } catch (Exception ex) {
            showerror(ex.getMessage());
            return;
        }
        if (mReader != null) {
            new InitTask().execute();
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
                Toast.makeText(LocateScanActivity.this, "Initialization fail", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LocateScanActivity.this, "Initialization Success", Toast.LENGTH_SHORT).show();
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

    private void stopBLELocation() {
        uhf.stopLocation();
    }

    private void stopLocation() {
        binding.tvbuttontext.setText("Locate");
        isScanning = false;
        mReader.stopLocation();







    }

    public void startThread() {

        mReader.setPower((int) 30.0);
        if (mReader.startInventoryTag()) {
            dataset = new ArrayList<>();

            binding.tvbuttontext.setText("Stop");
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
                binding.tvbuttontext.setText("Start");
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
                binding.tvbuttontext.setText("Start");
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


                    }
                    msg = handler.obtainMessage();
                    msg.obj = uhftagInfo;
                    handler.sendMessage(msg);

                }
            }
        }
    }

    private void addDataToList(String epc, String epcAndTidUser, String rssi) {

        epcCode = "";
        epcCode = HexToString(epc);

        if (epcCode.startsWith(getPrefix()) && epcCode.endsWith(getSuffix())) {
            epcCode = epcCode.substring(1, epcCode.lastIndexOf(getSuffix()));



            if (epcCode.equalsIgnoreCase("2200000813")) {
                Log.d("addDataToList", "addDataToList: " + rssi);
                val = Math.abs(Double.parseDouble(rssi));

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //do stuff like remove view etc

                    }
                });

            }

            //     ReaderUtils.playSound(1);
            update();

        }


    }

    private void update() {

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

    private String mergeTidEpc(String tid, String epc, String user) {
        String data = "EPC:" + epc;
        if (!TextUtils.isEmpty(tid) && !tid.equals("0000000000000000") && !tid.equals("000000000000000000000000")) {
            data += "\nTID:" + tid;
        }
        if (user != null && user.length() > 0) {
            data += "\nUSER:" + user;
        }
        return data;
    }

    private void checkexists() {
        int systemID = 0;
        new Thread(() -> {
            // DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().updateFindmissing(auditID, systemLocationID, categoryId, itemID, storeID);

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
            });
        }).start();
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
                        showsuccess("remoteBT" + remoteBTName);


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
                    msg = handler.obtainMessage();
                    msg.obj = uhftagInfo;
                    handler.sendMessage(msg);
                    if (this.hashSetTags.contains(epc)) {
                        Log.i("tagExists", "EPC Already Exists:" + epc);

                    } else {
                        this.hashSetTags.add(epc);


                    }


                }
            }
        }
    }

    private void startLocation() {
        serialnos = getPrefix() + serialnos +getSuffix();
        StringBuffer sb = new StringBuffer();
        //Converting string to character array
        char ch[] = serialnos.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            String hexString = Integer.toHexString(ch[i]);
            sb.append(hexString);
        }
        String EPC = sb.toString();
        Log.d("startLocation", "startLocation: " + EPC);
        /*  53524E30313330377C000000*/
        boolean result = mReader.startLocation(LocateScanActivity.this, EPC, IUHF.Bank_EPC, 32, new IUHFLocationCallback() {
            @Override
            public void getLocationValue(int data) {
                ReaderUtils.playSound(1);
                if (data < 30) {
                    binding.waveLoadingView.setCenterTitle("Far");
                    binding.waveLoadingView.setWaveColor(Color.parseColor("#C41E3A"));
                    binding.waveLoadingView.setProgressValue(30);
                } else {
                    binding.waveLoadingView.setProgressValue(data);

                }
                int Value = binding.waveLoadingView.getProgressValue();

                Log.d("getLocationValue", "getLocationValue: " + Value);
                if (Value > 0 && Value <= 30) {
                    binding.waveLoadingView.setCenterTitle("Far");
                    binding.waveLoadingView.setWaveColor(Color.parseColor("#C41E3A"));

                } else if (Value > 30 && Value < 60) {
                    binding.waveLoadingView.setCenterTitle("Around");

                    binding.waveLoadingView.setWaveColor(Color.parseColor("#FFA500"));
                } else if (Value > 60 && Value < 85) {
                    binding.waveLoadingView.setCenterTitle("Near");

                    binding.waveLoadingView.setWaveColor(Color.parseColor("#FFEA00"));

                } else if (Value > 85) {
                    binding.waveLoadingView.setWaveColor(Color.parseColor("#008000"));
                    binding.waveLoadingView.setCenterTitle("Found");


                }

            }
        });
        if (!result) {

            showerror("Failed please try after some time");
            return;
        } else {
            isScanning = true;
            binding.tvbuttontext.setText("Stop");
        }


    }

}