package so.voc.vhome.okgo;

import android.app.Activity;

import com.blankj.utilcode.util.LogUtils;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.model.Response;

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
 * @Date：2019/5/1 9:02
 */
public abstract class StringCallback<T> extends JsonCallback<T> {


    public StringCallback(){
        super();
    }

    @Override
    public void onSuccess(Response<T> response) {
        ResponseModel responseModel = (ResponseModel) response.body();
//        if (Constants.ERROR.equals(responseModel.code)){
//            ToastUtil.showToast(responseModel.content);
//        }
    }

    @Override
    public void onError(Response<T> response) {
        super.onError(response);

    }
}

