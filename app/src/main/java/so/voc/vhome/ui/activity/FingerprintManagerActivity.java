package so.voc.vhome.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.bouncycastle.util.encoders.Hex;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import so.voc.vhome.R;
import so.voc.vhome.adapter.FingerprintChooseAdapter;
import so.voc.vhome.adapter.MemberListAdapter;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.ble.BleDataUtil;
import so.voc.vhome.ble.data.BleData;
import so.voc.vhome.databinding.ActivityFingerprintManageBinding;
import so.voc.vhome.model.BleDataModel;
import so.voc.vhome.model.FingerMemberModel;
import so.voc.vhome.model.FingerModel;
import so.voc.vhome.model.MemberModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.HudCallback;
import so.voc.vhome.okgo.StringCallback;
import so.voc.vhome.recycler.DividerDecoration;
import so.voc.vhome.recycler.SingleItemClickListener;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.DeviceListType;
import so.voc.vhome.util.HexUtil;
import so.voc.vhome.util.RxBusUtil;
import so.voc.vhome.util.SharedPreferencesUtils;

/**
 * Fun：指纹管理
 *
 * @Author：Linus_Xie
 * @Date：2019/6/10 15:04
 */
public class FingerprintManagerActivity extends BaseActivity<ActivityFingerprintManageBinding> {

    public static final String TAG = FingerprintManagerActivity.class.getSimpleName();

