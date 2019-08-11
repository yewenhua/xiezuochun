package so.voc.vhome.ble;

import android.bluetooth.BluetoothGatt;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.hjq.toast.ToastUtils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import so.voc.vhome.R;
import so.voc.vhome.ble.data.BleData;
import so.voc.vhome.model.BleDataModel;
import so.voc.vhome.ui.activity.AddFingerprintActivity;
import so.voc.vhome.ui.activity.BleBindingActivity;
import so.voc.vhome.ui.activity.DeviceSettingActivity;
import so.voc.vhome.ui.activity.FingerprintManageListActivity;
import so.voc.vhome.ui.activity.FingerprintManagerActivity;
import so.voc.vhome.ui.activity.Main01Activity;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.HexUtil;
import so.voc.vhome.util.RxBusUtil;
import so.voc.vhome.util.SharedPreferencesUtils;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/7/8 15:30
 */
public class BleDataUtil {

    public static final String TAG = BleDataUtil.class.getSimpleName();

    private static BleDataUtil instance;
    private BleDevice bleDevice;

    private BleData bleData = new BleData();
    private ScheduledExecutorService executorHeart = new ScheduledThreadPoolExecutor(2);

    public static BleDataUtil getInstance() {
        if (instance == null){
            synchronized (BleDataUtil.class){
                if (instance == null){
                    instance = new BleDataUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 连接蓝牙
     */
    public void connectBle(){
        SharedPreferencesUtils.put(Constants.BLE_MAC, "90:9A:77:25:0C:6C");
        String macAddress = String.valueOf(SharedPreferencesUtils.get(Constants.BLE_MAC, ""));
        BleManager.getInstance().connect(macAddress, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                //正在连接的object为 0
                LogUtils.e(macAddress + "》》》》》开始连接");
                RxBusUtil.getInstance().post(TAG, 0);
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                //正在连接的object为 1
                RxBusUtil.getInstance().post(TAG, 1);
                ToastUtils.show(CommonUtils.getString(R.string.connect_fail));
                LogUtils.e(macAddress + "》》》》》连接失败");
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                //正在连接的object为 2
                RxBusUtil.getInstance().post(TAG, 2);
                LogUtils.e(macAddress + "》》》》》连接成功");
                write(bleDevice, bleData.firmwareQuery());
                notifyData();
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                //正在连接的object为 3
                RxBusUtil.getInstance().post(TAG, 3);
                ToastUtils.show(CommonUtils.getString(R.string.device_disconnect));
                LogUtils.e(macAddress + "》》》》》断开了");
            }
        });
    }

    /**
     * 入参带BleDevice的写入方法
     * @param bleDevice
     * @param data
     */
    private void write(BleDevice bleDevice, byte[] data) {
        this.bleDevice = bleDevice;
        BleManager.getInstance().write(
                bleDevice,
                Constants.SERVICE_UUID,
                Constants.UUID,
                data,
                new BleWriteCallback() {

                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                    }
                });
    }

    /**
     * 写数据
     * @param data
     */
    public void write(byte[] data){
        BleManager.getInstance().write(
                bleDevice,
                Constants.SERVICE_UUID,
                Constants.UUID,
                data,
                new BleWriteCallback() {

                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                    }
                });
    }

    /**
     * 绑定监听
     */
    public void notifyData(){
        BleManager.getInstance().notify(
                bleDevice,
                Constants.SERVICE_UUID,
                Constants.UUID,
                new BleNotifyCallback() {

                    @Override
                    public void onNotifySuccess() {
                    }

                    @Override
                    public void onNotifyFailure(final BleException exception) {
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        LogUtils.e("onCharacteristicChanged: " + HexUtil.formatHexString(data, true));
                        if (data.length > 0) {
                            readData(data);
                        }
                    }
                });
    }

    private void readData(byte[] originalData) {
        if (originalData.length == 0){
            return;
        }
        Log.e("originalData:>>>>>", HexUtil.formatHexString(originalData, true));
        String cmd = HexUtil.formatHexString(HexUtil.getByteArrayByLength(originalData, 1, 1)).toUpperCase();
        int len = Integer.valueOf(HexUtil.getByteArrayByLength(originalData, 2, 1)[0]);
        byte[] data = HexUtil.getByteArrayByLength(originalData, 3, len);
        if (data.length == 0){
            return;
        }
        Log.e("data:>>>>>", HexUtil.formatHexString(data, true));
        BleDataModel bleDataModel = new BleDataModel();
        bleDataModel.setCmd(cmd);
        bleDataModel.setLen(len);
        bleDataModel.setData(data);
        switch (cmd) {
            case "81":
            case "82"://注册
                RxBusUtil.getInstance().post(BleBindingActivity.TAG, bleDataModel);
                LogUtils.i(new String(HexUtil.getByteArrayByLength(data, 1, len - 1)));
                heartBeat();
                break;
            case "84"://开门
                RxBusUtil.getInstance().post(Main01Activity.TAG + "Notify", bleDataModel);
                break;
            case "85"://数据获取指令
                RxBusUtil.getInstance().post(DeviceSettingActivity.TAG, bleDataModel);
                break;
            case "89":
                RxBusUtil.getInstance().post(AddFingerprintActivity.TAG, bleDataModel);
                break;
            case "94":
                RxBusUtil.getInstance().post(FingerprintManageListActivity.TAG , bleDataModel);
                break;
            case "8A":
                RxBusUtil.getInstance().post(FingerprintManagerActivity.TAG , bleDataModel);
                break;
        }
    }

    /**
     * 发送心跳
     */
    private void heartBeat() {
        executorHeart.scheduleAtFixedRate(() -> {
            write(bleDevice, bleData.heartBeat());
        }, 0, 10, TimeUnit.SECONDS);
    }

    public BleDevice getBleDevice() {
        return bleDevice;
    }

    public void setBleDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }
}
