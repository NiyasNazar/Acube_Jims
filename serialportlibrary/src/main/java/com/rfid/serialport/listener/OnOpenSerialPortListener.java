package com.rfid.serialport.listener;

import java.io.File;

/**
 * 打开串口监听器
 *
 * Created by Administrator on 2018/4/19.
 */

public interface OnOpenSerialPortListener {

    /**
     * 当成功打开串口时触发
     * @param device
     */
    void onSuccessfulOpen(File device);

    /**
     * 当串口打开失败时触发
     * @param device
     * @param status
     */
    void onFail(File device, Status status);
}