    private String id;
    private List<FingerMemberModel> memberModels;
    private FingerprintChooseAdapter mAdapter;
    private boolean isVisible = false;
    private String deviceMemberId = "";
    private BleData bleData;
    private Observable dataOb;
    private FingerModel fingerModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_manage);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.fingerprint_manage));
        bleData = new BleData();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            id = bundle.getString("id");
            fingerModel = (FingerModel) bundle.getSerializable("fingerModel");
            bindingView.etOwner.setText(fingerModel.getAlias() == null ? fingerModel.getMemberName() : fingerModel.getAlias());

        }
        memberModels = new ArrayList<>();
        bindingView.tvFingerNo.setText(CommonUtils.getString(R.string.finger_no) + id);
        mAdapter = new FingerprintChooseAdapter(this);
        DividerDecoration dividerDecoration = new DividerDecoration(this, DividerDecoration.VERTICAL_LIST);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        bindingView.rvMemberList.setLayoutManager(manager);
        bindingView.rvMemberList.addItemDecoration(dividerDecoration);
        bindingView.rvMemberList.addOnItemTouchListener(new SingleItemClickListener(bindingView.rvMemberList, new SingleItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                bindingView.etOwner.setText(memberModels.get(position).getMemberName());
                deviceMemberId = String.valueOf(memberModels.get(position).getMemberId());
            }
        }));
        getMemberList();

        dataOb = RxBusUtil.getInstance().register(TAG, BleDataModel.class);
        dataOb.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BleDataModel>() {
            @Override
            public void accept(BleDataModel bleDataModel) throws Exception {
                switch (bleDataModel.getCmd()){
                    case "8A":
                        if (HexUtil.checkByte(bleDataModel.getData(), bleData.none) || HexUtil.checkByte(bleDataModel.getData(), bleData.noNum)){
                            delFingerByNet();
                        }
                        break;
                }

            }
        });
    }

    private void getMemberList() {
        OkGo.<ResponseModel<List<FingerMemberModel>>>get(VHomeApi.DEVICE_WHITE_MEMBER_LIST + String.valueOf(SharedPreferencesUtils.get(Constants.DEVICE_ID, "")))
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .params("deviceWhiteListType", String.valueOf(DeviceListType.FINGER))
                .tag(this)
                .execute(new StringCallback<ResponseModel<List<FingerMemberModel>>>() {
                    @Override
                    public void onSuccess(Response<ResponseModel<List<FingerMemberModel>>> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            memberModels = response.body().data;
                            mAdapter.fillList(memberModels);
                            bindingView.rvMemberList.setAdapter(mAdapter);
                        } else {
                            ToastUtils.show(response.body().msg);
                        }
                    }
                });
    }

    public void manageClick(View view){
        switch (view.getId()){
            case R.id.btn_add:
                if (bindingView.etOwner.getText().toString().trim().isEmpty()){
                    ToastUtils.show(CommonUtils.getString(R.string.input_fingerprint_remark));
                    break;
                }
                if (fingerModel.getAlias() == null && fingerModel.getMemberName() == null){
                    fingerBind();
                } else {
                    fingerUpdate();
                }
                break;
            case R.id.ll_arrowList:
                if (isVisible){
                    bindingView.etOwner.setText("");
                    deviceMemberId = "";
                }
                isVisible = isVisible ? false : true;
                bindingView.llMemberList.setVisibility(isVisible ? View.VISIBLE : View.GONE);
                bindingView.ivArrowList.setBackgroundResource(isVisible ? R.drawable.ic_up : R.drawable.ic_down);

                break;
            case R.id.btn_delFinger:
                new MaterialDialog.Builder(FingerprintManagerActivity.this)
                        .title(CommonUtils.getString(R.string.tips))
                        .content(CommonUtils.getString(R.string.sure_del_finger))
                        .positiveText(CommonUtils.getString(R.string.confirm))
                        .positiveColor(CommonUtils.getColor(R.color.colorRed))
                        .negativeText(CommonUtils.getString(R.string.cancel))
                        .callback(new MaterialDialog.Callback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                delFingerByFirmware(id);
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

    /**
     * 软删除
     */
    private void delFingerByNet() {
        OkGo.<ResponseModel>post(VHomeApi.DEVICE_WHITE_DELETE + fingerModel.getId())
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .isSpliceUrl(true)
                .tag(this)
                .execute(new StringCallback<ResponseModel>() {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            ActivityUtil.goActivityAndFinish(FingerprintManagerActivity.this, FingerprintManageListActivity.class);
                        }
                    }
                });
    }


    /**
     * 硬删除指纹
     */
    private void delFingerByFirmware(String id) {
        BleDataUtil.getInstance().write(bleData.fingerDel(Integer.valueOf(id)));
    }

    /**
     * 指纹更新
     */
    private void fingerUpdate() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(fingerModel.getId()));
        if (deviceMemberId.isEmpty()){
            map.put("alias", bindingView.etOwner.getText().toString().trim());
        } else {
            map.put("memberId", deviceMemberId);
        }
        JSONObject jsonObject = new JSONObject(map);
        OkGo.<ResponseModel>post(VHomeApi.DEVICE_WHITE_UPDATE)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .isSpliceUrl(true)
                .tag(this)
                .upJson(jsonObject)
                .execute(new HudCallback<ResponseModel>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            ActivityUtil.goActivityAndFinish(FingerprintManagerActivity.this, FingerprintManageListActivity.class);
                        }
                    }
                });
    }

    /**
     * 指纹绑定
     */
    private void fingerBind() {
        HashMap<String, String> map = new HashMap<>();
        map.put("deviceId", String.valueOf(SharedPreferencesUtils.get(Constants.DEVICE_ID, "")));
        if (deviceMemberId.isEmpty()){
            map.put("alias", bindingView.etOwner.getText().toString().trim());
        } else {
            map.put("memberId", deviceMemberId);
        }
        map.put("deviceWhiteListType", String.valueOf(DeviceListType.FINGER));
        map.put("no", id);
        JSONObject jsonObject = new JSONObject(map);
        OkGo.<ResponseModel>post(VHomeApi.DEVICE_WHITE_BIND)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .isSpliceUrl(true)
                .tag(this)
                .upJson(jsonObject)
                .execute(new HudCallback<ResponseModel>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            ActivityUtil.goActivityAndFinish(FingerprintManagerActivity.this, FingerprintManageListActivity.class);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBusUtil.getInstance().unregister(TAG, dataOb);
    }
}
