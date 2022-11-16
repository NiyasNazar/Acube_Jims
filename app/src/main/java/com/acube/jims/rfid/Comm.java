package com.acube.jims.rfid;

import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ThinKPad on 2017/5/15.
 */

public class Comm {
    //region power
   public String COM = "";
    long exittime;
    public DeviceType dt;
    public static final String CTRL_FILE = "/sys/devices/platform/psam_dev/psam_state";
    public static final String CTRL_FILE_G3G = "/sys/devices/platform/psam/psam_state";

    private void writeToCtrlFile(String data) {
        try {
            if (dt == DeviceType.supion_S_3) {
                FileOutputStream fps = new FileOutputStream(new File(CTRL_FILE_G3G));
                fps.write(data.getBytes());
                fps.close();
            } else {
                FileOutputStream fps = new FileOutputStream(new File(CTRL_FILE));
                fps.write(data.getBytes());
                fps.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private final String UART_SWITCH_PATH = "/sys/devices/soc.0/78b0000.serial/uart_switch";

    private void switchSerialPin(String whichPin) {
        Log.e("serialport", "write uart switch");
        File f = new File(UART_SWITCH_PATH);
        Log.e("serialport", UART_SWITCH_PATH + " Read:" + f.canRead()
                + " Write:" + f.canWrite());
        try {
            FileOutputStream out = new FileOutputStream(f);
            out.write(whichPin.getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("serialport", "File write failed: " + e.toString());
        }
    }

    enum DeviceType {
        supion_S_3,
        supion_S_4,
        supion_M_3,
        supion_M_4;
    }

    public void checkDevice() {
        String strM = Build.MODEL;
        int SDK = Build.VERSION.SDK_INT;
        if (strM.equals("SHT3X")) {
            dt = DeviceType.supion_M_3;
            COM = "dev/ttyMT2";// MTK 3G
        } else if (strM.equals("SHT3X-4G")) {
            dt = DeviceType.supion_M_4;
            COM = "dev/ttyMT3";// MTK 4G
        } else if (SDK == 18) {
            dt = DeviceType.supion_S_3;
            COM = "/dev/ttyHSL2";//3G
        } else if (SDK == 22) {
            dt = DeviceType.supion_S_4;
            COM = "/dev/ttyHSL0";//4G
        }
    }

    public boolean powerUp() {
        try {
            if (dt == DeviceType.supion_S_3 || dt == DeviceType.supion_M_3 || dt == DeviceType.supion_M_4)
                writeToCtrlFile("2");
            else if (dt == DeviceType.supion_S_4)
                switchSerialPin(new String("uart3"));//上电
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean powerDown() {
        try {
            if (dt == DeviceType.supion_S_3 || dt == DeviceType.supion_M_3 || dt == DeviceType.supion_M_4)
                writeToCtrlFile("3");
            else if (dt == DeviceType.supion_S_4) {
                switchSerialPin(new String("disable"));//下电
                switchSerialPin(new String("uart2"));//切换复用
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //endregion

    //region 类型转换
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes, int starPos, int endPos) {
        int size = endPos - starPos;
        char[] hexChars = new char[size * 2];
        for (int j = 0; j < size; j++) {
            int v = bytes[j + starPos] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte CalculateLRC(byte[] bytes, int size) {
        byte lrc = 0;
        for (int i = 0; i < size; i++) {
            lrc ^= bytes[i];
        }
        return lrc;
    }

    public static byte[] HexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
    //endregion

    enum operateType {
        nullOperate,
        getModoulVer,
        getCardVer,
        startScan,
        stopScan,
        sendComd
    }
}
