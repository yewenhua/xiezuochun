package so.voc.vhome.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.LogUtils;
import com.clj.fastble.BleManager;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.ble.BleDataUtil;
import so.voc.vhome.ble.data.BleData;
import so.voc.vhome.databinding.ActivityMain01Binding;
import so.voc.vhome.model.BleDataModel;
import so.voc.vhome.model.DeviceModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.model.WeatherModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.StringCallback;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.Convert;
import so.voc.vhome.util.HexUtil;
import so.voc.vhome.util.RxBusUtil;
import so.voc.vhome.util.SharedPreferencesUtils;
import so.voc.vhome.util.Util;

/**
 * Fun：1.0版本的主界面，不含日志等功能
 *
 * @Author：Linus_Xie
 * @Date：2019/7/4 15:03
 */
public class Main01Activity extends BaseActivity<ActivityMain01Binding> {
    public static final String TAG = Main01Activity.class.getSimpleName();

    private AMapLocationClient locationClientSingle = null;
    private AMapLocationClient locationClientContinue = null;

    private BleData bleData;

    private DeviceModel deviceModel;
    private Observable observable;
    private Observable ob;
    private Observable dataOb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_01);
        bleData = new BleData();
    }

    @Override
    protected void initView() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            deviceModel = (DeviceModel) bundle.getSerializable("deviceModel");
        }
        if (deviceModel != null) {
            bindingView.setHasDevice(true);
            bindingView.llTitle.setBackgroundColor(CommonUtils.getColor(R.color.colorMain));
            //TODO 暂时有设备先去做链接蓝牙操作，以后不能这么写，需要判断
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                bindingView.rvRipple.start();
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
        } else {
            bindingView.llTitle.setBackgroundResource(R.drawable.title_bg_01);
        }
        noTitle();
        showContentView();
        if (!String.valueOf(SharedPreferencesUtils.get(Constants.TEM, "")).isEmpty()) {
            bindingView.setIsWeather(true);
            bindingView.tvTem.setText(String.valueOf(SharedPreferencesUtils.get(Constants.TEM, "")));
            bindingView.tvWea.setText(String.valueOf(SharedPreferencesUtils.get(Constants.WEA_AIR, "")));
        }
        startSingleLocation();
        initObservable();
    }

    private void initObservable() {
        observable = RxBusUtil.getInstance().register(TAG, DeviceModel.class);
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<DeviceModel>() {
            @Override
            public void accept(DeviceModel deviceModel) throws Exception {
                bindingView.setHasDevice(true);
                bindingView.llTitle.setBackgroundColor(CommonUtils.getColor(R.color.colorMain));
                SharedPreferencesUtils.put(Constants.ID, deviceModel.getId());
                SharedPreferencesUtils.put(Constants.DEVICE_ID, deviceModel.getDeviceId());
                SharedPreferencesUtils.put(Constants.DEVICE_NAME, deviceModel.getDeviceName());
                SharedPreferencesUtils.put(Constants.AUTHORITY_NAME, deviceModel.getDeviceMemberAuthority());
                bindingView.tvLockName.setText(deviceModel.getDeviceName());
                bindingView.setIsAdmin(deviceModel.getDeviceMemberAuthority().equals(Constants.OWNER));
            }
        });

        ob = RxBusUtil.getInstance().register("onBackPressed", Integer.class);
        ob.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                switch (integer) {
                    case 0:
                        getDeviceSelected();
                        break;
                }
            }
        });

        dataOb = RxBusUtil.getInstance().register(TAG + "Notify", BleDataModel.class);
        dataOb.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BleDataModel>() {
            @Override
            public void accept(BleDataModel bleDataModel) throws Exception {
                if (HexUtil.checkByte(bleDataModel.getData(), bleData.none)) {
                    ToastUtils.show("开门成功！");
                } else {
                    ToastUtils.show("开门失败！");
                }
                //TODO 纯属PM为了让动画多一会儿
                new Handler().postDelayed(() ->{
                    bindingView.ivLock.clearAnimation();
                }, 1000);
            }
        });

        connectOb.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                switch (integer) {
                    case 0:
                        bindingView.rvRipple.start();
                        break;
                    case 1:
                    case 3:
                        //TODO 连接失败
                        bindingView.rlKeyBg.setBackgroundResource(R.mipmap.unconnect_bg);
                        bindingView.rvRipple.stop();
                        break;
                    case 2:
                        bindingView.rvRipple.stopImmediately();
                        bindingView.rlKeyBg.setBackgroundResource(R.mipmap.connect_bg);
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        unreadCount();
        bindingView.tvUserName.setText(String.valueOf(SharedPreferencesUtils.get(Constants.USER_NAME, "")).isEmpty() ? Util.hidePhoneNum(String.valueOf(SharedPreferencesUtils.get(Constants.USER_PHONE, ""))) : String.valueOf(SharedPreferencesUtils.get(Constants.USER_NAME, "")));
        bindingView.tvLockName.setText(String.valueOf(SharedPreferencesUtils.get(Constants.DEVICE_NAME, "")));
        bindingView.setIsAdmin(String.valueOf(SharedPreferencesUtils.get(Constants.AUTHORITY_NAME, "")).equals(Constants.OWNER));
    }

    /**
     * 获取站内未读消息数量
     */
    private void unreadCount() {
        OkGo.<ResponseModel<Integer>>get(VHomeApi.NOTIFICATION_UNREADCOUNT)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .tag(this)
                .execute(new StringCallback<ResponseModel<Integer>>() {
                    @Override
                    public void onSuccess(Response<ResponseModel<Integer>> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0) {
                            bindingView.setHasUnread(response.body().data == 0);
                        }
                    }
                });
    }

    /**
     * 启动单次客户端定位
     */
    private void startSingleLocation() {
        if (null == locationClientSingle) {
            locationClientSingle = new AMapLocationClient(this.getApplicationContext());
        }
        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
        //使用单次定位
        locationClientOption.setOnceLocation(true);
        // 地址信息
        locationClientOption.setNeedAddress(true);
        locationClientOption.setLocationCacheEnable(false);
        locationClientSingle.setLocationOption(locationClientOption);
        locationClientSingle.setLocationListener(locationSingleListener);
        locationClientSingle.startLocation();
    }

    /**
     * 单次客户端的定位监听
     */
    AMapLocationListener locationSingleListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null == location) {
                LogUtils.e("定位失败~~~");
            } else {
                getWeather(location.getDistrict());
            }
        }
    };

    /**
     * 获取天气接口
     *
     * @param district
     */
    private void getWeather(String district) {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(VHomeApi.WEATHER_URL + "?version=v1&city=" + district.replaceAll("[市,县,区]+", ""))
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.code() == 200) {
                    WeatherModel weatherModel = Convert.fromJson(response.body().string(), WeatherModel.class);
                    for (WeatherModel.DataBean dataBean : weatherModel.getData()) {
                        if (dataBean.getDay().contains("今天")) {
                            runOnUiThread(() -> {
                                bindingView.setIsWeather(true);
                                switch (dataBean.getWea_img()) {
                                    case "yun":
                                        bindingView.ivWeather.setBackgroundResource(R.drawable.cloudy);
                                        break;
                                    case "yu":
                                        bindingView.ivWeather.setBackgroundResource(R.drawable.light_rain);
                                        break;
                                }
                                bindingView.tvTem.setText(dataBean.getTem());
                                SharedPreferencesUtils.put(Constants.WEA_IMG, dataBean.getWea_img());
                                SharedPreferencesUtils.put(Constants.TEM, dataBean.getTem());
                                bindingView.tvWea.setText(dataBean.getWea() + "  " + dataBean.getAir_level());
                                SharedPreferencesUtils.put(Constants.WEA_AIR, dataBean.getWea() + "  " + dataBean.getAir_level());
                            });
                        }
                    }
                }
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != locationClientSingle) {
            locationClientSingle.onDestroy();
            locationClientSingle = null;
        }
        if (null != locationClientContinue) {
            locationClientContinue.onDestroy();
            locationClientContinue = null;
        }
        RxBusUtil.getInstance().unregister(this, observable);
        RxBusUtil.getInstance().unregister(this, ob);
        RxBusUtil.getInstance().unregister(this, dataOb);
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }

    private void getDeviceSelected() {
        OkGo.<ResponseModel<DeviceModel>>get(VHomeApi.DEVICE_MEMBER_SELECTED)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .tag(this)
                .execute(new StringCallback<ResponseModel<DeviceModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<ResponseModel<DeviceModel>> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0) {
                            DeviceModel deviceModel = response.body().data;
                            if (deviceModel != null) {
                                bindingView.setHasDevice(true);
                                bindingView.llTitle.setBackgroundColor(CommonUtils.getColor(R.color.colorMain));
                                SharedPreferencesUtils.put(Constants.ID, deviceModel.getId());
                                SharedPreferencesUtils.put(Constants.DEVICE_ID, deviceModel.getDeviceId());
                                SharedPreferencesUtils.put(Constants.DEVICE_NAME, deviceModel.getDeviceName());
                                SharedPreferencesUtils.put(Constants.AUTHORITY_NAME, deviceModel.getDeviceMemberAuthority());
                                bindingView.tvLockName.setText(String.valueOf(SharedPreferencesUtils.get(Constants.DEVICE_NAME, "")));
                                bindingView.setIsAdmin(String.valueOf(SharedPreferencesUtils.get(Constants.AUTHORITY_NAME, "")).equals(Constants.OWNER));

                            } else {
                                bindingView.setHasDevice(false);
                            }
                        } else {
                            ToastUtils.show(response.body().msg);
                        }
                    }
                });
    }

    /**
     * 开启钥匙旋转动画
     */
    private void startAnim() {
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        if (rotate != null) {
            bindingView.ivLock.startAnimation(rotate);
        } else {
            bindingView.ivLock.setAnimation(rotate);
            bindingView.ivLock.startAnimation(rotate);
        }
    }

    public void mainClick(View view) {
        switch (view.getId()) {
            case R.id.fl_msg:
                ActivityUtil.goActivity(this, NotifyActivity.class);
                break;
            case R.id.iv_setting:
                ActivityUtil.goActivity(this, UserInfoActivity.class);
                break;
            case R.id.iv_deviceList:
                ActivityUtil.goActivity(this, DeviceListActivity.class);
                break;
            case R.id.ll_lockName:
                ActivityUtil.goActivity(this, DeviceInfoActivity.class);
                break;
            case R.id.btn_addDevice:
                ActivityUtil.goActivity(this, AddDeviceActivity.class);
                break;
            case R.id.ll_shareKey:
                if (SharedPreferencesUtils.get(Constants.AUTHORITY_NAME, "").equals(Constants.OWNER)){
                    ActivityUtil.goActivity(this, ShareKeyActivity.class);
                } else {
                    //TODO 不是管理员
                    showDialog();
                }

                break;
            case R.id.ll_shareDevice:
                if (SharedPreferencesUtils.get(Constants.AUTHORITY_NAME, "").equals(Constants.OWNER)) {
                    ActivityUtil.goActivity(this, ShareDeviceActivity.class);
                } else {
                    //TODO 不是管理员
                    showDialog();
                }
                break;
            case R.id.ll_memberManage:
                ActivityUtil.goActivity(this, MemberListActivity.class);
                break;
            case R.id.ll_deviceSetting:
                if (SharedPreferencesUtils.get(Constants.AUTHORITY_NAME, "").equals(Constants.OWNER)){
                    ActivityUtil.goActivity(this, DeviceSettingActivity.class);
                } else {
                    //TODO 不是管理员
                    showDialog();
                }
                break;
            case R.id.rl_keyBg:
                if (BleManager.getInstance().isConnected(String.valueOf(SharedPreferencesUtils.get(Constants.BLE_MAC, "")))){
                    startAnim();
                    BleDataUtil.getInstance().write(bleData.openLock());
                } else {
                    BleDataUtil.getInstance().connectBle();
                }
                break;
        }

    }

    private void showDialog() {
        new MaterialDialog.Builder(this)
                .title(CommonUtils.getString(R.string.tips))
                .content(CommonUtils.getString(R.string.not_an_admin))
                .positiveText(CommonUtils.getString(R.string.confirm))
                .positiveColor(CommonUtils.getColor(R.color.colorRed))
                .callback(new MaterialDialog.SimpleCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show();
    }
}
