package so.voc.vhome.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import cn.jpush.android.api.JPushInterface;
import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityUserInfoBinding;
import so.voc.vhome.jpush.PushService;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.StringCallback;
import so.voc.vhome.util.ActivityManager;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.BindingUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.SharedPreferencesUtils;
import so.voc.vhome.util.Util;
import so.voc.vhome.widget.SwitchButton;

/**
 * Fun：用户资料
 *
 * @Author：Linus_Xie
 * @Date：2019/6/5 16:22
 */
public class UserInfoActivity extends BaseActivity<ActivityUserInfoBinding> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.user_info));

        bindingView.sbMessagePush.setState(JPushInterface.isPushStopped(getApplicationContext()));
        bindingView.sbMessagePush.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                JPushInterface.resumePush(getApplicationContext());
                bindingView.sbMessagePush.setState(true);
            }

            @Override
            public void toggleToOff() {
                JPushInterface.stopPush(getApplicationContext());
                bindingView.sbMessagePush.setState(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindingView.tvUserName.setText(String.valueOf(SharedPreferencesUtils.get(Constants.USER_NAME, "")).isEmpty() ? "未设置" : String.valueOf(SharedPreferencesUtils.get(Constants.USER_NAME, "")));
        bindingView.tvPhoneNum.setText(Util.hidePhoneNum(String.valueOf(SharedPreferencesUtils.get(Constants.USER_PHONE, ""))));
        if (!String.valueOf(SharedPreferencesUtils.get(Constants.HEAD_URL, "")).isEmpty()){
            BindingUtil.loadImage(bindingView.civHeadIcon, String.valueOf(SharedPreferencesUtils.get(Constants.HEAD_URL, "")));
        }
    }

    @SuppressLint("MissingPermission")
    public void userClick(View view){
        switch (view.getId()){
            case R.id.ll_headIcon:
                ActivityUtil.goActivity(this, PersonalActivity.class);
                break;
            case R.id.ll_modifyPwd:
                ActivityUtil.goActivity(this, ModifyPwdActivity.class);
                break;
            case R.id.ll_contactService:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4001030377"));
                startActivity(intent);
                break;
            case R.id.ll_guideUse:

                break;
            case R.id.ll_aboutUs:

                break;
            case R.id.btn_logout:
                new MaterialDialog.Builder(this)
                        .title(CommonUtils.getString(R.string.tips))
                        .content(CommonUtils.getString(R.string.sure_logout))
                        .positiveText(CommonUtils.getString(R.string.confirm))
                        .positiveColor(CommonUtils.getColor(R.color.colorRed))
                        .negativeText(CommonUtils.getString(R.string.cancel))
                        .callback(new MaterialDialog.Callback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                logout();
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .build()
                        .show();
                break;
        }
    }

    private void logout() {
        OkGo.<ResponseModel>post(VHomeApi.LOGOUT)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .isSpliceUrl(true)
                .execute(new StringCallback<ResponseModel>() {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);

                    }
                });
        SharedPreferencesUtils.clear(this);
        ActivityManager.getInstance().finishAllActivity();
        ActivityUtil.goActivityAndFinish(UserInfoActivity.this, LoginActivity.class);
    }
}
