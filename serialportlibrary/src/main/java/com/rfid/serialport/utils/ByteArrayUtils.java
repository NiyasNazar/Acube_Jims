package com.rfid.serialport.utils;

public class ByteArrayUtils {

    public static String toHexString(byte[] source) {
        if (source == null || source.length == 0) {
            throw new IllegalArgumentException();
        }
        
        StringBuffer stringBuffer = new StringBuffer();
        for (byte item : source) {
            String temp = Integer.toHexString(item & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            stringBuffer.append(temp);
            stringBuffer.append(" ");
        }
        
        return stringBuffer.substring(0, stringBuffer.length() - 1).toString().toUpperCase();
    }

    public static byte[] toBytes(String hexStr) {
        if (hexStr == null || hexStr.length() == 0) {
            throw new IllegalArgumentException();
        }

        String[] hexStrArray = hexStr.split(" +");
        byte[] result = new byte[hexStrArray.length];
        for (int i = 0; i < result.length; i++) {
            int val = Integer.valueOf(hexStrArray[i], 16);
            result[i] = (byte) val;
        }

        return result;
    }

    public static boolean isAllEquals(byte[] source, byte equals) {
        if (source == null || source.length == 0) {
            return false;
        }
        for (byte item : source) {
            if (item != equals) {
                return false;
            }
        }
        return true;
    }
}
