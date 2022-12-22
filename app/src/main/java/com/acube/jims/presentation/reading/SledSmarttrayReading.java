package com.acube.jims.presentation.reading;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityFindmissingReadingBinding;
import com.acube.jims.databinding.ActivitySledtrayReadingBinding;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogDetails;
import com.acube.jims.datalayer.models.SelectionHolder;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.datalayer.remote.dbmodel.Smarttool;
import com.acube.jims.presentation.CartManagment.ViewModel.AddtoCartViewModel;
import com.acube.jims.presentation.Catalogue.View.CatalogueActivity;
import com.acube.jims.presentation.Compare.CompareFragment;
import com.acube.jims.presentation.DeviceRegistration.View.DeviceRegistrationFragment;
import com.acube.jims.presentation.Favorites.ViewModel.AddtoFavoritesViewModel;
import com.acube.jims.presentation.ImageGallery.ImageZoomerActivity;
import com.acube.jims.presentation.PdfGeneration.ShareScannedItems;
import com.acube.jims.presentation.Quotation.InvoiceFragment;
import com.acube.jims.presentation.Quotation.SaleFragment;
import com.acube.jims.presentation.ReadingRangeSettings;
import com.acube.jims.presentation.ScanItems.ResponseItems;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.utils.OnSingleClickListener;
import com.acube.jims.utils.ReaderUtils;
import com.acube.jims.utils.Utils;
import com.acube.jims.utils.ViewDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.ConnectionStatus;
import com.rscja.deviceapi.interfaces.ConnectionStatusCallback;
import com.rscja.deviceapi.interfaces.ScanBTCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SledSmarttrayReading extends BaseActivity {
    ActivitySledtrayReadingBinding binding;
    public RFIDWithUHFUART mReader;
    AddtoCartViewModel addtoCartViewModel;

    public boolean isScanning = false;
    private HashMap<String, String> map;
    private ArrayList<Smarttool> tagList;
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
    boolean isSelectedAll = false;
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
    private FirstAdapter firstAdapter;
    String UserId;
    String CartId = "";
    int GuestCustomerID;
    float trayrange;
    AddtoFavoritesViewModel addtoFavoritesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sledtray_reading);

        initToolBar(binding.toolbarApp.toolbar, "Smart Scan", true);
        auditID = getIntent().getStringExtra("auditID");
        alert = new ViewDialog();
        username = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "EmployeeName");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        }
        GuestCustomerID = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        UserId = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.UserID);
        url = getIntent().getStringExtra("url");
        temDataSerials = new ArrayList<>();
        temDataScanned = new ArrayList<>();
        DeviceReg = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "DeviceReg");
        tagList = new ArrayList<>();
        systemLocationID = getIntent().getIntExtra("systemLocationID", 0);
        storeID = getIntent().getIntExtra("storeID", 0);
        categoryId = getIntent().getIntExtra("categoryId", 0);
        itemID = getIntent().getIntExtra("itemID", 0);
        handheld = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "handheld");
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        DeleteTemp();
        addtoFavoritesViewModel = new ViewModelProvider(this).get(AddtoFavoritesViewModel.class);
        addtoFavoritesViewModel.init();
        checkLocationEnable();
        uhf.init(getApplicationContext());
        binding.toolbarApp.imvsmarttool.setVisibility(View.VISIBLE);
        addtoCartViewModel = new ViewModelProvider(this).get(AddtoCartViewModel.class);
        addtoCartViewModel.init();
        addtoCartViewModel.getLiveData().observe(this, new Observer<ResponseAddtoCart>() {
            @Override
            public void onChanged(ResponseAddtoCart responseAddtoCart) {
                hideProgressDialog();
                if (responseAddtoCart != null) {
                    Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                    LocalPreferences.storeStringPreference(getApplicationContext(), AppConstants.CartID, responseAddtoCart.getCartListNo());
                }
            }
        });
        binding.toolbarApp.settings.setVisibility(View.VISIBLE);
        binding.toolbarApp.settings.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReadingRangeSettings.class));
            }
        });
        ReaderUtils.initSound(SledSmarttrayReading.this);
        binding.toolbarApp.imvsmarttool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    int count = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().scannedItemsDao().getallcount();
                    runOnUiThread(() -> {
                        if (count > 1) {
                            showPopupWindow(v);

                        } else {


                            showerror("Please select one or more item before proceeding.");
                        }

                    });
                }).start();

            }
        });
        binding.toolbarApp.BtInventory.setVisibility(View.VISIBLE);

        LinearLayoutManager firstManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        firstAdapter = new FirstAdapter(getApplicationContext());
        binding.LvTags.setAdapter(firstAdapter);
        binding.LvTags.setLayoutManager(firstManager);
        // firstManager.setStackFromEnd(true);
        binding.toolbarApp.BtInventory.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {


                if (DeviceReg) {
                    if (uhf.getConnectStatus() == ConnectionStatus.CONNECTED) {

                        readBlutoothTags();
                    } else {
                        alert.showDialog(SledSmarttrayReading.this, "Connecting to " + macAddress);


                        if (checkLocationEnable()) {
                            showBluetoothDevice(false);
                        }
                    }
                } else {
                    startActivity(new Intent(getApplicationContext(), DeviceRegistrationFragment.class));
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
        uhf.setPower((int) trayrange);


        if (uhf.startInventoryTag()) {
            dataset = new ArrayList<>();
            //  binding.tvbuttontext.setText("Stop");
            new BleTagThread().start();
            isScanning = true;
            //  setViewEnabled(false);

        } else {
            stopBLEInventory();
//
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
                Toast.makeText(SledSmarttrayReading.this, "Initialization fail", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SledSmarttrayReading.this, "Initialization Success", Toast.LENGTH_SHORT).show();
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

        mReader.setPower((int) 30.0);
        if (mReader.startInventoryTag()) {
            dataset = new ArrayList<>();

            // binding.tvbuttontext.setText("Stop");
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
                //    binding.tvbuttontext.setText("Start");
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
                //binding.tvbuttontext.setText("Start");
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
                uhftagInfo = uhf.inventorySingleTag();
                if (uhftagInfo != null) {
                    String epc = uhftagInfo.getEPC();
                    Log.d("MYRSSIVALUE", "run: " + uhftagInfo.getRssi());
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

            Log.d("addDataToList", "addDataToList: " + epcCode);
            checkValid(epcCode);

            if (tagList.size() == 1) {

            }




       /*     new Thread(() -> {
                int status = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().updateFindmissing(auditID, systemLocationID, categoryId, itemID, storeID, epcCode, date, username);
                Log.d("UpdateStatus", "addDataToList: " + status + epcCode);
                if (status == 1) {
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().updateSelectionHolder(epcCode);
                    ReaderUtils.playSound(1);

                }

            }).start();*/


        }
        firstAdapter.notifyDataSetChanged();

    }

    private void checkValid(String epcCode) {
        RetrofitInstance.getApiService(getApplicationContext()).getItemDetails(LocalPreferences.getToken(getApplicationContext()), epcCode).enqueue(new Callback<ResponseCatalogDetails>() {
            @Override
            public void onResponse(Call<ResponseCatalogDetails> call, Response<ResponseCatalogDetails> response) {
                if (response.body() != null && response.code() == 200) {
                    ResponseCatalogDetails responseCatalogDetails = response.body();
                    if (responseCatalogDetails.getItemSubList() != null && responseCatalogDetails.getItemSubList().size() != 0) {
                        tagList.add(new Smarttool(String.valueOf(responseCatalogDetails.getId()), responseCatalogDetails.getSerialNumber(), responseCatalogDetails.getItemSubList().get(0).getImageFilePath()));

                    } else {
                        tagList.add(new Smarttool(String.valueOf(responseCatalogDetails.getId()), responseCatalogDetails.getSerialNumber(), ""));

                    }
                    Collections.sort(tagList);
                    firstAdapter.notifyDataSetChanged();

                }


            }


            @Override
            public void onFailure(Call<ResponseCatalogDetails> call, Throwable t) {

            }
        });
    }

    private void getItemDetails(String epcCode) {
        RetrofitInstance.getApiService(getApplicationContext()).getItemDetails(LocalPreferences.getToken(getApplicationContext()), epcCode).enqueue(new Callback<ResponseCatalogDetails>() {
            @Override
            public void onResponse(Call<ResponseCatalogDetails> call, Response<ResponseCatalogDetails> response) {
                if (response.body() != null && response.code() == 200) {
                  /*  Transition transition = new Slide(Gravity.BOTTOM);
                    transition.setDuration(600);


                    TransitionManager.beginDelayedTransition(binding.parent, transition);*/

                    Gson gson = new Gson();
                    String myJson = gson.toJson(response.body());
                    Log.d("TAGRESPONSE", "onResponse: " + myJson);


                    ResponseCatalogDetails responseCatalogDetails = response.body();
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_right)
                            .replace(R.id.container, SingleReadingView.newInstance(myJson))
                            .commit();
                  /*  ResponseCatalogDetails responseCatalogDetails = response.body();
                    binding.tvitemname.setText(responseCatalogDetails.getItemName());
                    binding.tvDescription.setText(responseCatalogDetails.getItemDesc());
                    binding.tvItemcode.setText(responseCatalogDetails.getSerialNumber());
                    binding.tvGender.setText(responseCatalogDetails.getGender());
                    binding.tvmakingchargemin.setText("" + getValueOrDefault(responseCatalogDetails.getMakingChargeMin(), ""));
                    binding.tvMakingchrgmax.setText("" + getValueOrDefault(responseCatalogDetails.getMakingChargeMax(), ""));
                    binding.tvKaratname.setText(getValueOrDefault(responseCatalogDetails.getKaratName(), ""));
                    binding.tvColor.setText(getValueOrDefault(responseCatalogDetails.getColorName(), ""));
                    binding.tvuom.setText(responseCatalogDetails.getUomName());
                    binding.tvgrossweight.setText("" + responseCatalogDetails.getGrossWeight() + " g");
                    binding.tvcategory.setText(responseCatalogDetails.getCategoryName());
                    binding.tvSubcategory.setText(responseCatalogDetails.getSubCategoryName());
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
                            .into(binding.glideimg);*/
                }


            }


            @Override
            public void onFailure(Call<ResponseCatalogDetails> call, Throwable t) {

            }
        });
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
                    Log.d("MYRSSI", "run: " + uhftagInfo.getRssi());


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
        trayrange = LocalPreferences.retrieveReadFloatPreferences(getApplicationContext(), "trayrange");

    }

    public class FirstAdapter extends RecyclerView.Adapter<FirstAdapter.ViewHolder> {
        private ArrayList<HashMap<String, String>> aryList;
        private Context context;

        private int lastSelectedPosition = -1;
        private int lastsel = 0;

        public FirstAdapter(Context context) {
            this.context = context;


        }

        /* public FirstAdapter(Context context,String rfid) {
             this.mRfid = rfid;
             this.context = context;
         }*/
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_second_card, viewGroup, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.textViewSerialnos.setText(tagList.get(position).getSerialNumber());
            Log.d("TAG", "onBindViewHolder: " + tagList.get(position).getImagepath());
            Glide.with(getApplicationContext())
                    .load(tagList.get(position).getImagepath())
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
                    .into(viewHolder.ItemImage);

            viewHolder.ItemImage.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    startActivity(new Intent(getApplicationContext(), ImageZoomerActivity.class).putExtra("imageResId", tagList.get(position).getImagepath()));

                }
            });
            if (lastSelectedPosition == position) {

            } else if (position == lastsel) {
                getItemDetails(tagList.get(position).getSerialNumber());
            } else {

            }


        }

        @Override
        public int getItemCount() {


            return tagList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView textView_item_code, textViewitemName, textViewWeight, textViewPrice, textViewStoneweight, textViewSerialnos, tv_description;
            ImageView imageViewadd, imageViewremove, ItemImage, imageviewdelete;
            CheckBox comparecheckbox;


            public ViewHolder(View view) {
                super(view);
                textViewitemName = itemView.findViewById(R.id.tv_itemname);
                textViewSerialnos = itemView.findViewById(R.id.tv_serialnumber);
                textView_item_code = itemView.findViewById(R.id.tv_item_code);
                textViewWeight = itemView.findViewById(R.id.tv_weight);
                textViewStoneweight = itemView.findViewById(R.id.tv_stoneweight);
                textViewPrice = itemView.findViewById(R.id.tvprice);
                imageviewdelete = itemView.findViewById(R.id.imvdelete);
                ItemImage = itemView.findViewById(R.id.item_image);
                comparecheckbox = itemView.findViewById(R.id.comparecheckbox);

                tv_description = itemView.findViewById(R.id.tv_description);

                comparecheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        new Thread(() -> {
                            Smarttool smarttool = new Smarttool(tagList.get(getAbsoluteAdapterPosition()).getID(), tagList.get(getAbsoluteAdapterPosition()).getSerialNumber(), "");

                            if (isChecked) {
                                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().scannedItemsDao().insertCheckboxSelection(smarttool);
                            } else {
                                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().scannedItemsDao().DeleteCheckboxSelection(smarttool);
                            }


                        }).start();
                    }
                });
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
                        getItemDetails(tagList.get(position).getSerialNumber());
                        // fragmenttransition.replaceFragment(dataset.get(getAdapterPosition()).getId());
                        //  binding.tvusername.setTextColor(dataset.get(position).isSelected() ? Color.parseColor("#0276FF") : Color.parseColor("#000000"));
                        notifyDataSetChanged();


                    }
                });


            }
        }
    }

    public void showPopupWindow(final View view) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.pop_up_layout_smarttool, null);
        CardView cdvfavorites = alertLayout.findViewById(R.id.cdvfavorites);
        CardView compare = alertLayout.findViewById(R.id.cdvcompare);
        CardView cdvshare = alertLayout.findViewById(R.id.cdvshare);
        CardView cdvsale = alertLayout.findViewById(R.id.cdvsale);
        CardView cdvquote = alertLayout.findViewById(R.id.cdvquote);
        CardView addtoCart = alertLayout.findViewById(R.id.cdvaddtocart);

        boolean salesman = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "salesman");


        //  final TextInputEditText etPassword = alertLayout.findViewById(R.id.tiet_password);


        AlertDialog.Builder alert = new AlertDialog.Builder(SledSmarttrayReading.this);
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    int count = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().scannedItemsDao().getallcount();
                    runOnUiThread(() -> {
                        if (count > 1) {
                            dialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), CompareFragment.class));

                        } else {

                            dialog.dismiss();
                            showerror("Please add more than one item for comparing.");
                        }

                    });
                }).start();


            }
        });
        cdvshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(), ShareScannedItems.class));
                dialog.dismiss();
            }
        });
        cdvfavorites.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!salesman) {
                    if (GuestCustomerID != 0) {
                        updateFavorites();
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        showerror("Favorites only available  on customer login");
                    }
                } else {
                    showerror("Favorites only available  on customer login");
                }


            }
        });


        addtoCart.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!salesman) {
                    if (GuestCustomerID != 0) {
                        updateCart();
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        showerror("Shopping cart only available  on customer login");
                    }
                } else {
                    showerror("Shopping cart only available  on customer login");
                }


            }
        });
        cdvsale.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(getApplicationContext(), SaleFragment.class));
            }
        });
        cdvquote.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(getApplicationContext(), InvoiceFragment.class));
            }
        });

    }

    private void updateCart() {
        DatabaseClient.getInstance(SledSmarttrayReading.this).getAppDatabase().scannedItemsDao().getFromSmarttoolList().observe(this, new Observer<List<Smarttool>>() {
            @Override
            public void onChanged(List<Smarttool> strings) {
                JsonArray jsonArray = new JsonArray();
                for (int i = 0; i < strings.size(); i++) {

                    JsonObject items = new JsonObject();
                    items.addProperty("cartListNo", CartId);
                    items.addProperty("customerID", GuestCustomerID);
                    items.addProperty("employeeID", UserId);
                    items.addProperty("serialNumber", strings.get(i).getSerialNumber());
                    items.addProperty("itemID", strings.get(i).getID());
                    items.addProperty("qty", 0);
                    jsonArray.add(items);
                }
                addtoCartViewModel.AddtoCart(LocalPreferences.getToken(getApplicationContext()), "add", jsonArray, getApplicationContext());
            }
        });


    }

    private void updateFavorites() {
        DatabaseClient.getInstance(SledSmarttrayReading.this).getAppDatabase().scannedItemsDao().getFromSmarttoolList().observe(this, new Observer<List<Smarttool>>() {
            @Override
            public void onChanged(List<Smarttool> strings) {
                JsonArray jsonArray = new JsonArray();
                for (int i = 0; i < strings.size(); i++) {
                    addtoFavoritesViewModel.AddtoFavorites(LocalPreferences.getToken(getApplicationContext()), String.valueOf(GuestCustomerID), UserId, strings.get(i).getID(), "add", "", strings.get(i).getSerialNumber(), getApplicationContext());


                }
            }
        });
    }

}