package so.voc.vhome.base;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.clj.fastble.data.BleDevice;
import com.hjq.toast.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import so.voc.vhome.R;
import so.voc.vhome.ble.BleDataUtil;
import so.voc.vhome.databinding.ActivityBaseBinding;
import so.voc.vhome.util.ActivityManager;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.RxBusUtil;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/5/31 16:51
 */
public abstract class BaseActivity <SV extends ViewDataBinding> extends AppCompatActivity {

    /**布局*/
    protected SV bindingView;
    protected ActivityBaseBinding mBaseBinding;

    private LinearLayout llLoad;
    private ImageView ivLoad;
    private TextView tvLoad;
    private TextView tvDeal;
    private LinearLayout llOffLine;

    protected Observable connectOb;

    protected <T extends View> T getView(int id) {
        return findViewById(id);
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base, null, false);
        bindingView = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);

        //content
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        RelativeLayout rlContainer = mBaseBinding.getRoot().findViewById(R.id.rl_container);
        rlContainer.addView(bindingView.getRoot());
        getWindow().setContentView(mBaseBinding.getRoot());

        connectOb = RxBusUtil.getInstance().register(BleDataUtil.TAG, Integer.class);

        ivLoad = getView(R.id.iv_load);
        tvLoad = getView(R.id.tv_load);
        tvDeal = getView(R.id.tv_deal);

        llOffLine = getView(R.id.ll_offLine);
        llLoad = getView(R.id.ll_load);

        setTitleBar();
        initView();

        llLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                onLoading();
            }
        });

        connectOb.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                switch (integer) {
                    case 0:
                        mBaseBinding.tvDeal.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                    case 3:
                        mBaseBinding.tvDeal.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        mBaseBinding.tvDeal.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    /**
     * 网络没有连接
     */
    protected void showOffLine() {
        if (!NetworkUtils.isConnected()){
            if (bindingView.getRoot().getVisibility() != View.GONE){
                bindingView.getRoot().setVisibility(View.GONE);
            }
            if (llOffLine.getVisibility() != View.VISIBLE){
                llOffLine.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 显示加载中的状态
     */
    protected void showLoading(){
        if (bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
        if (llLoad.getVisibility() != View.GONE) {
            llLoad.setVisibility(View.GONE);
        }
    }

    /**
     * 失败后点击刷新
     */
    protected void onLoading() {

    }

    /**
     * 加载完成的状态
     */
    protected void showContentView() {
        if (llLoad.getVisibility() != View.GONE) {
            llLoad.setVisibility(View.GONE);
        }
        if (bindingView.getRoot().getVisibility() != View.VISIBLE) {
            bindingView.getRoot().setVisibility(View.VISIBLE);
        }
    }

    protected void showNoContent(int i) {
        switch (i){
            case 0://共享设备
                tvLoad.setText(CommonUtils.getString(R.string.no_share_device));
                break;
            case 1://分享钥匙
                tvLoad.setText(CommonUtils.getString(R.string.no_share_key));
                break;
            case 2://报警记录
                tvLoad.setText(CommonUtils.getString(R.string.no_alarm_record));
                break;
            case 3://开门记录
                tvLoad.setText(CommonUtils.getString(R.string.no_open_record));
                break;
            case 4://消息记录
                tvLoad.setText(CommonUtils.getString(R.string.no_notify));
                break;
            case 5://消息记录
                tvLoad.setText(CommonUtils.getString(R.string.no_device));
                break;
        }
        if (llLoad.getVisibility() != View.VISIBLE) {
            llLoad.setVisibility(View.VISIBLE);
        }
        if (bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
    }

    /**
     * 初始化界面
     */
    protected abstract void initView();

    protected void setTitleBar(){
        mBaseBinding.llBack.setOnClickListener(v  -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void noTitle(){
        mBaseBinding.llTitle.setVisibility(View.GONE);
    }

    public void setDeal(CharSequence dealStr){
        mBaseBinding.tvDeal.setVisibility(View.VISIBLE);
        mBaseBinding.tvDeal.setText(dealStr);

    }

    @Override
    public void setTitle(CharSequence title) {
        mBaseBinding.tvTitle.setText(title);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //如果通知栏的权限被手动关闭了
        if (!ToastUtils.isNotificationEnabled(this) && "XToast".equals(ToastUtils.getToast().getClass().getSimpleName())){
            //因为吐司只有初始化的时候才会判断权限有没有开启，根据这个通知开关来显示原生的吐司还是兼容的吐司
            ToastUtils.init(getApplication());
            recreate();
            ToastUtils.show("检查到你手动关闭了通知权限，正在重新初始化框架，只有这样吐司才能正常显示出来");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishActivity(this);
        RxBusUtil.getInstance().unregister(this, connectOb);
    }
}
