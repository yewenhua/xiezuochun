package so.voc.vhome.ui.activity;

import android.bluetooth.BluetoothGatt;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.LogUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.ble.comm.ObserverManager;
import so.voc.vhome.ble.data.BleData;
import so.voc.vhome.databinding.ActivityMainBinding;
import so.voc.vhome.model.DeviceModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.model.WeatherModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.StringCallback;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.Convert;
import so.voc.vhome.util.HexUtil;
import so.voc.vhome.util.RxBusUtil;
import so.voc.vhome.util.SharedPreferencesUtils;
import so.voc.vhome.util.Util;

/**
 * Fun：主界面
 *
 * @author Linus_Xie
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> {
    public static final String TAG = MainActivity.class.getSimpleName();

    private AMapLocationClient locationClientSingle = null;
    private AMapLocationClient locationClientContinue = null;

    private BleDevice bleDevice;
    private BleData bleData;
    private String version = "";

    private DeviceModel deviceModel;
    private Observable observable;
    private Observable ob;

    private Bundle bundle = new Bundle();

    private ScheduledExecutorService executorHeart = new ScheduledThreadPoolExecutor(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bleData = new BleData();
    }

    @Override
    protected void initView() {
        Bundle bundle =  this.getIntent().getExtras();
        if (bundle != null){
            deviceModel = (DeviceModel) bundle.getSerializable("deviceModel");
        }
        if (deviceModel != null){
            bindingView.setHasDevice(true);
        }
        noTitle();
        showContentView();
        if (!String.valueOf(SharedPreferencesUtils.get(Constants.TEM, "")).isEmpty()){
            bindingView.setIsWeather(true);
            bindingView.tvTem.setText(String.valueOf(SharedPreferencesUtils.get(Constants.TEM, "")));
            bindingView.tvWea.setText(String.valueOf(SharedPreferencesUtils.get(Constants.WEA_AIR, "")));
        }

        startSingleLocation();

        observable = RxBusUtil.getInstance().register(TAG, DeviceModel.class);
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<DeviceModel>() {
            @Override
            public void accept(DeviceModel deviceModel) throws Exception {
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
                switch (integer){
                    case 0:
                        getDeviceSelected();
                        break;
                }
            }
        });
        connectBle();
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
                        if (response.body().code == 0){
                            bindingView.setHasUnread(response.body().data == 0);
                        }
                    }
                });
    }

    /**
     * 启动单次客户端定位
     */
    private void startSingleLocation() {
        if(null == locationClientSingle){
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
            if(null == location){
                LogUtils.e("定位失败~~~");
            } else {
                getWeather(location.getDistrict());
            }
        }
    };

    /**
     * 获取天气接口
     * @param district
     */
    private void getWeather(String district) {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(VHomeApi.WEATHER_URL + "?version=v1&city=" + district.replaceAll("[市,县,区]+",""))
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
                if (response.code() == 200){
                    WeatherModel weatherModel = Convert.fromJson(response.body().string(), WeatherModel.class);
                    for (WeatherModel.DataBean dataBean : weatherModel.getData()) {
                                if(dataBean.getDay().contains("今天")){
                                    runOnUiThread(() ->{
                                        bindingView.setIsWeather(true);
                                        switch (dataBean.getWea_img()){
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
        if(null != locationClientSingle){
            locationClientSingle.onDestroy();
            locationClientSingle = null;
        }
        if(null != locationClientContinue){
            locationClientContinue.onDestroy();
            locationClientContinue = null;
        }
        RxBusUtil.getInstance().unregister(this, observable);
        RxBusUtil.getInstance().unregister(this, ob);
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }

    public void mainClick(View view){
        switch (view.getId()){
            case R.id.fl_msg:
                ActivityUtil.goActivity(this, NotifyActivity.class);
                break;
            case R.id.iv_setting:
                ActivityUtil.goActivity(this, UserInfoActivity.class);
                break;
            case R.id.ll_deviceList:
                ActivityUtil.goActivity(this, DeviceListActivity.class);
                break;
            case R.id.ll_lockName:
                ActivityUtil.goActivity(this, DeviceInfoActivity.class);
                break;
            case R.id.btn_addDevice:
                ActivityUtil.goActivity(this, AddDeviceActivity.class);
                break;
            case R.id.ll_openRecord:
                ActivityUtil.goActivity(this, OpenRecordActivity.class);
                break;
            case R.id.ll_alarmRecord:
                ActivityUtil.goActivity(this, AlarmRecordActivity.class);
                break;
            case R.id.ll_memberManage:
                ActivityUtil.goActivity(this, MemberListActivity.class);
                break;
            case R.id.ll_shareKey:
                ActivityUtil.goActivity(this, ShareKeyActivity.class);
                break;
            case R.id.ll_shareDevice:
                ActivityUtil.goActivity(this, ShareDeviceActivity.class);
                break;
            case R.id.ll_deviceSetting:
                bundle.putParcelable("bleDevice", bleDevice);
                ActivityUtil.goActivityWithBundle(this, DeviceSettingActivity.class, bundle);
                break;
            case R.id.rl_keyBg:
                startAnim();
                write(bleDevice, bleData.openLock());
                break;
        }

    }

    private void getDeviceSelected() {
        OkGo.<ResponseModel<DeviceModel>>get(VHomeApi.DEVICE_MEMBER_SELECTED)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .tag(this)
                .execute(new StringCallback<ResponseModel<DeviceModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<ResponseModel<DeviceModel>> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            DeviceModel deviceModel = response.body().data;
                            if (deviceModel != null) {
                                bindingView.setHasDevice(true);
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

//    private String macAddress = "D8:A9:8B:AF:6A:33";
        private String macAddress = "90:9A:77:25:0C:6C";

    private void connectBle() {
        SharedPreferencesUtils.put(Constants.BLE_MAC, macAddress);
        BleManager.getInstance().connect(macAddress, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                bindingView.rvRipple.start();
                LogUtils.e(macAddress + "》》》》》开始连接");
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                bindingView.rvRipple.stop();
                LogUtils.e(macAddress + "》》》》》连接失败");
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                bindingView.rvRipple.stopImmediately();
                bindingView.rlKeyBg.setBackgroundResource(R.mipmap.connect_bg);
                LogUtils.e(macAddress + "》》》》》连接成功");
                getVersion(bleDevice, bleData.firmwareQuery());
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                bindingView.rlKeyBg.setBackgroundResource(R.mipmap.unconnect_bg);
                bindingView.rvRipple.stop();
                if (!isActiveDisConnected){
                    LogUtils.e(macAddress + "》》》》》断开了");
                } else {
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                    LogUtils.e(macAddress + "》》》》》断开连接");
                }
            }
        });
    }

    /**
     * 获取版本号
     * @param data
     */
    private void getVersion(BleDevice bleDevice, byte[] data) {
        this.bleDevice = bleDevice;
        write(bleDevice, data);
        BleManager.getInstance().notify(
                bleDevice,
                Constants.SERVICE_UUID,
                Constants.UUID,
                new BleNotifyCallback() {

                    @Override
                    public void onNotifySuccess() {
                        Log.e("notify:>>>>>", "onNotifySuccess: ");
                    }

                    @Override
                    public void onNotifyFailure(final BleException exception) {
                        Log.e("notify:>>>>>", "exception: " + exception.toString());
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        readData(data);
                        Log.e("notify:>>>>>", "onCharacteristicChanged: " + HexUtil.formatHexString(data, true));
                    }
                });
    }

    private void write(BleDevice bleDevice, byte[] data) {
        BleManager.getInstance().write(
                bleDevice,
                Constants.SERVICE_UUID,
                Constants.UUID,
                data,
                new BleWriteCallback() {

                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        Log.e("getVersion:>>>>>", HexUtil.formatHexString(justWrite, true));
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        Log.e("getVersion:>>>>>", exception.toString());
                    }
                });

    }

    /**
     * 处理回调结果
     *
     * @param originalData
     */
    private void readData(byte[] originalData) {
        bindingView.ivLock.clearAnimation();
        byte[] cmd = HexUtil.getByteArrayByLength(originalData, 1, 1);
        int len = Integer.valueOf(HexUtil.getByteArrayByLength(originalData, 2, 1)[0]);
        byte[] data = HexUtil.getByteArrayByLength(originalData, 3, len);
        Log.e("data:>>>>>", HexUtil.formatHexString(data, true));
        switch (HexUtil.bytesToHexString(cmd)) {
            case "81":
                if (HexUtil.checkByte(HexUtil.getByteArrayByLength(data, 0, 1), bleData.none)) {
                    version = new String(HexUtil.getByteArrayByLength(data, 1, len - 1));
                    Log.e("获取版本>>>>：", version);
                    write(bleDevice, bleData.registrationByPassword("12345678"));
                } else {
                    Log.e("获取版本>>>>：", ">>>>>>>出错");
                }
                break;
            case "82"://注册
                ToastUtils.show("注册成功！");
                heartBeat();
                break;
            case "84"://开门
                if (HexUtil.checkByte(data, bleData.none)){
                    ToastUtils.show("开门成功！");
                } else {
                    ToastUtils.show("开门失败！");
                }
                bindingView.ivLock.clearAnimation();
                break;
        }
    }

    /**
     * 发送心跳包
     */
    private void heartBeat() {
        executorHeart.scheduleAtFixedRate(() -> {
            write(bleDevice, bleData.heartBeat());
        }, 0, 10, TimeUnit.SECONDS);
    }

    /**
     * 开启钥匙旋转动画
     */
    private void startAnim() {
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        if (rotate != null) {
            bindingView.ivLock.startAnimation(rotate);
        }  else {
            bindingView.ivLock.setAnimation(rotate);
            bindingView.ivLock.startAnimation(rotate);
        }
    }

}
