package com.rfid.serialport;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by Administrator on 2018/4/19.
 */

public class SerialPort {

    private static final String TAG = SerialPort.class.getSimpleName();

    /**
     * 文件设置最高权限 777 可读 可写 可执行
     *
     * @param file 文件
     * @return 权限修改是否成功
     */
    boolean chmod777(File file) {
        if (file == null || !file.exists()) {
            return false;
        }

        try {
            File device = new File("/dev/ttyHSL2");
            Process process1 = Runtime.getRuntime().exec("/system/xbin/su");
            DataOutputStream os = new DataOutputStream(process1.getOutputStream());
            os.writeBytes("chmod 0777 " + device.getAbsolutePath() + "\n");
            os.writeBytes("exit" + "\n");
            os.close();

            if ((process1.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                throw new SecurityException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SecurityException();
        }
        return false;
    }

    // JNI
    // 打开串口
    protected native static FileDescriptor open(String path, int baudrate, int flags);

    // 关闭串口
    protected native void close();

    static {
        System.loadLibrary("SerialPort");
    }
}
