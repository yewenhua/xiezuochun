package so.voc.vhome.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.clj.fastble.BleManager;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import so.voc.vhome.R;
import so.voc.vhome.adapter.FingerprintManageAdapter;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.ble.BleDataUtil;
import so.voc.vhome.ble.data.BleData;
import so.voc.vhome.databinding.ActivityFingerprintManageListBinding;
import so.voc.vhome.model.BleDataModel;
import so.voc.vhome.model.FingerModel;
import so.voc.vhome.model.FingerModel_;
import so.voc.vhome.model.MemberDetailModel;
import so.voc.vhome.model.RelatedMemberModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.StringCallback;
import so.voc.vhome.recycler.DividerDecoration;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.DeviceListLevel;
import so.voc.vhome.util.DeviceListType;
import so.voc.vhome.util.HexUtil;
import so.voc.vhome.util.ObjectBox;
import so.voc.vhome.util.RxBusUtil;
import so.voc.vhome.util.SharedPreferencesUtils;
import so.voc.vhome.util.Util;

/**
 * Fun：指纹管理
 *
 * @Author：Linus_Xie
 * @Date：2019/6/5 14:58
 */
public class FingerprintManageListActivity extends BaseActivity<ActivityFingerprintManageListBinding> implements OnRefreshListener {

    public static final String TAG = FingerprintManageListActivity.class.getSimpleName();

    private BleData bleData;
    private List<FingerModel> fingerModels;
    private FingerprintManageAdapter mAdapter;

    private Observable dataOb;

    private OptionsPickerView fingerTypeView;
    private String fingerTypes[] = {"所有指纹", "管理指纹", "主人指纹", "客人指纹"};
    private OptionsPickerView userView;
    private List<RelatedMemberModel> relatedMemberModels;

    private String deviceWhiteListLevel = "";
    private String memberId = "";
    private String alias = "";

    private Box<FingerModel> fingerModelBox;

