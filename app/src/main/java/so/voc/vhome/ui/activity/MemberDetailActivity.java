package so.voc.vhome.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityMemberDetailBinding;
import so.voc.vhome.model.DeviceShareModel;
import so.voc.vhome.model.MemberDetailModel;
import so.voc.vhome.model.MemberModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.HudCallback;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.AppDateUtil;
import so.voc.vhome.util.BindingUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.Convert;
import so.voc.vhome.util.RxBusUtil;
import so.voc.vhome.util.SharedPreferencesUtils;
import so.voc.vhome.util.Util;
import so.voc.vhome.widget.SwitchButton;

/**
 * Fun：成员详情
 * isAdmin为true则是管理员
 *
 * @Author：Linus_Xie
 * @Date：2019/6/10 11:39
 */
public class MemberDetailActivity extends BaseActivity<ActivityMemberDetailBinding> {

    private MemberDetailModel memberDetailModel;
    private MemberModel memberModel;
    private String content = "";
    private boolean isLimit = false;
    private TimePickerView startTimeView;
    private TimePickerView endTimeView;
    private OptionsPickerView unlockNumView;
    private List<String> unlockNums = new ArrayList<>();
    private int unLockNum = 0;//开锁次数，默认为0，不限制
    private Bundle bundle = new Bundle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.member_detail));
        Bundle bundle = this.getIntent().getExtras();
        memberModel = (MemberModel) bundle.getSerializable("memberModel");
        bindingView.setIsAdmin(memberModel.getDeviceMemberAuthority().equals(Constants.OWNER));
        if (!Util.initNullStr(memberModel.getMemberHeadIcon()).isEmpty()){
            BindingUtil.loadImage(bindingView.civHeadIcon, VHomeApi.GET_HEAD_ICON
                    + memberModel.getMemberHeadIcon() + "?access_token="
                    + String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")));
        }
        //自己是锁的成员，按钮名称改变，操作逻辑改变
        if (String.valueOf(SharedPreferencesUtils.get(Constants.AUTHORITY_NAME, "")).equals(Constants.OWNER)){
            if (memberModel.getDeviceMemberAuthority().equals(Constants.OWNER)){
                bindingView.btnOperate.setText(CommonUtils.getString(R.string.transfer_admin));
            } else {
                bindingView.btnOperate.setText(CommonUtils.getString(R.string.del_member));
                content = CommonUtils.getString(R.string.sure_del_member);
            }
        } else {
            bindingView.btnOperate.setText(CommonUtils.getString(R.string.del_device));
            content = CommonUtils.getString(R.string.sure_del_device);
        }
        memberDetail(memberModel.getId());

        bindingView.sbOpenTime.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                isLimit = true;
                bindingView.llOpenTime.setVisibility(isLimit ? View.VISIBLE : View.GONE);
                bindingView.sbOpenTime.setState(isLimit);
            }

            @Override
            public void toggleToOff() {
                isLimit = false;
                bindingView.llOpenTime.setVisibility(isLimit ? View.VISIBLE : View.GONE);
                bindingView.sbOpenTime.setState(isLimit);
            }
        });
        //设置开门次数
        setUnlockNum();
    }

    private void setUnlockNum() {
        for (int i = 0; i < 51; i++){
            unlockNums.add(i == 0 ? "不限制" : String.valueOf(i));
        }
    }

    /**
     * 获取成员的详情
     */
    private void memberDetail(int id) {
        OkGo.<ResponseModel<MemberDetailModel>>get(VHomeApi.DEVICE_MEMBER_DETAIL + id)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .tag(this)
                .execute(new HudCallback<ResponseModel<MemberDetailModel>>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel<MemberDetailModel>> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            memberDetailModel = response.body().data;
                            bindingView.setMemberDetailModel(memberDetailModel);
                            uiState(memberDetailModel);
                        }
                    }
                });
    }

    /**
     * 根据不同的人员显示不同的界面操作方式
     */
    private void uiState(MemberDetailModel memberDetailModel) {
        isLimit = memberDetailModel.getDeviceMemberDateConstraintType().equals("CONSTRAINT") ?  true : false;
        bindingView.llOpenTime.setVisibility(isLimit ? View.VISIBLE : View.GONE);
        //先考虑是不是管理员进来的
        if (SharedPreferencesUtils.get(Constants.AUTHORITY_NAME, "").equals(Constants.OWNER)){
            //再看选择的对象是不是管理员
            bindingView.llShareSetting.setVisibility(memberModel.getDeviceMemberAuthority().equals(Constants.OWNER) ? View.GONE : View.VISIBLE);
            bindingView.btnSave.setVisibility(memberModel.getDeviceMemberAuthority().equals(Constants.OWNER) ? View.GONE : View.VISIBLE);
            bindingView.sbOpenTime.setState(memberDetailModel.getDeviceMemberDateConstraintType().equals("NONE") ? false : true);
        } else {
            bindingView.llFinger.setClickable(false);
            bindingView.ivFinger.setVisibility(View.GONE);
            bindingView.llCard.setClickable(false);
            bindingView.ivCards.setVisibility(View.GONE);
            bindingView.llSbOpen.setVisibility(View.GONE);
            bindingView.llStartTime.setClickable(false);
            bindingView.ivStartTime.setVisibility(View.GONE);
            bindingView.llEndTime.setClickable(false);
            bindingView.ivEndTime.setVisibility(View.GONE);
            bindingView.llUnlockNum.setClickable(false);
            bindingView.ivUnlockNum.setVisibility(View.GONE);
            bindingView.llOpenTime.setVisibility(memberDetailModel.getDeviceMemberDateConstraintType().equals("NONE") ? View.GONE : View.VISIBLE);
            bindingView.btnSave.setVisibility(View.GONE);
        }

    }

    public void memberClick(View view){
        switch (view.getId()){
            case R.id.btn_operate:
                if (memberDetailModel.getDeviceMemberAuthority().equals(Constants.OWNER)){
                    ActivityUtil.goActivity(this, OrdinaryMemberActivity.class);
                } else {
                    new MaterialDialog.Builder(this)
                            .title(CommonUtils.getString(R.string.tips))
                            .content(content)
                            .positiveText(CommonUtils.getString(R.string.confirm))
                            .positiveColor(CommonUtils.getColor(R.color.colorRed))
                            .negativeText(CommonUtils.getString(R.string.cancel))
                            .callback(new MaterialDialog.Callback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    dialog.dismiss();
                                    delMember();
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .build()
                            .show();

                }
                break;
            case R.id.btn_save:
                updateShare();
                break;
            case R.id.ll_finger:
                bundle.putSerializable("memberDetailModel", memberDetailModel);
                ActivityUtil.goActivityWithBundle(this, FingerprintManageListActivity.class, bundle);
                break;
            case R.id.ll_card:

                break;
            case R.id.ll_startTime:
                startTimeView = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        bindingView.tvStartTime.setText(AppDateUtil.date2String(date));
                    }
                })
                        .setType(new boolean[]{true, true, true, true, true, false})
                        .setCancelText(CommonUtils.getString(R.string.cancel))
                        .setCancelColor(CommonUtils.getColor(R.color.colorRed))
                        .setSubmitText(CommonUtils.getString(R.string.confirm))
                        .setSubmitColor(CommonUtils.getColor(R.color.colorAccent))
                        .setTitleText(CommonUtils.getString(R.string.open_time))
                        .setTitleSize(16)
                        .setTitleColor(CommonUtils.getColor(R.color.colorInput))
                        .build();
                startTimeView.show();
                break;
            case R.id.ll_endTime:
                endTimeView = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        bindingView.tvEndTime.setText(AppDateUtil.date2String(date));
                    }
                })
                        .setType(new boolean[]{true, true, true, true, true, false})
                        .setCancelText(CommonUtils.getString(R.string.cancel))
                        .setCancelColor(CommonUtils.getColor(R.color.colorRed))
                        .setSubmitText(CommonUtils.getString(R.string.confirm))
                        .setSubmitColor(CommonUtils.getColor(R.color.colorAccent))
                        .setTitleText(CommonUtils.getString(R.string.open_time))
                        .setTitleSize(16)
                        .setTitleColor(CommonUtils.getColor(R.color.colorInput))
                        .build();
                endTimeView.show();
                break;
            case R.id.ll_unlockNum:
                unlockNumView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        bindingView.tvUnlockNum.setText(unlockNums.get(options1));
                        unLockNum = options1;
                    }
                })
                        .setLayoutRes(R.layout.layout_pickerview_time, new CustomListener() {
                            @Override
                            public void customLayout(View v) {
                                final TextView tvCancel = v.findViewById(R.id.tv_cancel);
                                final TextView tvConfirm = v.findViewById(R.id.tv_confirm);
                                tvCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        unlockNumView.dismiss();
                                    }
                                });
                                tvConfirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        unlockNumView.returnData();
                                        unlockNumView.dismiss();
                                    }
                                });
                            }
                        })
                        .build();
                unlockNumView.setPicker(unlockNums);
                unlockNumView.show();
                break;
        }
    }

    private void updateShare() {
        HashMap<String, Object> map = new HashMap<>();
        if (isLimit){
            map.put("beginDate", bindingView.tvStartTime.getText().toString().isEmpty()? "" : bindingView.tvStartTime.getText().toString() + ":00");
            map.put("endDate", bindingView.tvEndTime.getText().toString().isEmpty()? "" : bindingView.tvEndTime.getText().toString() + ":00");
        } else {
            map.put("beginDate", "");
            map.put("endDate", "");
        }
        map.put("deviceMemberDateConstraintType", isLimit ? "CONSTRAINT" : "NONE");
        map.put("id", String.valueOf(memberDetailModel.getId()));
        map.put("maxOpenTimes", unLockNum == 0 ? "" : String.valueOf(unLockNum));
        map.put("viewAlarm", true);
        map.put("viewOpenRecord", true);
        JSONObject jsonObject = new JSONObject(map);
        OkGo.<ResponseModel>post(VHomeApi.DEVICE_MEMBER_UPDATE)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .isSpliceUrl(true)
                .upJson(jsonObject.toString())
                .tag(this)
                .execute(new HudCallback<ResponseModel>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);

                    }
                });

    }

    /**
     * 删除成员
     */
    private void delMember() {
        OkGo.<ResponseModel>post(VHomeApi.DEVICE_MEMBER_DELETE + memberModel.getId())
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .tag(this)
                .execute(new HudCallback<ResponseModel>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            if(String.valueOf(SharedPreferencesUtils.get(Constants.AUTHORITY_NAME, "")).equals(Constants.OWNER)) {
                                RxBusUtil.getInstance().post(MemberListActivity.TAG, 0);
                                finish();
                            } else {
                                SharedPreferencesUtils.put(Constants.DEVICE_ID, "");
                                SharedPreferencesUtils.put(Constants.ID, "");
                                ActivityUtil.goActivityAndFinish(MemberDetailActivity.this, DeviceListActivity.class);
                            }
                        }
                    }
                });
    }
}
