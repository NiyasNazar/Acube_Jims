package com.rfid.serialport.listener;

/**
 * 串口连接异常状态
 * Created by Administrator on 2018/4/19.
 */
public enum Status {

    /**
     * 没有读写串口的权限
     */
    NO_READ_WRITE_PERMISSION,

    /**
     * 打开串口失败
     */
    OPEN_FAIL
}
