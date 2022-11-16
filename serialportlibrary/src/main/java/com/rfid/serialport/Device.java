package com.rfid.serialport;

import java.io.File;

/**
 * Created by Administrator on 2018/4/19.
 */

public class Device {

    private static final String TAG = Device.class.getSimpleName();

    private String name;
    private String root;
    private File file;

    public Device(File file) {
        this.file = file;
        this.name = file.getAbsolutePath();
    }

    public Device(String name, String root, File file) {
        this.name = name;
        this.root = root;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFullName() {
        return this.file.getAbsolutePath();
    }
}
