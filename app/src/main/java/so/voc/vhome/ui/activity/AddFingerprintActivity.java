package so.voc.vhome.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.hjq.toast.ToastUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.ble.BleDataUtil;
import so.voc.vhome.ble.data.BleData;
import so.voc.vhome.databinding.ActivityAddFingerprintBinding;
import so.voc.vhome.model.BleDataModel;
import so.voc.vhome.model.FingerModel;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.HexUtil;
import so.voc.vhome.util.RxBusUtil;
import so.voc.vhome.util.SharedPreferencesUtils;

/**
 * Fun：添加指纹
 *
 * @Author：Linus_Xie
 * @Date：2019/6/10 19:02
 */
public class AddFingerprintActivity extends BaseActivity<ActivityAddFingerprintBinding> {

    public static final String TAG = AddFingerprintActivity.class.getSimpleName();

    private BleData bleData;
    private String id;
    private Observable dataOb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fingerprint);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.add_fingerprint));
        bleData = new BleData();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("fingerNo");
        }

        dataOb = RxBusUtil.getInstance().register(TAG, BleDataModel.class);
        dataOb.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BleDataModel>() {
            @Override
            public void accept(BleDataModel bleDataModel) throws Exception {
                switch (bleDataModel.getCmd()){
                    case "89":
                        byte[] state = HexUtil.getByteArrayByLength(bleDataModel.getData(), 0, 1);
                        if (HexUtil.checkByte(state, bleData.none)){
                            if (bleDataModel.getLen() == 3) {
                                int times = Integer.valueOf(HexUtil.getByteArrayByLength(bleDataModel.getData(), 2, 1)[0]);
                                if (times == 0) {
                                    //TODO 开始添加
                                    bindingView.rlSecondStep.setVisibility(View.VISIBLE);
                                    bindingView.rlFirstStep.setVisibility(View.GONE);
                                    bindingView.ivFingerprint.setBackgroundResource(R.mipmap.iv_finger_0);
                                    bindingView.tvNum.setText("0");
                                } else if (times == 1) {
                                    bindingView.ivFingerprint.setBackgroundResource(R.mipmap.iv_finger_1);
                                    bindingView.tvNum.setText("1");
                                } else if (times == 2){
                                    bindingView.ivFingerprint.setBackgroundResource(R.mipmap.iv_finger_2);
                                    bindingView.tvNum.setText("2");
                                } else if (times == 3){
                                    bindingView.ivFingerprint.setBackgroundResource(R.mipmap.iv_finger_3);
                                    bindingView.tvNum.setText("3");
                                } else if (times == 4){
                                    bindingView.ivFingerprint.setBackgroundResource(R.mipmap.iv_finger_4);
                                    bindingView.tvNum.setText("4");
                                    bindingView.btnAddComplete.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        break;
                }

            }
        });
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
    }

    public void addFingerClick(View view) {
        switch (view.getId()) {
            case R.id.btn_addFingerprint:
                BleDataUtil.getInstance().write(bleData.fingerAdd(Integer.valueOf(id)));
                break;
            case R.id.btn_addComplete:
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putSerializable("fingerModel", new FingerModel());
                ActivityUtil.goActivityWithBundle(AddFingerprintActivity.this, FingerprintManagerActivity.class, bundle);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBusUtil.getInstance().unregister(TAG, dataOb);
    }
}
