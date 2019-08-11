package so.voc.vhome.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import so.voc.vhome.R;
import so.voc.vhome.adapter.DeviceAdapter;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityDeviceListBinding;
import so.voc.vhome.model.DeviceModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.StringCallback;
import so.voc.vhome.recycler.DividerDecoration;
import so.voc.vhome.recycler.SingleItemClickListener;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.RxBusUtil;
import so.voc.vhome.util.SharedPreferencesUtils;
import so.voc.vhome.util.Util;

/**
 * Fun：设备列表
 *
 * @Author：Linus_Xie
 * @Date：2019/6/12 16:35
 */
public class DeviceListActivity extends BaseActivity<ActivityDeviceListBinding> implements OnRefreshListener {

    public static final String TAG = DeviceListActivity.class.getSimpleName();

    private DeviceAdapter mAdapter;
    private List<DeviceModel> deviceModels;
    private Observable ob;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        bindingView.setHasDevice(true);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.device_list));
        deviceModels = new ArrayList<>();
        mAdapter = new DeviceAdapter(this);
        DividerDecoration dividerDecoration = new DividerDecoration(this, DividerDecoration.VERTICAL_LIST);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        bindingView.rvDevice.setLayoutManager(manager);
        bindingView.rvDevice.addItemDecoration(dividerDecoration);
        bindingView.rvDevice.addOnItemTouchListener(new SingleItemClickListener(bindingView.rvDevice, new SingleItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                deviceMemberSelect(deviceModels.get(position).getId());
                if (Util.currentVersionCode().equals("2")) {
                    RxBusUtil.getInstance().post(MainActivity.TAG, deviceModels.get(position));
                    ActivityUtil.goActivityAndFinish(DeviceListActivity.this, MainActivity.class);
                } else {
                    RxBusUtil.getInstance().post(Main01Activity.TAG, deviceModels.get(position));
                    ActivityUtil.goActivityAndFinish(DeviceListActivity.this, Main01Activity.class);
                }
            }
        }));
        bindingView.srlDevice.setOnRefreshListener(this);
        bindingView.srlDevice.autoRefresh();

        ob = RxBusUtil.getInstance().register(TAG, Integer.class);
        ob.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                switch (integer) {
                    case 0:
                        getDeviceList();
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        RxBusUtil.getInstance().post("onBackPressed", 0);
        if (Util.currentVersionCode().equals("2")) {
            ActivityUtil.goActivityAndFinish(DeviceListActivity.this, MainActivity.class);
        } else {
            ActivityUtil.goActivityAndFinish(DeviceListActivity.this, Main01Activity.class);
        }

    }

    private void deviceMemberSelect(int id) {
        OkGo.<ResponseModel>post(VHomeApi.DEVICE_MEMBER_SELECT + id)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .isSpliceUrl(true)
                .execute(new StringCallback<ResponseModel>() {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        LogUtils.e(VHomeApi.DEVICE_MEMBER_SELECT, response.body().msg);
                        super.onSuccess(response);
                    }
                });
    }

    private void getDeviceList() {
        OkGo.<ResponseModel<List<DeviceModel>>>get(VHomeApi.DEVICE_MEMBER_MINE)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .tag(this)
                .execute(new StringCallback<ResponseModel<List<DeviceModel>>>() {
                    @Override
                    public void onSuccess(Response<ResponseModel<List<DeviceModel>>> response) {
                        super.onSuccess(response);
                        LogUtils.e(VHomeApi.DEVICE_MEMBER_MINE, response.body().msg);
                        if (response.body().code == 0) {
                            bindingView.srlDevice.finishRefresh();
                            deviceModels = response.body().data;
                            if (deviceModels.size() > 0) {
                                bindingView.setHasDevice(true);
                                mAdapter.fillList(deviceModels);
                                bindingView.rvDevice.setAdapter(mAdapter);
                            } else {
                                //TODO 没有设备，添加设备
                                bindingView.setHasDevice(false);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<ResponseModel<List<DeviceModel>>> response) {
                        super.onError(response);
                        bindingView.srlDevice.finishRefresh();
                    }
                });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getDeviceList();
    }

    public void addDeviceClick(View view){
        switch (view.getId()){
            case R.id.btn_addDevice:
                ActivityUtil.goActivity(this, AddDeviceActivity.class);
                break;
        }
    }
}
