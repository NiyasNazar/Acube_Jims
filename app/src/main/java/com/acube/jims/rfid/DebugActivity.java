package com.acube.jims.rfid;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.acube.jims.R;
import com.rfid.cmd.Commands;
import com.rfid.cmd.ConstCode;
import com.rfid.cmd.parser.PacketDetailParser;
import com.rfid.cmd.parser.PacketParser;
import com.rfid.cmd.parser.ParamHashMap;
import com.rfid.serialport.Device;
import com.rfid.serialport.SerialPortFinder;
import com.rfid.serialport.SerialPortManager;
import com.rfid.serialport.listener.OnOpenSerialPortListener;
import com.rfid.serialport.listener.Status;
import com.rfid.serialport.utils.ByteArrayUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DebugActivity extends AppCompatActivity
        implements OnOpenSerialPortListener, View.OnClickListener, PacketParser.OnPacketParseListener {
    private static final String TAG = DebugActivity.class.getSimpleName();

    private TextView tv_info;
    private TextView tv_print;
    private Button btn_setup;
    private Button btn_set_select;
    private Button btn_read_data;
    private Button btn_write_data;
    private Button btn_lock;
    private EditText et_loop_num;
    private EditText et_pa_power;
    private EditText et_pointer;
    private EditText et_mask;
    private EditText et_kill_password;
    private EditText et_access_password;
    private EditText et_ld;
    private EditText et_sa;
    private EditText et_dl;
    private EditText et_data;
    private EditText et_access_password2;
    private EditText et_channel_index;
    private AppCompatSpinner spn_region;
    private AppCompatSpinner spn_select_mode;
    private AppCompatSpinner spn_target;
    private AppCompatSpinner spn_action;
    private AppCompatSpinner spn_mem_bank;
    private AppCompatSpinner spn_men_bakk2;
    private AppCompatSpinner spn_dr;
    private AppCompatSpinner spn_m;
    private AppCompatSpinner spn_t_rext;
    private AppCompatSpinner spn_sel;
    private AppCompatSpinner spn_session;
    private AppCompatSpinner spn_target_q;
    private AppCompatSpinner spn_q;

    private SharedPreferences sp;
    private PacketParser packetParser = new PacketParser(this);
    private PacketDetailParser packetDetailParser = new PacketDetailParser();
    private SerialPortFinder serialPortFinder = new SerialPortFinder();
    private SerialPortManager serialPortManager;

    private PreSetSelectTask preSetSelectTask = null;

    private static Comm mcomm = new Comm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        et_loop_num = findViewById(R.id.et_loop_num);
        et_pa_power = findViewById(R.id.et_pa_power);
        et_pointer = findViewById(R.id.et_pointer);
        et_mask = findViewById(R.id.et_mask);
        et_kill_password = findViewById(R.id.et_kill_password);
        et_access_password = findViewById(R.id.et_access_password);
        et_ld = findViewById(R.id.et_ld);
        et_sa = findViewById(R.id.et_sa);
        et_dl = findViewById(R.id.et_dl);
        et_data = findViewById(R.id.et_data);
        et_access_password2 = findViewById(R.id.et_access_password2);
        et_channel_index = findViewById(R.id.et_channel_index);

        spn_region = findViewById(R.id.spn_region);
        spn_select_mode = findViewById(R.id.spn_select_mode);
        spn_target = findViewById(R.id.spn_target);
        spn_action = findViewById(R.id.spn_action);
        spn_mem_bank = findViewById(R.id.spn_mem_bank);
        spn_men_bakk2 = findViewById(R.id.spn_mem_bank2);
        spn_dr = findViewById(R.id.spn_dr);
        spn_m = findViewById(R.id.spn_m);
        spn_t_rext = findViewById(R.id.spn_t_rext);
        spn_sel = findViewById(R.id.spn_sel);
        spn_session = findViewById(R.id.spn_session);
        spn_target_q = findViewById(R.id.spn_target_q);
        spn_q = findViewById(R.id.spn_q);

        tv_info = findViewById(R.id.tv_info);
        tv_print = findViewById(R.id.tv_print);
        tv_print.setMovementMethod(ScrollingMovementMethod.getInstance());

        btn_setup = findViewById(R.id.btn_setup);
        btn_set_select = findViewById(R.id.btn_set_select);
        btn_read_data = findViewById(R.id.btn_read_data);
        btn_write_data = findViewById(R.id.btn_write_data);
        btn_lock = findViewById(R.id.btn_lock);
        btn_setup.setOnClickListener(this);
        autoSetOnClickListener(R.id.btn_clear);
        autoSetOnClickListener(R.id.btn_firmware_version);
        autoSetOnClickListener(R.id.btn_software_version);
        autoSetOnClickListener(R.id.btn_manufacturers_info);
        autoSetOnClickListener(R.id.btn_read_single);
        autoSetOnClickListener(R.id.btn_read_multi);
        autoSetOnClickListener(R.id.btn_stop_read);
        autoSetOnClickListener(R.id.btn_set_region);
        autoSetOnClickListener(R.id.btn_get_pa_power);
        autoSetOnClickListener(R.id.btn_set_pa_power);
        autoSetOnClickListener(R.id.btn_set_select_mode);
        autoSetOnClickListener(R.id.btn_set_select);
        autoSetOnClickListener(R.id.btn_kill);
        autoSetOnClickListener(R.id.btn_lock);
        autoSetOnClickListener(R.id.btn_read_data);
        autoSetOnClickListener(R.id.btn_write_data);
        autoSetOnClickListener(R.id.btn_set_channel);
        autoSetOnClickListener(R.id.btn_get_channel);
        autoSetOnClickListener(R.id.btn_set_q);
        autoSetOnClickListener(R.id.btn_get_q);
        autoSetOnClickListener(R.id.btn_set_fhss_auto);
        autoSetOnClickListener(R.id.btn_set_fhss_cancel);

        // 自动加载上次连接的串口
        sp = getSharedPreferences("sp_device", MODE_PRIVATE);
        String filePath = sp.getString("device", "");
        if (!filePath.isEmpty()) {
            File file = new File(filePath);
            if (file.exists()) {
                Toast.makeText(this, String.format("自动连接上次的串口 [%s]", filePath), Toast.LENGTH_SHORT).show();
                openSerialPort(new Device(file));
                return;
            }
        }

        mcomm.checkDevice();
        mcomm.powerUp();

        openSerialPort(new Device(new File(mcomm.COM)));
    }

    private void autoSetOnClickListener(int id) {
        findViewById(id).setOnClickListener(this);
    }

    /**
     * 通过串口发送数据包
     */
    private void sendPacket(byte[] sendPacket) {
        if (serialPortManager != null) {
            serialPortManager.sendBytes(sendPacket);
        }
    }

    /**
     * 往输出面板追加消息并自动滚到底部
     */
    private void appendPrintPanel(String msg) {
        tv_print.append(msg);
        int offset = tv_print.getLineCount() * tv_print.getLineHeight();
        if (offset > tv_print.getHeight()) {
            tv_print.scrollTo(0, offset - tv_print.getHeight());
        }
    }

    /**
     * 打开串口
     */
    private void openSerialPort(Device device) {
        File file = device.getFile();
        serialPortManager = new SerialPortManager();
        packetParser.reset();
        serialPortManager.setOnSerialPortDataReceivedListener(packetParser);
        serialPortManager.setOnOpenSerialPortListener(this);
        boolean result = serialPortManager.openSerialPort(new File("/dev/ttyHSL2"), 115200);
        if (result) {
//            sp.edit().putString("device", file.getAbsolutePath()).commit();
            tv_info.setText(String.format("成功打开串口 %s", device.getFullName()));
//            btn_setup.setText("关闭");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serialPortManager != null) {
            serialPortManager.closeSerialPort();
            serialPortManager = null;
        }

        mcomm.powerDown();
    }

    @Override
    public void onSuccessfulOpen(File device) {
        Toast.makeText(this, String.format("打开串口[ %s ]成功", device.getName()), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFail(File device, Status status) {
        switch (status) {
            case OPEN_FAIL:
                Toast.makeText(this, String.format("打开串口[ %s ]失败", device.getName()), Toast.LENGTH_LONG).show();
                break;
            case NO_READ_WRITE_PERMISSION:
                Toast.makeText(this, String.format("打开串口[ %s ]失败，权限不够", device.getName()), Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setup: // 设置串口
                if (serialPortManager == null) {
                    final List<Device> devices = serialPortFinder.getDevices();
                    String[] devicesStr = new String[devices.size()];
                    for (int i = 0; i < devicesStr.length; i++) {
                        devicesStr[i] =
                                String.format("%s (%s)", devices.get(i).getName(), devices.get(i).getFullName());
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setSingleChoiceItems(devicesStr, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Log.d(TAG, "Select SerialPort index:" + which);
                            Device handleDevice = devices.get(which);
                            openSerialPort(handleDevice);
                        }
                    });
                    builder.show();
                } else {
                    serialPortManager.closeSerialPort();
                    serialPortManager = null;
                    tv_info.setText("No serial Port");
                    btn_setup.setText("set up");
                }
                break;
            case R.id.btn_clear: // 清除控制台信息
                tv_print.setText("");
                tv_print.scrollTo(0, 0);
                break;
            case R.id.btn_firmware_version: /* 获取固件版本 */
                sendPacket(Commands.buildGetFirmwareVersionFrame());
                break;
            case R.id.btn_software_version: /* 获取软件版本 */
                sendPacket(Commands.buildGetSoftwareVersionFrame());
                break;
            case R.id.btn_manufacturers_info: /* 获取制造商信息 */
                sendPacket(Commands.buildGetManufacturersInfoFrame());
                break;
            case R.id.btn_read_single: /* 单读 */
                sendPacket(Commands.buildReadSingleFrame());
                break;
            case R.id.btn_read_multi: /* 多读 */
                try {
                    String loopStr = et_loop_num.getText().toString();
                    int loopNum = Integer.valueOf(loopStr);
                    sendPacket(Commands.buildReadMultiFrame(loopNum));
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "\n" +
                            "Please check if the number of polls is correct", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_stop_read: /* 停止多读 */
                sendPacket(Commands.buildStopReadFrame());
                break;
            case R.id.btn_set_region: /* 设置区域 */
                String regionStr = (String) spn_region.getSelectedItem();
                switch (regionStr) {
                    case "中国900MHz":
                        sendPacket(Commands.buildSetRegionFrame(ConstCode.FRAME_PARAM_REGION_CHINA_900_MHZ));
                        break;
                    case "中国800MHz":
                        sendPacket(Commands.buildSetRegionFrame(ConstCode.FRAME_PARAM_REGION_CHINA_800_MHZ));
                        break;
                    case "美国":
                        sendPacket(Commands.buildSetRegionFrame(ConstCode.FRAME_PARAM_REGION_USA));
                        break;
                    case "欧洲":
                        sendPacket(Commands.buildSetRegionFrame(ConstCode.FRAME_PARAM_REGION_EUROPE));
                        break;
                    case "韩国":
                        sendPacket(Commands.buildSetRegionFrame(ConstCode.FRAME_PARAM_REGION_KOREA));
                        break;
                }
                break;
            case R.id.btn_get_pa_power: /* 获取发射功率 */
                sendPacket(Commands.buildGetPaPowerFrame());
                break;
            case R.id.btn_set_pa_power:
                try {
                    String powerStr = et_pa_power.getText().toString();
                    int power = Integer.valueOf(powerStr);
                    sendPacket(Commands.buildSetPaPowerFrame(power));
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "请检查发射功率值是否正确", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_set_select_mode: /* 设置SELECT MODE */
                byte selectMode = 0x00;
                switch ((String)spn_select_mode.getSelectedItem()) {
                    case "0x00(选定)":
                        selectMode = 0x00;
                        break;
                    case "0x01(不选定)":
                        selectMode = 0x01;
                        break;
                    case "0x02(盘存之外)":
                        selectMode = 0x02;
                        break;
                }
                sendPacket(Commands.buildSetSelectModelFrame(selectMode));
                break;
            case R.id.btn_set_select: /* 设置SELECT */
                byte targetByte = 0x00;
                switch ((String) spn_target.getSelectedItem()) {
                    case "S0(000)":
                        targetByte = 0x00;
                        break;
                    case "S1(001)":
                        targetByte = 0x01;
                        break;
                    case "S2(010)":
                        targetByte = 0x0A;
                        break;
                    case "S3(011)":
                        targetByte = 0x0B;
                        break;
                    case "SL(100)":
                        targetByte = 0x64;
                        break;
                    case "RFU(101)":
                        targetByte = 0x65;
                        break;
                    case "RFU(110)":
                        targetByte = 0x6E;
                        break;
                    case "RFU(111)":
                        targetByte = 0x6F;
                        break;
                }
                byte actionByte = 0x00;
                switch ((String) spn_action.getSelectedItem()) {
                    case "000":
                        actionByte = 0x00;
                        break;
                    case "001":
                        actionByte = 0x01;
                        break;
                    case "010":
                        actionByte = 0x0A;
                        break;
                    case "011":
                        actionByte = 0x0B;
                        break;
                    case "100":
                        actionByte = 0x64;
                        break;
                    case "101":
                        actionByte = 0x65;
                        break;
                    case "110":
                        actionByte = 0x6E;
                        break;
                    case "111":
                        actionByte = 0x6F;
                        break;
                }
                byte memBankByte = 0x00;
                switch ((String) spn_mem_bank.getSelectedItem()) {
                    case "RFU":
                        memBankByte = ConstCode.FRAME_PARAM_MEM_BANK_RFU;
                        break;
                    case "EPC":
                        memBankByte = ConstCode.FRAME_PARAM_MEM_BANK_EPC;
                        break;
                    case "TID":
                        memBankByte = ConstCode.FRAME_PARAM_MEM_BANK_TID;
                        break;
                    case "User":
                        memBankByte = ConstCode.FRAME_PARAM_MEM_BANK_USER;
                        break;
                }
                try {
                    String pointerHexStr = et_pointer.getText().toString().replace(" ", "");
                    long pointerVal = Long.valueOf(pointerHexStr, 16);
                    byte[] maskBytes = ByteArrayUtils.toBytes(et_mask.getText().toString());

                    sendPacket(Commands.buildSetSelectFrame(targetByte, actionByte, memBankByte, pointerVal, false, maskBytes));

                } catch (NumberFormatException e) {
                    Toast.makeText(this, "十六进制输入错误", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_kill: /* 灭活 */
                try {
                    byte[] killPwd = ByteArrayUtils.toBytes(et_kill_password.getText().toString());
                    sendPacket(Commands.buildKillFrame(killPwd));
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "十六进制输入错误", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_lock: /* Lock */
                try {
                    if (this.checkPreSelect() && preSetSelectTask == null) {
                        preSetSelectTask = new PreSetSelectTask(TaskType.LOCK);
                        preSetSelectTask.start();
                        return;
                    }

                    preSetSelectTask = null;

                    byte[] accessPwd = ByteArrayUtils.toBytes(et_access_password.getText().toString());
                    String dlHexStr = et_ld.getText().toString().replace(" ", "");
                    int ld = Integer.valueOf(dlHexStr, 16);
                    sendPacket(Commands.buildLockFrame(accessPwd, ld));
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "十六进制输入错误", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_read_data: /* 读取数据 */
            case R.id.btn_write_data: /* 写入数据 */
                try {
                    // 如果满足"前置Select"条件（Mask都不为零），并且当前没有运行任务
                    if (this.checkPreSelect() && preSetSelectTask == null) {
                        preSetSelectTask = new PreSetSelectTask(v.getId() == R.id.btn_read_data ? TaskType.READ_DATA : TaskType.WRITE_DATA);
                        preSetSelectTask.start();
                        return;
                    }

                    preSetSelectTask = null;

                    byte memBankByte2 = 0;
                    switch ((String) spn_men_bakk2.getSelectedItem()) {
                        case "RFU":
                            memBankByte2 = ConstCode.FRAME_PARAM_MEM_BANK_RFU;
                            break;
                        case "EPC":
                            memBankByte2 = ConstCode.FRAME_PARAM_MEM_BANK_EPC;
                            break;
                        case "TID":
                            memBankByte2 = ConstCode.FRAME_PARAM_MEM_BANK_TID;
                            break;
                        case "User":
                            memBankByte2 = ConstCode.FRAME_PARAM_MEM_BANK_USER;
                            break;
                    }
                    byte[] accessPwd2 = ByteArrayUtils.toBytes(et_access_password2.getText().toString());
                    int sa = Integer.valueOf(et_sa.getText().toString().replace(" ", ""), 16);
                    int dl = Integer.valueOf(et_dl.getText().toString().replace(" ", ""), 16);

                    if (v.getId() == R.id.btn_read_data) {
                        sendPacket(Commands.buildReadDataFrame(accessPwd2, memBankByte2, sa, dl));
                    } else if (v.getId() == R.id.btn_write_data) {
                        byte[] data = ByteArrayUtils.toBytes(et_data.getText().toString());
                        sendPacket(Commands.buildWriteDataFrame(accessPwd2, memBankByte2, sa, dl, data));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "十六进制输入错误", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_set_channel: /* 设置工作信道 */
                try {
                    int ch = Integer.valueOf(et_channel_index.getText().toString().replace(" ", ""), 16);
                    sendPacket(Commands.buildSetChannelFrame((byte) (ch & 0xFF)));
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "十六进制输入错误", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_get_channel: /* 获取工作信道 */
                sendPacket(Commands.buildGetChannelFrame());
                break;
            case R.id.btn_get_q: /* 获取固件Q值 */
                sendPacket(Commands.buildGetQueryFrame());
                break;
            case R.id.btn_set_q: /* 设置固件Q值 */
                int sel = 0;
                switch ((String) spn_sel.getSelectedItem()) {
                    case "ALL(00)":
                        sel = 0;
                        break;
                    case "ALL(01)":
                        sel = 1;
                        break;
                    case "~SL(10)":
                        sel = 2;
                        break;
                    case "SL(11)":
                        sel = 3;
                        break;
                }
                int session = 0;
                switch ((String) spn_session.getSelectedItem()) {
                    case "S0":
                        session = 0;
                        break;
                    case "S1":
                        session = 1;
                        break;
                    case "S2":
                        session = 2;
                        break;
                    case "S3":
                        session = 3;
                        break;
                }
                int target = 0;
                switch ((String) spn_target_q.getSelectedItem()) {
                    case "A":
                        target = 0;
                        break;
                    case "B":
                        target = 1;
                        break;
                }
                int q = Integer.valueOf((String) spn_q.getSelectedItem());
                sendPacket(Commands.buildSetQueryFrame(0, 0, 1, sel, session, target, q));
                break;
            case R.id.btn_set_fhss_auto: /* 设置自动跳频模式 */
                sendPacket(Commands.buildSetFhssFrame(true));
                break;
            case R.id.btn_set_fhss_cancel: /* 取消自动跳频模式 */
                sendPacket(Commands.buildSetFhssFrame(false));
                break;
        }
    }

    @Override
    public void onPacketReceived(byte[] packet) {
        Message message = Message.obtain();
        message.what = HANDLER_WAHT_PRINT_TAG_DATA;
        ParamHashMap paramHashMap = packetDetailParser.parsePacket(packet);
        message.obj = String.format("REC -> %s\n", paramHashMap.toString());
        mainHandler.sendMessage(message);

        // -------------------------------------
        // 检查是不是Select响应结果，并且“前置Select”任务存在时，调用任务函数尝试进行下一步操作
        if (paramHashMap.containsKey("set_select_result_code") && preSetSelectTask != null) {
            byte resultCode = paramHashMap.get("set_select_result_code")[0];
            if (resultCode == 0x00) {
                preSetSelectTask.tryNextActionWithSuccess();
            } else {
                preSetSelectTask.tryNextActionWithFail();
            }
        }
    }

    private static final int HANDLER_WAHT_PRINT_TAG_DATA = 0;
    private Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case HANDLER_WAHT_PRINT_TAG_DATA:
                    appendPrintPanel((String) msg.obj);
                    break;
            }
        }
    };

    // --------------------------------------------------

    /**
     * 判断是否满足前置SetSelect操作的条件
     * 条件：Mask值都不为零（字节数组都不为零）
     * 另外，只有在进行“读数据”、“写数据”、“锁”等操作时才需要先进行Select操作
     */
    public boolean checkPreSelect() {
        byte[] maskBytes = ByteArrayUtils.toBytes(et_mask.getText().toString());
        return !ByteArrayUtils.isAllEquals(maskBytes, (byte) 0x00);
    }

    /**
     * 前置Select操作任务
     *
     * 在进行“读数据”、“写数据”、“锁”操作时需先进行Select操作，然后进行其他操作
     */
    private class PreSetSelectTask extends Thread {

        private TaskType taskType;
        private ReentrantLock lock;
        private Condition condition;

        private AtomicBoolean preSelectSuccess = new AtomicBoolean(false);

        public PreSetSelectTask(TaskType taskType) {
            this.taskType = taskType;
            this.lock = new ReentrantLock();
        }

        @Override
        public void run() {
            lock.lock();
            condition = lock.newCondition();
            try {
                // 任务开始，先发送Select，这里模拟按键触发时间
                mainHandler.post(() -> btn_set_select.performClick());
                // 等待100ms
                condition.await(100, TimeUnit.MILLISECONDS);
                // 获取Select命令的响应结果，
                // 如果该操作成功，则进行其他操作
                    // 失败无其他操作
                if (preSelectSuccess.get()) {
                    mainHandler.post(() -> {
                        switch (taskType) {
                            case READ_DATA:
                                btn_read_data.performClick();
                                break;
                            case WRITE_DATA:
                                btn_write_data.performClick();
                                break;
                            case LOCK:
                                btn_lock.performClick();
                                break;
                        }
                    });
                } else {
                    preSetSelectTask = null;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                condition = null;
                lock = null;
            }
        }

        /**
         * 收到Select成功响应结果后调用该函数，尝试进行其他操作
         */
        public void tryNextActionWithSuccess() {
            preSelectSuccess.compareAndSet(false, true);
            lock.lock();
            try {
                if (condition != null) {
                    condition.signal();
                    condition = null;
                }
            } finally {
                lock.unlock();
            }
        }

        /**
         * 收到Select失败响应结果后调用该函数
         */
        public void tryNextActionWithFail() {
            preSelectSuccess.compareAndSet(false, false);
            lock.lock();
            try {
                if (condition != null) {
                    condition.signal();
                    condition = null;
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private enum TaskType {
        READ_DATA,
        WRITE_DATA,
        LOCK
    }
}
