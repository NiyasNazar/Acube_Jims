package com.acube.jims.Presentation.DeviceRegistration.View;

import androidx.activity.OnBackPressedCallback;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.DeviceRegistration.ViewModel.AddDeviceViewModel;
import com.acube.jims.Presentation.DeviceRegistration.ViewModel.DeviceRegistrationViewModel;
import com.acube.jims.Presentation.DeviceRegistration.ViewModel.TrayMasterViewModel;
import com.acube.jims.Presentation.DeviceRegistration.ViewModel.UpdateDeviceViewModel;
import com.acube.jims.Presentation.HomePage.View.HomeFragment;
import com.acube.jims.R;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.DeviceRegistrationFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceRegistration;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceUpdation;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseGetRegistered;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseTrayMaster;
import com.google.gson.JsonObject;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DeviceRegistrationFragment extends BaseFragment {


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

    public static DeviceRegistrationFragment newInstance() {
        return new DeviceRegistrationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(
                inflater, R.layout.device_registration_fragment, container, false);
        View view = binding.getRoot();
        initViewModels();
        binding.toolbar.tvFragname.setText("Device Settings");
        String vaDeviceID = getDeviceUniqueID(getActivity());
        binding.edDeviceId.setText(vaDeviceID);
        binding.toolbar.tvDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.replaceFragment(getActivity(), R.id.content, new HomeFragment());

            }
        });


        AuthToken = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token);
        mViewModel.getLiveData().observe(getActivity(), new Observer<ResponseGetRegistered>() {
            @Override
            public void onChanged(ResponseGetRegistered responseGetRegistered) {
                hideProgressDialog();
                if (responseGetRegistered != null) {

                    binding.edDeviceId.setText(responseGetRegistered.getDeviceID());
                    binding.edTrayname.setText(responseGetRegistered.getTrayName());
                    key = responseGetRegistered.getId();
                    isDeviceRegistered = true;

                }
            }
        });
        trayMasterViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseTrayMaster>>() {
            @Override
            public void onChanged(List<ResponseTrayMaster> responseTrayMasters) {
                if (responseTrayMasters != null) {
                    datasettray = new ArrayList<>();
                    datasettray = responseTrayMasters;
                    ArrayAdapter<ResponseTrayMaster> arrayAdapter = new ArrayAdapter<ResponseTrayMaster>(getActivity(), android.R.layout.simple_spinner_item, datasettray);
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
                Log.d("onItemSelected", "onItemSelected: "+vaTrayMacAddress);
                LocalPreferences.storeStringPreference(getActivity(),"TrayMacAddress",vaTrayMacAddress);
                vaTrayName = datasettray.get(position).getTrayName();
                binding.edTrayname.setText(vaTrayName);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addDeviceViewModel.getLiveData().observe(getActivity(), new Observer<ResponseDeviceRegistration>() {
            @Override
            public void onChanged(ResponseDeviceRegistration responseDeviceRegistration) {
                hideProgressDialog();
                if (responseDeviceRegistration != null) {
                    customSnackBar(binding.parent, "Device registered successfully");

                }
            }
        });
        updateDeviceViewModel.getLiveData().observe(getActivity(), new Observer<ResponseDeviceUpdation>() {
            @Override
            public void onChanged(ResponseDeviceUpdation responseDeviceUpdation) {
                hideProgressDialog();
                if (responseDeviceUpdation != null) {
                    customSnackBar(binding.parent, "Device updated successfully");
                }
            }
        });

        trayMasterViewModel.AddDeviceRegistrationDetails(LocalPreferences.getToken(getActivity()));

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String vaDeviceID = getDeviceUniqueID(getActivity());
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
        return view;
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
        addDeviceViewModel.AddDeviceRegistrationDetails(LocalPreferences.getToken(getActivity()), jsonObject);
    }

    private void doUpdateDeviceRegistration(String vaMacaddress, int trayID, String vaDeviceID) {
        showProgressDialog();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", key);
        jsonObject.addProperty("deviceID", vaDeviceID);
        jsonObject.addProperty("macAddress", vaMacaddress);
        jsonObject.addProperty("trayID", trayID);
        updateDeviceViewModel.UpdateDeviceRegistrationDetails(LocalPreferences.getToken(getActivity()), String.valueOf(key), jsonObject);
    }

    public static String getMacAddr() {
        try {
            DeviceAdminReceiver admin = new DeviceAdminReceiver();
            DevicePolicyManager devicepolicymanager = admin.getManager(newInstance().getActivity());
            ComponentName name1 = admin.getWho(newInstance().getActivity());
            if (devicepolicymanager.isAdminActive(name1)) {
                String mac_address = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    mac_address = devicepolicymanager.getWifiMacAddress(name1);
                }
                Log.e("macAddress", "" + mac_address);
            }

        } catch (Exception ex) {
            //handle exception
            Log.d("getMacAddr", "getMacAddr: " + ex.getMessage());
        }
        return "";
    }

    public String getDeviceUniqueID(Activity activity) {
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }
}

