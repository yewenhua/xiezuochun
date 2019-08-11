package so.voc.vhome.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.cache.Sp;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivitySplashBinding;
import so.voc.vhome.model.DeviceModel;
import so.voc.vhome.model.OauthModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.BasicAuthInterceptor;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.StringCallback;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.Convert;
import so.voc.vhome.util.RxBusUtil;
import so.voc.vhome.util.SharedPreferencesUtils;
import so.voc.vhome.util.Util;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/1 16:33
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    public static final String TAG = SplashActivity.class.getSimpleName();

    private Observable<Integer> observable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initView() {
        noTitle();
        observable = RxBusUtil.getInstance().register(TAG, Integer.class);
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                switch (integer) {
                    case 0:
                        //getDeviceSelected();
                        memberMessage();
                        break;
                    case 1:
                        ToastUtils.show(CommonUtils.getString(R.string.logon_invalidation));
                        ActivityUtil.goActivityAndFinish(SplashActivity.this, LoginActivity.class);
                        finish();
                        break;
                }

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    refresh();
                } catch (IOException e) {
                    e.printStackTrace();
                    RxBusUtil.getInstance().post(TAG, 1);
                }
            }
        }).start();
    }

    /**
     * 获取用户信息
     */
    private void memberMessage() {
        OkGo.<ResponseModel<OauthModel.UserInfoBean>>get(VHomeApi.MEMBER_MESSAGE)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .tag(this)
                .execute(new StringCallback<ResponseModel<OauthModel.UserInfoBean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<ResponseModel<OauthModel.UserInfoBean>> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            OauthModel.UserInfoBean userInfoBean = response.body().data;
                            SharedPreferencesUtils.put(Constants.USER_NAME, Util.initNullStr(userInfoBean.getName()));
                            SharedPreferencesUtils.put(Constants.USER_PHONE, Util.initNullStr(userInfoBean.getMobile()));
                            if (!Util.initNullStr(userInfoBean.getHeadIcon()).isEmpty()){
                                SharedPreferencesUtils.put(Constants.HEAD_URL, VHomeApi.GET_HEAD_ICON
                                        + userInfoBean.getHeadIcon() + "?access_token="
                                        + String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")));
                            }
                            getDeviceSelected();
                        }
                    }
                });
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
                                SharedPreferencesUtils.put(Constants.ID, deviceModel.getId());
                                SharedPreferencesUtils.put(Constants.DEVICE_ID, deviceModel.getDeviceId());
                                SharedPreferencesUtils.put(Constants.DEVICE_NAME, Util.initNullStr(deviceModel.getDeviceName()));
                                SharedPreferencesUtils.put(Constants.AUTHORITY_NAME, deviceModel.getDeviceMemberAuthority());
                            }
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("deviceModel", deviceModel);
                            if (Util.currentVersionCode().equals("2")){
                                ActivityUtil.goActivityWithBundle(SplashActivity.this, MainActivity.class, bundle);
                            } else {
                                ActivityUtil.goActivityWithBundle(SplashActivity.this, Main01Activity.class, bundle);
                            }
                            finish();
                        } else {
                            ToastUtils.show(response.body().msg);
                        }
                    }
                });
    }


    private void refresh() throws IOException {
        OauthModel oauth = new OauthModel();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor("client_app", "123abc"))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", String.valueOf(SharedPreferencesUtils.get(Constants.REFRESH_TOKEN, "")))
                .add("client_id", "client_app")
                .add("client_secret", "123abc")
                .build();
        Request request = new Request.Builder()
                .url(VHomeApi.REFRESH_TOKEN)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        LogUtils.e(response);
        if (!response.isSuccessful()) {
            throw new IOException("服务器端错误: " + response);
        }
        String responseStr = response.body().string();
        LogUtils.e(responseStr);
        if (responseStr.contains(Constants.ACCESS_TOKEN)) {
            oauth = Convert.fromJson(responseStr, OauthModel.class);
            SharedPreferencesUtils.put(Constants.ACCESS_TOKEN, oauth.getAccess_token());
            SharedPreferencesUtils.put(Constants.REFRESH_TOKEN, oauth.getRefresh_token());
            RxBusUtil.getInstance().post(TAG, 0);
            Log.i("oauth", oauth.toString());
        } else {
            ResponseModel responseModel = Convert.fromJson(responseStr, ResponseModel.class);
            runOnUiThread(() -> {
                ToastUtils.show(responseModel.msg);
                RxBusUtil.getInstance().post(TAG, 1);
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBusUtil.getInstance().unregister(this, observable);
    }
}