    private MemberDetailModel memberDetailModel;
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_manage_list);
        fingerModelBox = ObjectBox.get().boxFor(FingerModel.class);
        bundle = this.getIntent().getExtras();
    }

    /**
     * 获取设备所有用户，用于筛选
     */
    private void getAllMembers() {
        OkGo.<ResponseModel<List<RelatedMemberModel>>>get(VHomeApi.DEVICE_WHITE_ALL_MEMBERS + String.valueOf(SharedPreferencesUtils.get(Constants.DEVICE_ID, "")))
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .params("deviceWhiteListType", String.valueOf(DeviceListType.FINGER))
                .params("deviceWhiteListLevel", "")
                .tag(this)
                .execute(new StringCallback<ResponseModel<List<RelatedMemberModel>>>() {
                    @Override
                    public void onSuccess(Response<ResponseModel<List<RelatedMemberModel>>> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0) {
                            relatedMemberModels = new ArrayList<>();
                            RelatedMemberModel relatedMemberModel = new RelatedMemberModel();
                            relatedMemberModel.setAlias("所有用户");
                            relatedMemberModels.add(relatedMemberModel);
                            relatedMemberModels.addAll(response.body().data);
                        }
                    }
                });
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.fingerprint_manage));
        bleData = new BleData();
        mAdapter = new FingerprintManageAdapter(this);

        dataOb = RxBusUtil.getInstance().register(TAG, BleDataModel.class);
        dataOb.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BleDataModel>() {
            @Override
            public void accept(BleDataModel bleDataModel) throws Exception {
                switch (bleDataModel.getCmd()) {
                    case "94":
                        byte[] state = HexUtil.getByteArrayByLength(bleDataModel.getData(), 0, 1);
                        if (HexUtil.checkByte(state, bleData.none)) {
                            int count = Integer.valueOf(HexUtil.getByteArrayByLength(bleDataModel.getData(), 1, 1)[0]);
                            byte[] finger = HexUtil.getByteArrayByLength(bleDataModel.getData(), 2, count);
                            dealFiner(finger);
                        }
                        break;
                }

            }
        });

        DividerDecoration dividerDecoration = new DividerDecoration(this, DividerDecoration.VERTICAL_LIST);
        LinearLayoutManager manager = new LinearLayoutManager(this);

        bindingView.rvFingerprint.setLayoutManager(manager);
        bindingView.rvFingerprint.addItemDecoration(dividerDecoration);
        mAdapter.setOnFingerClickListener(new FingerprintManageAdapter.OnFingerClickListener() {

            @Override
            public void onFingerClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(fingerModels.get(position).getNo()));
                bundle.putSerializable("fingerModel", fingerModels.get(position));
                ActivityUtil.goActivityWithBundle(FingerprintManageListActivity.this, FingerprintManagerActivity.class, bundle);
            }
        });
        bindingView.rvFingerprint.setAdapter(mAdapter);
        bindingView.srlFingerprint.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllMembers();
        if (bundle != null) {
            memberDetailModel = (MemberDetailModel) bundle.getSerializable("memberDetailModel");
            bindingView.tvUser.setText(memberDetailModel.getMemberName());
            fingerModelBox.removeAll();
            getMembersList("", String.valueOf(memberDetailModel.getMemberId()), "");
        } else {
            bindingView.srlFingerprint.autoRefresh();
            if (BleManager.getInstance().isConnected(String.valueOf(SharedPreferencesUtils.get(Constants.BLE_MAC, "")))) {
                BleDataUtil.getInstance().notifyData();
                BleDataUtil.getInstance().write(bleData.obtainFinger());
            } else {
                setDeal(CommonUtils.getString(R.string.connect_device));
                mBaseBinding.tvDeal.setOnClickListener(v -> {
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter.isEnabled()) {
                        ToastUtils.show(CommonUtils.getString(R.string.reconnect));
                        BleDataUtil.getInstance().connectBle();
                    } else {
                        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
                        bluetoothAdapter = bluetoothManager.getAdapter();
                        //打开蓝牙权限
                        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BLE);
                        }
                    }
                });
            }
        }
    }

    /**
     * 请求指纹列表
     *
     * @param deviceWhiteListLevel 指纹类型
     * @param memberId
     * @param alias
     */
    private void getMembersList(String deviceWhiteListLevel, String memberId, String alias) {
        if (!BleManager.getInstance().isConnected(String.valueOf(SharedPreferencesUtils.get(Constants.BLE_MAC, "")))) {
            fingerModelBox.removeAll();
        }
        OkGo.<ResponseModel<List<FingerModel>>>get(VHomeApi.DEVICE_WHITE_LIST)
                .tag(this)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .params("deviceId", String.valueOf(SharedPreferencesUtils.get(Constants.DEVICE_ID, "")))
                .params("deviceWhiteListType", String.valueOf(DeviceListType.FINGER))
                .params("deviceWhiteListLevel", deviceWhiteListLevel)
                .params("memberId", memberId)
                .params("alias", alias)
                .execute(new StringCallback<ResponseModel<List<FingerModel>>>() {
                    @Override
                    public void onSuccess(Response<ResponseModel<List<FingerModel>>> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0) {
                            fingerModels = response.body().data;
                            if (BleManager.getInstance().isConnected(String.valueOf(SharedPreferencesUtils.get(Constants.BLE_MAC, "")))) {
                                for (FingerModel fingerModel : fingerModels) {
                                    List<FingerModel> fingerModelList = fingerModelBox.query().equal(FingerModel_.no, fingerModel.getNo()).build().find();
                                    if (fingerModelList.size() > 0) {
                                        fingerModelBox.put(fingerModel);
                                    } else {
                                        delFinger(fingerModel.getId());
                                    }
                                }
                                fingerModels = fingerModelBox.getAll();
                            }

                            sort();
                            bindingView.srlFingerprint.finishRefresh();
                            if (fingerModels.size() > 0) {
                                bindingView.srlFingerprint.setVisibility(View.VISIBLE);
                                bindingView.llNoData.setVisibility(View.GONE);
                                mAdapter.fillList(fingerModels);
                            } else {
                                bindingView.srlFingerprint.setVisibility(View.GONE);
                                bindingView.llNoData.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    private void delFinger(int id) {
        OkGo.<ResponseModel>post(VHomeApi.DEVICE_WHITE_DELETE + id)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .isSpliceUrl(true)
                .tag(this)
                .execute(new StringCallback<ResponseModel>() {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);

                    }
                });
    }

    /**
     * 根据指纹编号排序
     */
    private void sort() {
        Collections.sort(fingerModels, new Comparator<FingerModel>() {

            @Override
            public int compare(FingerModel t1, FingerModel t2) {
                if (t1.getNo() > t2.getNo()) {
                    return 1;
                }
                if (t1.getNo() < t2.getNo()) {
                    return -1;
                }
                return 0;
            }
        });
    }

    /**
     * 解析指纹
     *
     * @param finger 指纹编号
     */
    private void dealFiner(byte[] finger) {
        fingerModelBox.removeAll();
        fingerModels = new ArrayList<>();
        for (byte b : finger) {
            FingerModel fingerModel = new FingerModel();
            fingerModel.setNo(Long.valueOf(b));
            fingerModels.add(fingerModel);
        }
        fingerModelBox.put(fingerModels);
        getMembersList("", "", "");
    }

    public void fingerClick(View view) {
        switch (view.getId()) {
            case R.id.btn_addFingerprint:
                new MaterialDialog.Builder(this)
                        .title("添加指纹编号")
                        .positiveText(R.string.accept)
                        .customView(R.layout.dialog_add_finger)
                        .positiveText(CommonUtils.getString(R.string.confirm))
                        .positiveColor(CommonUtils.getColor(R.color.colorRed))
                        .negativeText(CommonUtils.getString(R.string.cancel))
                        .callback(new MaterialDialog.Callback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                EditText etFingerNo = dialog.findViewById(R.id.et_fingerNo);
                                if (etFingerNo.getText().toString().trim().isEmpty()) {
                                    ToastUtils.show(CommonUtils.getString(R.string.input_finger_no));
                                    return;
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("fingerNo", etFingerNo.getText().toString().trim());
                                    ActivityUtil.goActivityWithBundle(FingerprintManageListActivity.this, AddFingerprintActivity.class, bundle);
                                    dialog.dismiss();
                                }
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

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (BleManager.getInstance().isConnected(String.valueOf(SharedPreferencesUtils.get(Constants.BLE_MAC, "")))) {
            BleDataUtil.getInstance().write(bleData.obtainFinger());
        } else {
            getMembersList("", "", "");
        }
    }

    public void manageClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fingerType:
                fingerTypeView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        switch (options1) {
                            case 0:
                                deviceWhiteListLevel = "";
                                break;
                            case 1:
                                deviceWhiteListLevel = String.valueOf(DeviceListLevel.MANAGER);
                                break;
                            case 2:
                                deviceWhiteListLevel = String.valueOf(DeviceListLevel.HOST);
                                break;
                            case 3:
                                deviceWhiteListLevel = String.valueOf(DeviceListLevel.GUEST);
                                break;
                        }
                        fingerModelBox.removeAll();
                        getMembersList(deviceWhiteListLevel, memberId, alias);
                        bindingView.tvFingerType.setText(fingerTypes[options1]);
                    }
                }).setOutSideCancelable(false)
                        .setLayoutRes(R.layout.layout_pickerview_time, new CustomListener() {
                            @Override
                            public void customLayout(View v) {
                                final TextView tvCancel = v.findViewById(R.id.tv_cancel);
                                final TextView tvConfirm = v.findViewById(R.id.tv_confirm);
                                tvCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        fingerTypeView.dismiss();
                                    }
                                });
                                tvConfirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        fingerTypeView.returnData();
                                        fingerTypeView.dismiss();
                                    }
                                });
                            }
                        })
                        .build();
                fingerTypeView.setPicker(Arrays.asList(fingerTypes));
                fingerTypeView.show();
                break;
            case R.id.ll_user:
                if (BleManager.getInstance().isConnected(String.valueOf(SharedPreferencesUtils.get(Constants.BLE_MAC, "")))) {
                    BleDataUtil.getInstance().notifyData();
                    BleDataUtil.getInstance().write(bleData.obtainFinger());
                } else {
                    setDeal(CommonUtils.getString(R.string.connect_device));
                    mBaseBinding.tvDeal.setOnClickListener(v -> {
                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (bluetoothAdapter.isEnabled()) {
                            ToastUtils.show(CommonUtils.getString(R.string.reconnect));
                            BleDataUtil.getInstance().connectBle();
                        } else {
                            final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
                            bluetoothAdapter = bluetoothManager.getAdapter();
                            //打开蓝牙权限
                            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BLE);
                            }
                        }
                    });
                }
                userView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        memberId = Util.initNullStr(relatedMemberModels.get(options1).getMemberId());
                        alias = Util.initNullStr(relatedMemberModels.get(options1).getAlias());//这边alias用于展示
                        String memberName = Util.initNullStr(relatedMemberModels.get(options1).getMemberName());
                        bindingView.tvUser.setText(alias.isEmpty() ? memberName : alias);
                        alias = options1 == 0 ? "" : alias;//这边用于复制
                        fingerModelBox.removeAll();
                        getMembersList(deviceWhiteListLevel, memberId, alias);
                    }
                }).setOutSideCancelable(false)
                        .setLayoutRes(R.layout.layout_pickerview_time, new CustomListener() {
                            @Override
                            public void customLayout(View v) {
                                final TextView tvCancel = v.findViewById(R.id.tv_cancel);
                                final TextView tvConfirm = v.findViewById(R.id.tv_confirm);
                                tvCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        userView.dismiss();
                                    }
                                });
                                tvConfirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        userView.returnData();
                                        userView.dismiss();
                                    }
                                });
                            }
                        })
                        .build();
                userView.setPicker(relatedMemberModels);
                userView.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBusUtil.getInstance().unregister(TAG, dataOb);
    }
}
