package com.acube.jims.presentation.transfer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityConsignmentBinding;
import com.acube.jims.databinding.ActivityTransferactivityBinding;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Audit.ResponseLiveStore;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.models.ConsignmentLine;
import com.acube.jims.datalayer.models.OfflineConsignment;
import com.acube.jims.datalayer.models.ResponseConsignment;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.Audit.ZoneAssigning;
import com.acube.jims.presentation.Consignment.ConsignmentActivity;
import com.acube.jims.presentation.DeviceRegistration.View.DeviceRegistrationFragment;
import com.acube.jims.presentation.ReadingRangeSettings;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.utils.OnSingleClickListener;
import com.acube.jims.utils.ReaderUtils;
import com.acube.jims.utils.SearchableSpinners;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Transferactivity extends BaseActivity {
    ActivityTransferactivityBinding binding;
    public RFIDWithUHFUART mReader;
    public boolean isScanning = false;
    private HashMap<String, String> map;
    private List<String> tagList;
    String auditID, macAddress, url;
    int systemLocationID, storeID, categoryId, itemID;
    Boolean handheld;
    public RFIDWithUHFBLE uhf = RFIDWithUHFBLE.getInstance();
    public BluetoothDevice mDevice = null;
    BTStatus btStatus = new BTStatus();
    public String remoteBTName = "";
    public String remoteBTAdd = "";
    int FromstoreId = 0;
    int TostoreId = 0;

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
    Adapter adapter;
    List<ResponseLiveStore> storesdatset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_transferactivity);
        initToolBar(binding.toolbarApp.toolbar, "Transfer Create", true);
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
        tagList = new ArrayList<>();
        binding.scannedrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new Adapter(getApplicationContext());
        binding.scannedrecyclerview.setAdapter(adapter);
        binding.btcreate.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ShowconsigmentPopup();
            }
        });

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


        Log.d("subversions", "updateCount: " + systemLocationID);

        ReaderUtils.initSound(Transferactivity.this);


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
                            alert.showDialog(Transferactivity.this, "Connecting to " + macAddress);


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

            binding.BtInventory.setText("Stop");
            isScanning = true;
            //  setViewEnabled(false);
            new Transferactivity.BleTagThread().start();
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
                Toast.makeText(Transferactivity.this, "Initialization fail", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Transferactivity.this, "Initialization Success", Toast.LENGTH_SHORT).show();
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


            new Transferactivity.TagThread().start();
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
                if (adapter.getItemCount() == 0) {
                    binding.btcreate.setVisibility(View.GONE);
                } else {
                    binding.btcreate.setVisibility(View.VISIBLE);

                }
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
                if (adapter.getItemCount() == 0) {
                    binding.btcreate.setVisibility(View.GONE);
                } else {
                    binding.btcreate.setVisibility(View.VISIBLE);

                }
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
        if(isHexNumber(epc)){
            Log.d("addDataToList", "addDataToList: " + epc);
            String epcCode = HexToString(epc);

            if (epcCode.startsWith(getPrefix())&&epcCode.endsWith(getSuffix())) {
                epcCode = epcCode.substring(getPrefix().length(), epcCode.lastIndexOf(getSuffix()));
                tagList.add(epcCode);

                //   Log.d("addDataToList", "addDataToList: " + HexToString(epc));
                adapter.notifyDataSetChanged();
                ReaderUtils.playSound(1);
            }else{
                Log.d("invalid", "addDataToList: ");
            }
        }

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

    public class Adapter extends RecyclerView.Adapter<Transferactivity.Adapter.ViewHolder> {
        private ArrayList<HashMap<String, String>> aryList;
        private Context context;

        private int lastSelectedPosition = -1;
        private int lastsel = 0;

        public Adapter(Context context) {
            this.context = context;


        }

        /* public FirstAdapter(Context context,String rfid) {
             this.mRfid = rfid;
             this.context = context;
         }*/
        @Override
        public Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_consig_card, viewGroup, false);
            return new Transferactivity.Adapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(Adapter.ViewHolder viewHolder, int position) {
            viewHolder.textViewSerialnos.setText(tagList.get(position));


        }

        @Override
        public int getItemCount() {


            return tagList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView textView_item_code, textViewitemName, textViewWeight, textViewPrice, textViewStoneweight, textViewSerialnos, tv_description;

            ImageView imageviewdelete;

            public ViewHolder(View view) {
                super(view);


                textViewSerialnos = itemView.findViewById(R.id.tv_serialnumber);
                imageviewdelete = itemView.findViewById(R.id.imvdelete);

                imageviewdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getBindingAdapterPosition();
                        tagList.remove(tagList.get(pos));
                        notifyDataSetChanged();
                        // deleteProduct.removefromcart(String.valueOf(dataset.get(pos).getItemID()), dataset.get(pos).getSerialNumber());

                    }
                });


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAbsoluteAdapterPosition();
                        lastSelectedPosition = getAbsoluteAdapterPosition();
                        lastsel = getAbsoluteAdapterPosition();
                        // dataset.get(position).setSelected(!dataset.get(position).isSelected());

                        // fragmenttransition.replaceFragment(dataset.get(getAdapterPosition()).getId());
                        //  binding.tvusername.setTextColor(dataset.get(position).isSelected() ? Color.parseColor("#0276FF") : Color.parseColor("#000000"));
                        notifyDataSetChanged();


                    }
                });


            }
        }
    }

    public void ShowconsigmentPopup() {
        FromstoreId = 0;
        TostoreId = 0;
        showProgressDialog();
        storesdatset = new ArrayList<>();
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_transfer_dialog, null);
        final EditText edRemarks = alertLayout.findViewById(R.id.edremarks);
        SearchableSpinners spinners = alertLayout.findViewById(R.id.spin_branches);
        SearchableSpinners tospinner = alertLayout.findViewById(R.id.spin_to_branches);

        RetrofitInstance.getApiService(getApplicationContext()).FetchLiveStore(LocalPreferences.getToken(getApplicationContext())).enqueue(new Callback<List<ResponseLiveStore>>() {
            @Override
            public void onResponse(Call<List<ResponseLiveStore>> call, Response<List<ResponseLiveStore>> response) {
                hideProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    List<ResponseLiveStore> stores = response.body();
                    storesdatset = stores;
                    ArrayAdapter<ResponseLiveStore> arrayAdapter = new ArrayAdapter<ResponseLiveStore>(getApplicationContext(), android.R.layout.simple_spinner_item, stores);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinners.setAdapter(arrayAdapter);
                    ArrayAdapter<ResponseLiveStore> arrayAdapter2 = new ArrayAdapter<ResponseLiveStore>(getApplicationContext(), android.R.layout.simple_spinner_item, stores);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    tospinner.setAdapter(arrayAdapter2);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseLiveStore>> call, Throwable t) {
                hideProgressDialog();

            }
        });
        spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                FromstoreId = storesdatset.get(i).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tospinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TostoreId = storesdatset.get(i).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        AlertDialog.Builder alert = new AlertDialog.Builder(Transferactivity.this);
        alert.setTitle("Create Transfer");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show());

        alert.setPositiveButton("Proceed", (dialog, which) -> {
            String remarks = edRemarks.getText().toString();
            if (FromstoreId == 0) {
                showerror("Please select a store");
            } else if (TostoreId == 0) {
                showerror("Please select a store");
            } else if (FromstoreId == TostoreId) {
                showerror("From store and To store cannot be same");
            } else {
                JsonObject body = new JsonObject();
                JsonArray jsonArray = new JsonArray();
                body.addProperty("fromWarehouseID", FromstoreId);
                body.addProperty("toWarehouseID", TostoreId);
                body.addProperty("remarks", remarks);
                JsonObject serial;
                for (int i = 0; i < tagList.size(); i++) {
                    serial = new JsonObject();
                    serial.addProperty("serialNumber", tagList.get(i));
                    serial.addProperty("orderQty", 1);
                    serial.addProperty("itemID", 0);
                    jsonArray.add(serial);
                }
                body.add("transferOrderDetailDTOs", jsonArray);
                createTransfer(body);
                dialog.dismiss();
            }

        });
        AlertDialog dialog = alert.create();
        dialog.show();


    }

    private void createTransfer(JsonObject body) {
        showProgressDialog();
        RetrofitInstance.getApiService(getApplicationContext()).createTransfer(LocalPreferences.getToken(getApplicationContext()), body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    showsuccess("Created Successfully");

                } else {
                    showerror("Failed");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideProgressDialog();
                showerror("Failed");

            }
        });
    }

}