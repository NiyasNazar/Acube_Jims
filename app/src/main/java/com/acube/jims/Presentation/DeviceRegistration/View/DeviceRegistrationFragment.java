package com.acube.jims.Presentation.DeviceRegistration.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.R;
import com.acube.jims.databinding.DeviceRegistrationFragmentBinding;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class DeviceRegistrationFragment extends Fragment {

    private DeviceRegistrationViewModel mViewModel;
    DeviceRegistrationFragmentBinding binding;

    public static DeviceRegistrationFragment newInstance() {
        return new DeviceRegistrationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(
                inflater, R.layout.device_registration_fragment, container, false);
        View view = binding.getRoot();
        mViewModel = new ViewModelProvider(this).get(DeviceRegistrationViewModel.class);
        // TODO: Use the ViewModel
        /*WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();*/
        String macaddress = getMacAddr();
        binding.edMacaddress.setText(macaddress);
        Log.d("onCreateView", "onCreateView: " + macaddress);
        return view;
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

