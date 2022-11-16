package com.rfid.serialport.thread;

import android.util.Log;

import com.rfid.serialport.utils.ByteArrayUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2018/4/19.
 */

public abstract class SerialPortReadThread extends Thread {

    private static final String TAG = SerialPortReadThread.class.getSimpleName();
    private static final int BUFFER_SIZE = 1024;

    private InputStream inputStream;
    private byte[] readBuffer;

    public SerialPortReadThread(InputStream inputStream) {
        this.inputStream = inputStream;
        this.readBuffer = new byte[BUFFER_SIZE];
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                if (inputStream == null) {
                    return;
                }

                Log.d(TAG, "before reading...");
                int size = inputStream.read(readBuffer);
                if (size <= 0) {
                    continue;
                }

                byte[] readBytes = new byte[size];
                System.arraycopy(readBuffer, 0, readBytes, 0, size);
                Log.i(TAG, "run: readBytes = " + ByteArrayUtils.toHexString(readBytes));
                onDataReceived(readBytes);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * 关闭读线程并释放资源
     */
    public void stopAndRelease() {
        this.interrupt();

        if (inputStream != null) {
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public abstract void onDataReceived(byte[] data);
}
