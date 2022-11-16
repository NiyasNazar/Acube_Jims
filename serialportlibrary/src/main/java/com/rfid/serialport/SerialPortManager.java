package com.rfid.serialport;

import android.util.Log;

import com.rfid.serialport.listener.OnOpenSerialPortListener;
import com.rfid.serialport.listener.OnSerialPortDataReceivedListener;
import com.rfid.serialport.listener.Status;
import com.rfid.serialport.thread.SerialPortReadThread;
import com.rfid.serialport.utils.ByteArrayUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018/4/19.
 */

public class SerialPortManager extends SerialPort {

    private static final String TAG = SerialPort.class.getSimpleName();

    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private FileDescriptor mFd;
    private OnSerialPortDataReceivedListener onSerialPortDataReceivedListener;
    private OnOpenSerialPortListener onOpenSerialPortListener;

    private SerialPortReadThread serialPortReadThread;

    /**
     * 打开串口
     *
     * @param device   串口设备
     * @param baudRate 波特率
     * @return 打开是否成功
     */
    public boolean openSerialPort(File device, int baudRate) {
        Log.i(TAG, "openSerialPort: " + String.format("打开串口 %s  波特率 %s", device.getPath(), baudRate));

        // 检测串口权限
        if (!device.canWrite() || !device.canRead()) {
            boolean chmod777 = chmod777(device);
            if (!chmod777) {
                Log.i(TAG, "openSerialPort: 没有读写权限");
                if (onOpenSerialPortListener != null) {
                    onOpenSerialPortListener.onFail(device, Status.NO_READ_WRITE_PERMISSION);
                }
                return false;
            }
        }

        try {
            mFd = open(device.getAbsolutePath(), baudRate, 0);
            fileInputStream = new FileInputStream(mFd);
            fileOutputStream = new FileOutputStream(mFd);

            if (onOpenSerialPortListener != null) {
                onOpenSerialPortListener.onSuccessfulOpen(device);
            }

            startReadThread();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (onOpenSerialPortListener != null) {
                onOpenSerialPortListener.onFail(device, Status.OPEN_FAIL);
            }
        }
        return false;
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        if (mFd != null) {
            try {
                close();
            } catch (Exception e) {

            } finally {
                mFd = null;
            }
        }

        stopReadThread();

        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileInputStream = null;
        }

        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileOutputStream = null;
        }

        onOpenSerialPortListener = null;
        onSerialPortDataReceivedListener = null;
    }

    /**
     * 发送数据
     *
     * @param sendData
     */
    public boolean sendBytes(byte[] sendData) {
        Log.i(TAG, "before send: " + ByteArrayUtils.toHexString(sendData));
        if (mFd != null && fileOutputStream != null) {
            try {
                fileOutputStream.write(sendData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 开启接受消息线程
     */
    private void startReadThread() {
        serialPortReadThread = new SerialPortReadThread(fileInputStream) {
            @Override
            public void onDataReceived(byte[] data) {
                if (onSerialPortDataReceivedListener != null) {
                    onSerialPortDataReceivedListener.onDataReceived(data);
                }
            }
        };
        serialPortReadThread.start();
        Log.i(TAG, "read thread running...");
    }

    /**
     * 停止接受读线程
     */
    private void stopReadThread() {
        if (serialPortReadThread != null) {
            serialPortReadThread.stopAndRelease();
        }
    }

    /**
     * 设置串口状态改变监听器
     *
     * @param onOpenSerialPortListener
     */
    public void setOnOpenSerialPortListener(OnOpenSerialPortListener onOpenSerialPortListener) {
        this.onOpenSerialPortListener = onOpenSerialPortListener;
    }

    /**
     * 设置消息接受监听器
     *
     * @param onSerialPortDataReceivedListener
     */
    public void setOnSerialPortDataReceivedListener(OnSerialPortDataReceivedListener onSerialPortDataReceivedListener) {
        this.onSerialPortDataReceivedListener = onSerialPortDataReceivedListener;
    }
}
