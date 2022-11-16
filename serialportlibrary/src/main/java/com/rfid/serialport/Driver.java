package com.rfid.serialport;

import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */

public class Driver {

    private static final String TAG = Driver.class.getSimpleName();

    private String driverName;
    private String deviceRoot;

    public Driver(String name, String root) {
        this.driverName = name;
        this.deviceRoot = root;
    }

    public String getDriverName() {
        return driverName;
    }

    public List<File> getDevices() {
        ArrayList<File> devices = new ArrayList<>();
        File dev = new File("/dev");

        if (!dev.exists()) {
            Log.i(TAG, "getDevices" + dev.getAbsolutePath() + " 不存在");
            return devices;
        }
        if (!dev.canRead()) {
            Log.i(TAG, "getDevices" + dev.getAbsolutePath() + " 没有读取权限");
            return devices;
        }

        devices.addAll(Arrays.asList(dev.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().startsWith(deviceRoot);
            }
        })));
        return devices;
    }
}
