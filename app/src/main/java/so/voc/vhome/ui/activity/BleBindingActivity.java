package so.voc.vhome.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.hjq.toast.ToastUtils;
import com.king.zxing.BeepManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.ble.BleDataUtil;
import so.voc.vhome.ble.comm.Observer;
import so.voc.vhome.ble.comm.ObserverManager;
import so.voc.vhome.ble.data.BleData;
import so.voc.vhome.databinding.ActivityBleBindingBinding;
import so.voc.vhome.model.BleDataModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.HudCallback;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.HexUtil;
import so.voc.vhome.util.RxBusUtil;
import so.voc.vhome.util.SharedPreferencesUtils;
import so.voc.vhome.util.Util;

/**
 * Fun：蓝牙添加设备
 *
 * @Author：Linus_Xie
 * @Date：2019/6/11 15:05
 */
public class BleBindingActivity extends BaseActivity<ActivityBleBindingBinding> {

    public static final String TAG = BleBindingActivity.class.getSimpleName();

    private String bleMac = "";
    private BleData bleData;
    private String version = "";

    private Observable dataOb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_binding);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.add_device));
        bleMac = this.getIntent().getExtras().getString("bleMac");
        SharedPreferencesUtils.put(Constants.BLE_MAC, bleMac);
        bleData = new BleData();
        BleDataUtil.getInstance().connectBle();

        if (BleManager.getInstance().isConnected(String.valueOf(SharedPreferencesUtils.get(Constants.BLE_MAC, "")))) {
            BleDataUtil.getInstance().notifyData();
        } else {
            setDeal(CommonUtils.getString(R.string.connect_device));
            mBaseBinding.tvDeal.setOnClickListener(v -> {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter.isEnabled()) {
                    ToastUtils.show(CommonUtils.getString(R.string.reconnect));
                    BleDataUtil.getInstance().connectBle();
                } else {
                    final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
                    bluetoothAdapter = bluetoothManager.getAdapter();
                    //打开蓝牙权限
                    if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BLE);
                    }
                }
            });
        }

        dataOb = RxBusUtil.getInstance().register(TAG, BleDataModel.class);
        dataOb.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BleDataModel>() {
            @Override
            public void accept(BleDataModel bleDataModel) throws Exception {
                switch (bleDataModel.getCmd()){
                    case "81":
                        byte[] data = bleDataModel.getData();
                        int len = bleDataModel.getLen();
                        if (HexUtil.checkByte(HexUtil.getByteArrayByLength(data, 0, 1), bleData.none)) {
                            version = new String(HexUtil.getByteArrayByLength(data, 1, len - 1));
                            String type = version.substring(0 , 1);
                            if (type.toLowerCase().equals("a")) {
                                BleDataUtil.getInstance().write( bleData.registrationByPassword("12345678"));
                            } else {
                                BleDataUtil.getInstance().write( bleData.registrationByKey());
                            }
                        } else {
                            //TODO 获取版本出错
                        }
                        break;
                    case "82":
                        String key = HexUtil.formatHexString(bleDataModel.getData());
                        //TODO 将密钥存到后台
                        break;

                }
            }
        });

    }

    public void bindingClick(View view){
        switch (view.getId()){
            case R.id.btn_binding:
                String deviceName = bindingView.etDeviceName.getText().toString().trim();
                String deviceModel = bindingView.tvDeviceModel.getText().toString().trim();
                String deviceSn = bindingView.tvDeviceSN.getText().toString().trim();
                HashMap<String, String> map = new HashMap<>();
//        map.put("name", deviceName);
//        map.put("sn", deviceSn);

                map.put("name", deviceName);
                map.put("sn", "vhome100");
//                map.put("sn", "vhome003");

                JSONObject jsonObject = new JSONObject(map);
                OkGo.<ResponseModel>post(VHomeApi.DEVICE_BIND)
                        .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                        .isSpliceUrl(true)
                        .upJson(jsonObject)
                        .tag(this)
                        .execute(new HudCallback<ResponseModel>(this) {
                              @Override
                            public void onSuccess(Response<ResponseModel> response) {
                                super.onSuccess(response);
                                if (response.body().code == 0){
                                    RxBusUtil.getInstance().post(DeviceListActivity.TAG, 0);
                                    ActivityUtil.goActivityAndFinish(BleBindingActivity.this, DeviceListActivity.class);
                                }
                            }
                        });
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBusUtil.getInstance().unregister(TAG, dataOb);
    }
}
