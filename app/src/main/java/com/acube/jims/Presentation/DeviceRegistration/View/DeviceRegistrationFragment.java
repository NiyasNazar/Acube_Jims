package com.acube.jims.Presentation.DeviceRegistration.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.DeviceRegistration.ViewModel.AddDeviceViewModel;
import com.acube.jims.Presentation.DeviceRegistration.ViewModel.DeviceRegistrationViewModel;
import com.acube.jims.Presentation.DeviceRegistration.ViewModel.UpdateDeviceViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.DeviceRegistrationFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceRegistration;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceUpdation;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseGetRegistered;
import com.google.gson.JsonObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class DeviceRegistrationFragment extends BaseFragment {

    AddDeviceViewModel addDeviceViewModel;
    DeviceRegistrationViewModel mViewModel;
    UpdateDeviceViewModel updateDeviceViewModel;
    DeviceRegistrationFragmentBinding binding;
    int key = 0;
    Boolean isDeviceRegistered = false;
    String AuthToken;

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

        AuthToken = LocalPreferences.retrieveStringPreferences(getActivity(), "Token");
        mViewModel.getLiveData().observe(getActivity(), new Observer<ResponseGetRegistered>() {
            @Override
            public void onChanged(ResponseGetRegistered responseGetRegistered) {
                hideProgressDialog();
                if (responseGetRegistered != null) {
                    binding.edMacaddress.setText(responseGetRegistered.getMacAddress());
                    binding.edDeviceId.setText(responseGetRegistered.getDeviceID());
                    binding.edTrayname.setText(responseGetRegistered.getTrayName());
                    key = responseGetRegistered.getId();
                    isDeviceRegistered = true;

                }
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

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vaMacaddress = getMacAddr();
                String vaTrayname = binding.edTrayname.getText().toString();
                String vaDeviceID = binding.edDeviceId.getText().toString();
                if (vaTrayname.equalsIgnoreCase("")) {
                    binding.edTrayname.setError("Field Empty");

                } else if (vaDeviceID.equalsIgnoreCase("")) {
                    binding.edDeviceId.setError("Field Empty");
                } else if (isDeviceRegistered) {
                    doUpdateDeviceRegistration(vaMacaddress, vaTrayname, vaDeviceID);

                } else if (!isDeviceRegistered) {
                    doAddDeviceRegistrationDetails(vaMacaddress, vaTrayname, vaDeviceID);
                }
            }
        });
        /*WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();*/
        String macaddress = getMacAddr();
        binding.edMacaddress.setText(macaddress);
        Log.d("onCreateView", "onCreateView: " + macaddress);
        mViewModel.GetDeviceRegistrationDetails(AppConstants.Authorization + AuthToken, macaddress);
        return view;
    }

    private void initViewModels() {
        mViewModel = new ViewModelProvider(this).get(DeviceRegistrationViewModel.class);
        addDeviceViewModel = new ViewModelProvider(this).get(AddDeviceViewModel.class);
        updateDeviceViewModel = new ViewModelProvider(this).get(UpdateDeviceViewModel.class);

        mViewModel.init();
        addDeviceViewModel.init();
        updateDeviceViewModel.init();
    }

    private void doAddDeviceRegistrationDetails(String vaMacaddress, String vaTrayname, String vaDeviceID) {
        showProgressDialog();
        String AuthToken = LocalPreferences.retrieveStringPreferences(getActivity(), "Token");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("deviceID", vaDeviceID);
        jsonObject.addProperty("macAddress", vaMacaddress);
        jsonObject.addProperty("trayName", vaTrayname);
        addDeviceViewModel.AddDeviceRegistrationDetails(AppConstants.Authorization + AuthToken, jsonObject);
    }

    private void doUpdateDeviceRegistration(String vaMacaddress, String vaTrayname, String vaDeviceID) {
        showProgressDialog();
        String AuthToken = LocalPreferences.retrieveStringPreferences(getActivity(), "Token");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", key);
        jsonObject.addProperty("deviceID", vaDeviceID);
        jsonObject.addProperty("macAddress", vaMacaddress);
        jsonObject.addProperty("trayName", vaTrayname);
        updateDeviceViewModel.UpdateDeviceRegistrationDetails(AppConstants.Authorization + AuthToken, String.valueOf(key), jsonObject);
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }
}

