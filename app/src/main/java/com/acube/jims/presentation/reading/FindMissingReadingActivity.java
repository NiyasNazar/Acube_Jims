package com.acube.jims.presentation.reading;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityAuditReadingBinding;
import com.acube.jims.databinding.ActivityFindmissingReadingBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.models.SelectionHolder;
import com.acube.jims.datalayer.remote.db.AppDatabase;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.Audit.AuditReadingActivity;
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

public class FindMissingReadingActivity extends BaseActivity {
    ActivityFindmissingReadingBinding binding;
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
    private boolean mScanning;
    List<SelectionHolder> temDataSerials;
    List<SelectionHolder> temDataScanned;
    private static final long SCAN_PERIOD = 30000; //10 seconds
    float blevalue, handheldvalue;
    ArrayList<TemDataSerial> dataset;
    public BluetoothAdapter mBtAdapter = null;
    private static final int ACCESS_FINE_LOCATION_PERMISSION_REQUEST = 100;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST = 101;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 102;
    private static final int REQUEST_ACTION_LOCATION_SETTINGS = 3;
    private Handler mBlHandler = new Handler();
    ViewDialog alert;
    boolean DeviceReg;
    HashSet<String> hashSet;
    private ScanAdapter adapter;
    String epcCode;
    String username, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_findmissing_reading);
        initToolBar(binding.toolbarApp.toolbar, "Inventory Scan", true);
        auditID = getIntent().getStringExtra("auditID");
        alert = new ViewDialog();
        username = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "EmployeeName");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        }

        url = getIntent().getStringExtra("url");
        temDataSerials = new ArrayList<>();
        temDataScanned = new ArrayList<>();
        DeviceReg = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "DeviceReg");

        systemLocationID = getIntent().getIntExtra("systemLocationID", 0);
        storeID = getIntent().getIntExtra("storeID", 0);
        categoryId = getIntent().getIntExtra("categoryId", 0);
        itemID = getIntent().getIntExtra("itemID", 0);
        handheld = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "handheld");
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        binding.toolbarApp.settings.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReadingRangeSettings.class));
            }
        });
        DeleteTemp();
        if (handheld) {
            ReaderInit();
        } else {
            checkLocationEnable();
            uhf.init(getApplicationContext());
        }
        adapter = new ScanAdapter(FindMissingReadingActivity.this);
        binding.LvTags.setAdapter(adapter);
        ReaderUtils.initSound(FindMissingReadingActivity.this);
        DatabaseClient.getInstance(FindMissingReadingActivity.this).getAppDatabase().auditDownloadDao().getTempSerials().observe(this, new Observer<List<SelectionHolder>>() {
            @Override
            public void onChanged(List<SelectionHolder> dataSerials) {
                if (dataSerials != null) {
                    temDataSerials = dataSerials;
                    temDataScanned = dataSerials;
                    adapter.notifyDataSetChanged();
                    Log.d("Tempserials", "onChanged: " + temDataSerials.size());


                }
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
                            alert.showDialog(FindMissingReadingActivity.this, "Connecting to " + macAddress);


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
                Toast.makeText(FindMissingReadingActivity.this, "Initialization fail", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FindMissingReadingActivity.this, "Initialization Success", Toast.LENGTH_SHORT).show();
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
        super.onDestroy();
    }

    public void startThread() {

        mReader.setPower((int) handheldvalue);
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
        epcCode = "";
        epcCode = HexToString(epc);

        if (epcCode.startsWith(getPrefix()) && epcCode.endsWith(getSuffix())) {
            epcCode = epcCode.substring(1, epcCode.lastIndexOf(getSuffix()));

            new Thread(() -> {
                int status = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().updateFindmissing(auditID, systemLocationID, categoryId, itemID, storeID, epcCode, date, username);
                Log.d("UpdateStatus", "addDataToList: " + status + epcCode);
                if (status == 1) {
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().updateSelectionHolder(epcCode);
                    ReaderUtils.playSound(1);

                }

            }).start();
            adapter.notifyDataSetChanged();

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
                public void run() {
                    BluetoothDevice device = (BluetoothDevice) device1;
                    remoteBTName = "";
                    remoteBTAdd = "";
                    if (connectionStatus == ConnectionStatus.CONNECTED) {
                        remoteBTName = device.getName();
                        remoteBTAdd = device.getAddress();
                        showsuccess("Bluetooth Connected Succesfully");
                        alert.dismiss();

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

    public class ScanAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ScanAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return temDataScanned.size();
        }

        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return temDataScanned.get(arg0);
        }

        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        public final class ViewHolder {
            public TextView tvEPCTID;
            public TextView tvTagCount;
            public ImageView tvTagRssi;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.scan_items, null);
                holder.tvEPCTID = (TextView) convertView.findViewById(R.id.TvTagUii);
                holder.tvTagCount = (TextView) convertView.findViewById(R.id.TvTagCount);
                holder.tvTagRssi = (ImageView) convertView.findViewById(R.id.TvTagRssi);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (temDataScanned.get(position).getStatus() == 0) {
                holder.tvTagRssi.setVisibility(View.GONE);
            } else {
                holder.tvTagRssi.setVisibility(View.VISIBLE);

            }

            holder.tvEPCTID.setText((String) temDataScanned.get(position).getSerialNumber());

         /*   double val = Math.abs(Double.parseDouble(rssi));

            if (val < 80.0 && val > 60.0) {
                holder.tvTagRssi.setBackgroundColor(getResources().getColor(R.color.orange));
                holder.tvTagRssi.setText("Around");

            } else if (val <= 60.0 && val > 35.0) {
                holder.tvTagRssi.setBackgroundColor(getResources().getColor(R.color.yellow));
                holder.tvTagRssi.setText("Near");

            } else if (val <= 35.0) {
                holder.tvTagRssi.setBackgroundColor(getResources().getColor(R.color.green3));
                holder.tvTagRssi.setText("Found");

            } else {
                holder.tvTagRssi.setText("Far");
                holder.tvTagRssi.setBackgroundColor(getResources().getColor(R.color.white));

            }*/


            return convertView;
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
}