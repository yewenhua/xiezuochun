package so.voc.vhome.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityDeviceInfoBinding;
import so.voc.vhome.model.DeviceModel;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.SharedPreferencesUtils;

/**
 * Fun：设备信息
 *
 * @Author：Linus_Xie
 * @Date：2019/6/17 11:35
 */
public class DeviceInfoActivity extends BaseActivity<ActivityDeviceInfoBinding> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.device_info));
        bindingView.tvLockName.setText(String.valueOf(SharedPreferencesUtils.get(Constants.DEVICE_NAME, "")));
        bindingView.setIsAdmin(String.valueOf(SharedPreferencesUtils.get(Constants.AUTHORITY_NAME, "")).equals(Constants.OWNER));
    }

    public void infoClick(View view){
        switch (view.getId()){
            case R.id.ll_lockName:
                Bundle bundle = new Bundle();
                bundle.putString("type", "lockName");
                bundle.putString("value", String.valueOf(SharedPreferencesUtils.get(Constants.DEVICE_NAME, "")));
                ActivityUtil.goActivityForResultWithBundle(this, EditActivity.class, bundle, Constants.EDIT_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.EDIT_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                String value = bundle.getString("value");
                SharedPreferencesUtils.put(Constants.DEVICE_NAME, value);
                bindingView.tvLockName.setText(value);
            }
        }
    }
}
