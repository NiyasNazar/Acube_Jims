package com.rfid.serialport.listener;

/**
 * Created by Administrator on 2018/4/20.
 */

public interface OnSerialPortDataReceivedListener {

    /**
     * 当串口接收到消息时触发
     * @param data 接受到的字节数组
     */
    void onDataReceived(byte[] data);
}
