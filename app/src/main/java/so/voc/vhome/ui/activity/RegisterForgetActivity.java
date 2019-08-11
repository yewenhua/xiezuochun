package so.voc.vhome.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.HashMap;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityRegisterForgetBinding;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.HudCallback;
import so.voc.vhome.okgo.JsonCallback;
import so.voc.vhome.okgo.StringCallback;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.SharedPreferencesUtils;
import so.voc.vhome.util.TimeCountUtil;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/1 15:05
 */
public class RegisterForgetActivity extends BaseActivity<ActivityRegisterForgetBinding> {

    private String type;
    private String phoneStr = "";
    private String verifyCodeStr = "";
    private String pwdStr = "";
    private String confirmPwdStr = "";
    private TimeCountUtil timeCountUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_forget);
    }

    @Override
    protected void initView() {
        Bundle bundle = this.getIntent().getExtras();
        type = bundle.getString("type");
        if (type.equals("register")) {
            setTitle(CommonUtils.getString(R.string.register));
            bindingView.setIsRegister(true);
        } else {
            setTitle(CommonUtils.getString(R.string.forget_pwd));
            bindingView.setIsRegister(false);
        }
        timeCountUtil = new TimeCountUtil(this, 60000, 1000, bindingView.btnVerifyCode);
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_verifyCode:
                phoneStr = bindingView.etPhone.getText().toString().trim();
                if (!RegexUtils.isMobileSimple(phoneStr)) {
                    ToastUtils.show(CommonUtils.getString(R.string.input_phone));
                    break;
                }
                timeCountUtil.start();
                getVerifyCode(phoneStr);
                break;
            case R.id.btn_submit:
                phoneStr = bindingView.etPhone.getText().toString().trim();
                verifyCodeStr = bindingView.etVerifyCode.getText().toString().trim();
                pwdStr = bindingView.etPwd.getText().toString().trim();
                confirmPwdStr = bindingView.etConfirmPwd.getText().toString().trim();
                if (!RegexUtils.isMobileSimple(phoneStr)) {
                    ToastUtils.show(CommonUtils.getString(R.string.input_phone));
                    break;
                }
                if (verifyCodeStr.isEmpty()) {
                    ToastUtils.show(CommonUtils.getString(R.string.input_verify_code));
                    break;
                }
                if (pwdStr.isEmpty()) {
                    ToastUtils.show(CommonUtils.getString(R.string.input_pwd));
                    break;
                }
                if (pwdStr.length() < 6) {
                    ToastUtils.show(CommonUtils.getString(R.string.pwd_length));
                    break;
                }
                if (!pwdStr.equals(confirmPwdStr)) {
                    ToastUtils.show(CommonUtils.getString(R.string.pwd_unequal));
                    break;
                }
                if (("register").equals(type)) {
                    register();
                } else {
                    resetPwd();
                }
                break;
        }
    }

    private void getVerifyCode(String phoneStr) {
        String url = "";
        if (("register").equals(type)) {
            url = VHomeApi.MESSAGE_REGISTER + phoneStr;
        } else {
            url = VHomeApi.MESSAGE_FORGET_PASSWORD + phoneStr;
        }
        OkGo.<ResponseModel>post(url)
                .tag(this)
                .execute(new StringCallback<ResponseModel>() {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        if (response.body().code == 0) {
                            ToastUtils.show(response.body().msg);
                        } else {
                            timeCountUtil.cancel();
                            timeCountUtil.onFinish();
                            ToastUtils.show(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<ResponseModel> response) {
                        super.onError(response);
                        timeCountUtil.cancel();
                        timeCountUtil.onFinish();
                        ToastUtils.show(response.body().msg);
                    }
                });
    }

    private void register() {
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", phoneStr);
        map.put("password", pwdStr);
        map.put("validateCode", verifyCodeStr);
        JSONObject jsonObject = new JSONObject(map);
        OkGo.<ResponseModel>post(VHomeApi.REGISTER)
                .tag(this)
                .upJson(jsonObject)
                .execute(new HudCallback<ResponseModel>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            ToastUtils.show(response.body().msg);
                            ActivityUtil.goActivityAndFinish(RegisterForgetActivity.this, LoginActivity.class);
                        }
                    }
                });
    }

    private void resetPwd() {
        HashMap<String, String> map = new HashMap<>();
        map.put("forgetPasswordType", "SHORT_MESSAGE");
        map.put("mobile", phoneStr);
        map.put("password", pwdStr);
        map.put("validateCode", verifyCodeStr);
        JSONObject jsonObject = new JSONObject(map);
        OkGo.<ResponseModel>post(VHomeApi.FORGET_PWD)
                .tag(this)
                .upJson(jsonObject)
                .execute(new HudCallback<ResponseModel>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            ToastUtils.show(response.body().msg);
                            ActivityUtil.goActivityAndFinish(RegisterForgetActivity.this, LoginActivity.class);
                        }
                    }
                });
    }
}
