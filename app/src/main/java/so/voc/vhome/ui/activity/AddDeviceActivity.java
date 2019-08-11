package so.voc.vhome.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.king.zxing.CaptureActivity;
import com.king.zxing.Intents;

import java.util.List;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityAddDeviceBinding;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.DialogUtil;
import so.voc.vhome.widget.AddDevicePop;

/**
 * Fun：添加设备
 *
 * @Author：Linus_Xie
 * @Date：2019/6/10 9:04
 */
public class AddDeviceActivity extends BaseActivity<ActivityAddDeviceBinding> implements AddDevicePop.OnAddDevicePopClickListener{

    private AddDevicePop addDevicePop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.add_device));
        addDevicePop = new AddDevicePop(bindingView.llAddManual, this);
        addDevicePop.setOnAddDevicePopClickListener(this);
    }

    public void addDeviceClick(View view){
        switch (view.getId()){
            case R.id.ll_addScan:
                PermissionUtils.permission(PermissionConstants.CAMERA)
                        .callback(new PermissionUtils.FullCallback() {
                            @Override
                            public void onGranted(List<String> permissionsGranted) {
                                ActivityUtil.goActivityForResult(AddDeviceActivity.this, CaptureActivity.class, Constants.RESULT_CODE);
                            }

                            @Override
                            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                                DialogUtil.appSettingDialog(AddDeviceActivity.this);
                            }
                        }).request();
                break;
            case R.id.ll_addManual:
                addDevicePop.showPopupWindow();
                break;
        }
    }

    @Override
    public void onAddDevicePop(int position) {
        switch (position){
            case 0:

                break;
            case 1:
                ActivityUtil.goActivity(this, AddBootActivity.class);
                break;
            case 2:

                break;
            case 3:

                break;
        }
        addDevicePop.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK && data != null){
            switch (requestCode){
                case Constants.RESULT_CODE:
                    String result = data.getStringExtra(Intents.Scan.RESULT);

                    break;
            }
        }
    }
}
