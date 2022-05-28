package com.acube.jims.presentation.DeviceRegistration.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.acube.jims.BaseActivity;
import com.acube.jims.presentation.DeviceRegistration.ViewModel.AddDeviceViewModel;
import com.acube.jims.presentation.DeviceRegistration.ViewModel.DeviceRegistrationViewModel;
import com.acube.jims.presentation.DeviceRegistration.ViewModel.TrayMasterViewModel;
import com.acube.jims.presentation.DeviceRegistration.ViewModel.UpdateDeviceViewModel;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.DeviceRegistrationFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceRegistration;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceUpdation;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseGetRegistered;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseTrayMaster;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class DeviceRegistrationFragment extends BaseActivity {


    AddDeviceViewModel addDeviceViewModel;
    DeviceRegistrationViewModel mViewModel;
    TrayMasterViewModel trayMasterViewModel;
    UpdateDeviceViewModel updateDeviceViewModel;
    DeviceRegistrationFragmentBinding binding;
    int key = 0;
    Boolean isDeviceRegistered = false;
    String AuthToken;
    int vaTrayID = 0;
    String vaTrayMacAddress, vaTrayName;
    List<ResponseTrayMaster> datasettray;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.device_registration_fragment);
        initToolBar(binding.toolbarApp.toolbar, "Device Registration", true);


        initViewModels();
        String vaDeviceID = getDeviceUniqueID(DeviceRegistrationFragment.this);
        binding.edDeviceId.setText(vaDeviceID);



        AuthToken = LocalPreferences.retrieveStringPreferences(DeviceRegistrationFragment.this, AppConstants.Token);
        mViewModel.getLiveData().observe(DeviceRegistrationFragment.this, new Observer<ResponseGetRegistered>() {
            @Override
            public void onChanged(ResponseGetRegistered responseGetRegistered) {
                hideProgressDialog();
                if (responseGetRegistered != null) {
                    vaTrayMacAddress = responseGetRegistered.getMacAddress();

                    binding.edDeviceId.setText(responseGetRegistered.getDeviceID());
                    binding.edTrayname.setText(responseGetRegistered.getTrayName());
                    key = responseGetRegistered.getId();
                    isDeviceRegistered = true;

                }
            }
        });
        trayMasterViewModel.getLiveData().observe(DeviceRegistrationFragment.this, new Observer<List<ResponseTrayMaster>>() {
            @Override
            public void onChanged(List<ResponseTrayMaster> responseTrayMasters) {
                if (responseTrayMasters != null) {
                    datasettray = new ArrayList<>();
                    datasettray = responseTrayMasters;
                    ArrayAdapter<ResponseTrayMaster> arrayAdapter = new ArrayAdapter<ResponseTrayMaster>(DeviceRegistrationFragment.this, android.R.layout.simple_spinner_item, datasettray);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.edMacaddress.setAdapter(arrayAdapter);

                    //binding.edTrayname.setOnItemSelectedListener(DeviceRegistrationFragment.this);
                }
            }
        });

        binding.edMacaddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                vaTrayID = datasettray.get(position).getId();
                vaTrayMacAddress = datasettray.get(position).getTrayMacAddress();
                Log.d("onItemSelected", "onItemSelected: " + vaTrayMacAddress);
                LocalPreferences.storeStringPreference(DeviceRegistrationFragment.this, "TrayMacAddress", vaTrayMacAddress);
                vaTrayName = datasettray.get(position).getTrayName();
                binding.edTrayname.setText(vaTrayName);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addDeviceViewModel.getLiveData().observe(DeviceRegistrationFragment.this, new Observer<ResponseDeviceRegistration>() {
            @Override
            public void onChanged(ResponseDeviceRegistration responseDeviceRegistration) {
                hideProgressDialog();
                if (responseDeviceRegistration != null) {
                    customSnackBar(binding.parent, "Device registered successfully");
                    LocalPreferences.storeIntegerPreference(getApplicationContext(),"TrayID",responseDeviceRegistration.getTrayID());
                    LocalPreferences.storeIntegerPreference(getApplicationContext(),"DeviceID",responseDeviceRegistration.getId());


                }
            }
        });
        updateDeviceViewModel.getLiveData().observe(DeviceRegistrationFragment.this, new Observer<ResponseDeviceUpdation>() {
            @Override
            public void onChanged(ResponseDeviceUpdation responseDeviceUpdation) {
                hideProgressDialog();
                if (responseDeviceUpdation != null) {
                    LocalPreferences.storeIntegerPreference(getApplicationContext(),"TrayID",responseDeviceUpdation.getTrayID());
                    LocalPreferences.storeIntegerPreference(getApplicationContext(),"DeviceID",responseDeviceUpdation.getId());
                    customSnackBar(binding.parent, "Device updated successfully");
                }
            }
        });

        trayMasterViewModel.AddDeviceRegistrationDetails(LocalPreferences.getToken(DeviceRegistrationFragment.this));

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String vaDeviceID = getDeviceUniqueID(DeviceRegistrationFragment.this);
                if (vaDeviceID.equalsIgnoreCase("")) {


                } else if (vaDeviceID.equalsIgnoreCase("")) {
                    binding.edDeviceId.setError("Field Empty");
                } else if (isDeviceRegistered) {
                    doUpdateDeviceRegistration(vaTrayMacAddress, vaTrayID, vaDeviceID);
                    Log.d("tray", "onClick: registered");

                } else if (!isDeviceRegistered) {
                    Log.d("tray", "onClick: notregistered");
                    doAddDeviceRegistrationDetails(vaTrayMacAddress, vaTrayID, vaDeviceID);
                }
            }
        });


        mViewModel.GetDeviceRegistrationDetails(AppConstants.Authorization + AuthToken, vaDeviceID);
    }

    private void initViewModels() {
        mViewModel = new ViewModelProvider(this).get(DeviceRegistrationViewModel.class);
        addDeviceViewModel = new ViewModelProvider(this).get(AddDeviceViewModel.class);
        updateDeviceViewModel = new ViewModelProvider(this).get(UpdateDeviceViewModel.class);
        trayMasterViewModel = new ViewModelProvider(this).get(TrayMasterViewModel.class);
        trayMasterViewModel.init();

        mViewModel.init();
        addDeviceViewModel.init();
        updateDeviceViewModel.init();
    }

    private void doAddDeviceRegistrationDetails(String vaMacaddress, int trayID, String vaDeviceID) {
        showProgressDialog();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("deviceID", vaDeviceID);
        jsonObject.addProperty("macAddress", vaMacaddress);
        jsonObject.addProperty("trayID", trayID);
        addDeviceViewModel.AddDeviceRegistrationDetails(LocalPreferences.getToken(DeviceRegistrationFragment.this), jsonObject);
    }

    private void doUpdateDeviceRegistration(String vaMacaddress, int trayID, String vaDeviceID) {
        showProgressDialog();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", key);
        jsonObject.addProperty("deviceID", vaDeviceID);
        jsonObject.addProperty("macAddress", vaMacaddress);
        jsonObject.addProperty("trayID", trayID);
        updateDeviceViewModel.UpdateDeviceRegistrationDetails(LocalPreferences.getToken(DeviceRegistrationFragment.this), String.valueOf(key), jsonObject);
    }


    public String getDeviceUniqueID(Activity activity) {
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }
}

