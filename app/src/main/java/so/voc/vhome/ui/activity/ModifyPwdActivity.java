package so.voc.vhome.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.HashMap;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityModifyPwdBinding;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.HudCallback;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.SharedPreferencesUtils;

/**
 * Fun：修改密码
 *
 * @Author：Linus_Xie
 * @Date：2019/6/5 17:18
 */
public class ModifyPwdActivity extends BaseActivity<ActivityModifyPwdBinding> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.modify_pwd));
    }

    public void modifyClick(View view){
        switch (view.getId()){
            case R.id.btn_modify:
                String originalPwd = bindingView.etOriginalPwd.getText().toString().trim();
                String newPwd = bindingView.etPwd.getText().toString().trim();
                String confirmPwd = bindingView.etConfirmPwd.getText().toString().trim();
                if (originalPwd.isEmpty()){
                    ToastUtils.show(CommonUtils.getString(R.string.input_original_pwd));
                    return;
                }
                if (newPwd.isEmpty()){
                    ToastUtils.show(CommonUtils.getString(R.string.input_new_pwd));
                    return;
                }
                if (newPwd.length() < 6 ){
                    ToastUtils.show(CommonUtils.getString(R.string.pwd_length));
                    return;
                }
                if (!newPwd.equals(confirmPwd)){
                    ToastUtils.show(CommonUtils.getString(R.string.pwd_unequal));
                    return;
                }
                modifyPassword(newPwd, originalPwd);
                break;
        }
    }

    private void modifyPassword(String newPwd, String originalPwd){
        HashMap<String, String> map = new HashMap<>();
        map.put("newPassword", newPwd);
        map.put("originalPassword", originalPwd);
        JSONObject jsonObject = new JSONObject(map);
        OkGo.<ResponseModel>post(VHomeApi.MODIFY_PASSWORD)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .isSpliceUrl(true)
                .upJson(jsonObject.toString())
                .tag(this)
                .execute(new HudCallback<ResponseModel>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            SharedPreferencesUtils.put(Constants.ACCESS_TOKEN, "");
                            ToastUtils.show(response.body().msg);
                            ActivityUtil.goActivityAndFinish(ModifyPwdActivity.this, LoginActivity.class);
                        }
                    }
                });
    }
}
