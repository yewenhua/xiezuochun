package so.voc.vhome.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;


import java.util.List;

import so.voc.vhome.R;
import so.voc.vhome.adapter.BleAdapter;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityBleListBinding;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.DialogUtil;

/**
 * Fun：蓝牙列表
 *
 * @Author：Linus_Xie
 * @Date：2019/6/11 11:34
 */
public class BleListActivity extends BaseActivity<ActivityBleListBinding> {

    private BleAdapter mAdapter;

    private BluetoothAdapter mBluetoothAdapter;

    private boolean isScan = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_list);

        openBle();
    }

    private void openBle() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        //打开蓝牙权限
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BLE);
        }
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.add_device));

        mAdapter = new BleAdapter(this);
        bindingView.lvBle.setAdapter(mAdapter);

        mAdapter.setOnDeviceClickListener(new BleAdapter.OnDeviceClickListener() {
            @Override
            public void onConnect(BleDevice bleDevice) {
                Intent intent = new Intent(BleListActivity.this, BleBindingActivity.class);
                intent.putExtra("bleMac", bleDevice.getMac());
                startActivity(intent);
            }
        });
    }

    public void bleClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                if (isScan) {
                    isScan = false;
                    bindingView.setIsScan(isScan);
                    BleManager.getInstance().cancelScan();
                } else {
                    PermissionUtils.permission(PermissionConstants.LOCATION)
                            .callback(new PermissionUtils.FullCallback() {
                                @Override
                                public void onGranted(List<String> permissionsGranted) {
                                    isScan = true;
                                    bindingView.setIsScan(isScan);
                                    startScan();
                                }

                                @Override
                                public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                                    LogUtils.e(permissionsDeniedForever, permissionsDenied);
                                    if (!permissionsDeniedForever.isEmpty()) {
                                        DialogUtil.appSettingDialog(BleListActivity.this);
                                    }
                                }
                            }).request();

                }
                break;
        }
    }

    private void startScan() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mAdapter.clearScanDevice();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                mAdapter.addDevice(bleDevice);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                isScan = false;
                bindingView.setIsScan(isScan);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showConnectedDevice();
    }

    private void showConnectedDevice() {
        List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
        mAdapter.clearConnectedDevice();
        for (BleDevice bleDevice : deviceList) {
            mAdapter.addDevice(bleDevice);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }
}
