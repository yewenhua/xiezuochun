package so.voc.vhome.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.hjq.toast.ToastUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lzy.okgo.OkGo;

import java.io.IOException;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
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
import so.voc.vhome.databinding.ActivityLoginBinding;
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
import so.voc.vhome.util.DialogUtil;
import so.voc.vhome.util.RxBusUtil;
import so.voc.vhome.util.SharedPreferencesUtils;
import so.voc.vhome.util.Util;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/5/31 18:19
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding> {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private Bundle bundle;
    private String phoneStr = "";
    private String pwdStr = "";
    private KProgressHUD hud;

    private Observable<Integer> observable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initView() {
        bundle = new Bundle();
        noTitle();
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(CommonUtils.getColor(R.color.colorBlack46))
                .setAnimationSpeed(1);
        requestAllPermission();

        observable = RxBusUtil.getInstance().register(TAG, Integer.class);
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                switch (integer) {
                    case 0:
//                        memberMessage();
                        getDeviceSelected();
                        break;

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
                                ActivityUtil.goActivityWithBundle(LoginActivity.this, MainActivity.class, bundle);
                            } else {
                                ActivityUtil.goActivityWithBundle(LoginActivity.this, Main01Activity.class, bundle);
                            }
                            finish();
                        } else {
                            hud.dismiss();
                            ToastUtils.show(response.body().msg);
                        }
                    }
                });
    }

    private void requestAllPermission() {
        PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.LOCATION, PermissionConstants.PHONE, PermissionConstants.STORAGE)
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        LogUtils.e(permissionsGranted);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        LogUtils.e(permissionsDeniedForever, permissionsDenied);
                        if (!permissionsDeniedForever.isEmpty()) {
                            DialogUtil.appSettingDialog(LoginActivity.this);
                        }
                    }
                }).request();
    }


    public void loginClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                phoneStr = bindingView.etPhone.getText().toString().trim();
                pwdStr = bindingView.etPwd.getText().toString().trim();
                if (!RegexUtils.isMobileSimple(phoneStr)) {
                    ToastUtils.show(CommonUtils.getString(R.string.input_phone));
                    break;
                }
                if (pwdStr.isEmpty()) {
                    ToastUtils.show(CommonUtils.getString(R.string.input_pwd));
                    break;
                }
                if (hud != null && !hud.isShowing()) {
                    hud.show();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            login();
                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(() -> {
                                ToastUtils.show(CommonUtils.getString(R.string.logon_exception));
                                hud.dismiss();
                            });
                        }
                    }
                }).start();
                break;
            case R.id.tv_register:
                bundle.putString("type", "register");
                ActivityUtil.goActivityWithBundle(this, RegisterForgetActivity.class, bundle);
                break;
            case R.id.tv_forgerPwd:
                bundle.putString("type", "forgetPwd");
                ActivityUtil.goActivityWithBundle(this, RegisterForgetActivity.class, bundle);
                break;
            case R.id.iv_wechat:
                break;
        }
    }

    public void login() throws IOException {
        OauthModel oauth = new OauthModel();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor("client_app", "123abc"))
                .build();
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "password")
                .add("scope", "all")
                .add("loginType", "APP_USERNAME_PASSWORD")
                .add("username", phoneStr)
                .add("password", pwdStr)
                .add("registrationId", JPushInterface.getRegistrationID(this))
                .build();
        Request request = new Request.Builder()
                .url(VHomeApi.AUTH_LOGIN)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("服务器端错误: " + response);
        }
        String responseStr = response.body().string();
        LogUtils.e(responseStr);
        if (responseStr.contains(Constants.ACCESS_TOKEN)) {
            oauth = Convert.fromJson(responseStr, OauthModel.class);
            SharedPreferencesUtils.put(Constants.ACCESS_TOKEN, oauth.getAccess_token());
            SharedPreferencesUtils.put(Constants.REFRESH_TOKEN, oauth.getRefresh_token());
            SharedPreferencesUtils.put(Constants.USER_NAME, Util.initNullStr(oauth.getUser_info().getName()));
            SharedPreferencesUtils.put(Constants.USER_PHONE, Util.initNullStr(oauth.getUser_info().getMobile()));
            if (!Util.initNullStr(oauth.getUser_info().getHeadIcon()).isEmpty()){
                SharedPreferencesUtils.put(Constants.HEAD_URL, VHomeApi.GET_HEAD_ICON
                        + oauth.getUser_info().getHeadIcon() + "?access_token="
                        + String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")));
            }
            getDeviceSelected();
            RxBusUtil.getInstance().post(TAG, 0);
            Log.i("oauth", oauth.toString());
        } else {
            ResponseModel responseModel = Convert.fromJson(responseStr, ResponseModel.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hud.dismiss();
                    ToastUtils.show(responseModel.msg);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBusUtil.getInstance().unregister(this, observable);
    }
}
