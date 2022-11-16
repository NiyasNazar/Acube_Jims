package com.rfid.serialport.listener;

/**
 * Created by Administrator on 2018/4/20.
 */

public interface OnSerialPortDataSentListener {

    /**
     * 当串口发送消息时触发
     * @param data 发送的字节数组
     */
    void onDataSent(byte[] data);
}
