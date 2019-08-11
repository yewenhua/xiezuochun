package so.voc.vhome.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.hjq.toast.ToastUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.bouncycastle.util.encoders.Hex;
import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.ble.BleDataUtil;
import so.voc.vhome.ble.data.BleData;
import so.voc.vhome.databinding.ActivityDeviceSettingBinding;
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
import so.voc.vhome.widget.SwitchButton;

/**
 * Fun：设备设置
 *
 * @Author：Linus_Xie
 * @Date：2019/6/5 14:43
 */
public class DeviceSettingActivity extends BaseActivity<ActivityDeviceSettingBinding> {

    public static final String TAG = DeviceSettingActivity.class.getSimpleName();

    private BleData bleData;

    private KProgressHUD hud;
    private Observable dataOb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.device_setting));
        bleData = new BleData();
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(CommonUtils.getColor(R.color.colorBlack46))
                .setAnimationSpeed(1);
        bindingView.sbKeypadVoice.setState(true);
        bindingView.sbKeypadVoice.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                bindingView.sbKeypadVoice.setState(true);
                BleDataUtil.getInstance().write(bleData.modulation(2));
            }

            @Override
            public void toggleToOff() {
                bindingView.sbKeypadVoice.setState(false);
                BleDataUtil.getInstance().write(bleData.modulation(0));
            }
        });
        dataOb = RxBusUtil.getInstance().register(TAG, BleDataModel.class);
        dataOb.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BleDataModel>() {
            @Override
            public void accept(BleDataModel bleDataModel) throws Exception {
                hud.dismiss();
                switch (bleDataModel.getCmd()){
                    case "85":
                        String device_id = HexUtil.formatHexString(HexUtil.getByteArrayByLength(bleDataModel.getData(), 0, 12));
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BleManager.getInstance().isConnected(String.valueOf(SharedPreferencesUtils.get(Constants.BLE_MAC,"")))) {
            hud.show();
            BleDataUtil.getInstance().write(bleData.dataAcquisition());
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
    }

    public void settingClick(View view){
        switch (view.getId()){
            case R.id.ll_fingerprintManage:
                ActivityUtil.goActivity(this, FingerprintManageListActivity.class);
                break;
            case R.id.ll_cardManage:

                break;
            case R.id.ll_passwordManage:

                break;
            case R.id.btn_unbind:
                new MaterialDialog.Builder(this)
                        .title(CommonUtils.getString(R.string.tips))
                        .content(CommonUtils.getString(R.string.sure_unbind))
                        .positiveText(CommonUtils.getString(R.string.confirm))
                        .positiveColor(CommonUtils.getColor(R.color.colorRed))
                        .negativeText(CommonUtils.getString(R.string.cancel))
                        .callback(new MaterialDialog.Callback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                BleManager.getInstance().disconnect(BleDataUtil.getInstance().getBleDevice());
                                unbind();
                                dialog.dismiss();
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .build()
                        .show();
                break;
            case R.id.ll_deviceInfo:
                ActivityUtil.goActivity(this, DeviceInfoActivity.class);
                break;
        }
    }

    private void unbind() {
        HashMap<String, String> map = new HashMap<>();
        map.put("cleanHistory", "true");
        map.put("id", String.valueOf(SharedPreferencesUtils.get(Constants.DEVICE_ID, "")));
        JSONObject jsonObject = new JSONObject(map);
        OkGo.<ResponseModel>post(VHomeApi.DEVICE_UNBIND)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .isSpliceUrl(true)
                .upJson(jsonObject)
                .execute(new HudCallback<ResponseModel>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            ActivityUtil.goActivityAndFinish(DeviceSettingActivity.this, DeviceListActivity.class);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBusUtil.getInstance().unregister(TAG, dataOb);
    }
}
