package so.voc.vhome.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.HashMap;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityEditBinding;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.HudCallback;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.SharedPreferencesUtils;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/25 10:18
 */
public class EditActivity extends BaseActivity<ActivityEditBinding> {

    private String type = "";
    private String value = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }

    @Override
    protected void initView() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            type = bundle.getString("type");
            value = bundle.getString("value");
        }
        bindingView.etEdit.setText(value);
        switch (type){
            case "nickName":
                setTitle(CommonUtils.getString(R.string.modify_nick_name));
                break;
            case "lockName":
                setTitle(CommonUtils.getString(R.string.info_lock_name));
                break;
        }
    }

    public void click(View view){
        switch (view.getId()){
            case R.id.btn_submit:
                if (bindingView.etEdit.getText().toString().trim().isEmpty()){
                    ToastUtils.show(CommonUtils.getString(R.string.info_empty));
                    break;
                } else if (bindingView.etEdit.getText().toString().trim().equals(value)){
                    finish();
                } else {
                    switch (type) {
                        case "nickName":
                            modifyMember();
                            break;
                        case "lockName":
                            deviceUpdate();
                            break;
                    }
                }
                break;
        }
    }

    /**
     * 修改个人昵称
     */
    private void modifyMember() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", bindingView.etEdit.getText().toString().trim());
        JSONObject jsonObject = new JSONObject(map);
        OkGo.<ResponseModel>post(VHomeApi.MODIFY_MEMBER)
                .tag(this)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .isSpliceUrl(true)
                .upJson(jsonObject)
                .execute(new HudCallback<ResponseModel>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            Bundle bundle = new Bundle();
                            bundle.putString("value", bindingView.etEdit.getText().toString().trim());
                            Intent intent = new Intent();
                            intent.putExtras(bundle);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
    }

    /**
     * 修改设备名称
     */
    private void deviceUpdate() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(SharedPreferencesUtils.get(Constants.DEVICE_ID, "")));
        map.put("name", bindingView.etEdit.getText().toString().trim());
        JSONObject jsonObject = new JSONObject(map);
        OkGo.<ResponseModel>post(VHomeApi.DEVICE_UPDATE)
                .tag(this)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .isSpliceUrl(true)
                .upJson(jsonObject)
                .execute(new HudCallback<ResponseModel>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            Bundle bundle = new Bundle();
                            bundle.putString("value", bindingView.etEdit.getText().toString().trim());
                            Intent intent = new Intent();
                            intent.putExtras(bundle);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
    }
}
