package com.rfid.serialport;

import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */

public class SerialPortFinder {

    private static final String TAG = SerialPortFinder.class.getSimpleName();
    private static final String DRIVERS_PATH = "/proc/tty/drivers";
    private static final String SERIAL_FIELD = "serial";

    /**
     * 获取Drivers
     * @return
     * @throws IOException
     */
    public List<Driver> getDrivers() throws IOException {
        List<Driver> drivers = new ArrayList<>();

        LineNumberReader r = new LineNumberReader(new FileReader(DRIVERS_PATH));
        String line;
        while ((line = r.readLine()) != null) {
            String driverName = line.substring(0, 0x15).trim();
            String[] fields = line.split(" +");
            if (fields.length >= 5 && fields[fields.length - 1].equals(SERIAL_FIELD)) {
                Log.d(TAG, "Found new driver " + driverName + " on " + fields[fields.length - 4]);
                drivers.add(new Driver(driverName, fields[fields.length - 4]));
            }
        }

        return drivers;
    }

    /**
     * 获取Devices
     * @return
     */
    public List<Device> getDevices() {
        List<Device> devices = new ArrayList<>();

        try {
            List<Driver> drivers = getDrivers();
            for (Driver driver : drivers) {
                String driverName = driver.getDriverName();
                List<File> driverDevices = driver.getDevices();
                for (File file : driverDevices) {
                    String deviceName = file.getName();
                    devices.add(new Device(deviceName, driverName, file));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return devices;
    }
}
