package so.voc.vhome.okgo;

import android.app.Activity;

import com.blankj.utilcode.util.LogUtils;
import com.hjq.toast.ToastUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import so.voc.vhome.R;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.ui.activity.LoginActivity;
import so.voc.vhome.util.ActivityManager;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/5/1 8:55
 */
public abstract class HudCallback<T> extends JsonCallback<T> {

    private KProgressHUD hud;
    private Activity activity;

    public HudCallback(Activity activity){
        super();
        initHud(activity);
        this.activity = activity;
    }

    public void initHud(Activity activity){
        hud = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(CommonUtils.getColor(R.color.colorBlack46))
                .setAnimationSpeed(1);
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        if (hud != null && !hud.isShowing()){
            hud.show();
        }
    }

    @Override
    public void onSuccess(Response<T> response) {
        ResponseModel responseModel = (ResponseModel) response.body();
        LogUtils.e(responseModel);
        ToastUtils.show(responseModel.msg);
    }

    @Override
    public void onError(Response<T> response) {
        super.onError(response);
        if (hud != null && hud.isShowing()){
            hud.dismiss();
        }
        if (response.code() == 401){
            ToastUtils.show(CommonUtils.getString(R.string.logon_invalidation));
            ActivityUtil.goActivityAndFinish(activity, LoginActivity.class);
            ActivityManager.getInstance().finishAllActivity();
        } else {
            ToastUtils.show(response.getException().getMessage());
        }
    }

    @Override
    public void onFinish() {
        //网络请求结束之后，关闭hud
        if (hud != null && hud.isShowing()){
            hud.dismiss();
        }
    }
}
