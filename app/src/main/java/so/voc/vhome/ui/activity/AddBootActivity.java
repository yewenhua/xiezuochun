package so.voc.vhome.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityAddBootBinding;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;

/**
 * Fun：添加设备引导确认页
 *
 * @Author：Linus_Xie
 * @Date：2019/6/11 9:53
 */
public class AddBootActivity extends BaseActivity<ActivityAddBootBinding> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_boot);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.add_device));
    }

    public void bleClick(View view){
        switch (view.getId()){
            case R.id.btn_ble:
                ActivityUtil.goActivity(this, BleListActivity.class);
                break;
        }
    }
}

