package so.voc.vhome.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.LogUtils;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityShareDeviceSettingBinding;
import so.voc.vhome.model.DeviceShareModel;
import so.voc.vhome.model.DeviceShareRecordModel;
import so.voc.vhome.model.FriendsModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.HudCallback;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.AppDateUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.Convert;
import so.voc.vhome.util.SharedPreferencesUtils;
import so.voc.vhome.widget.SwitchButton;

/**
 * Fun：共享设备设置
 *
 * @Author：Linus_Xie
 * @Date：2019/6/13 9:27
 */
public class ShareDeviceSettingActivity extends BaseActivity<ActivityShareDeviceSettingBinding> {

    private TimePickerView startTimeView;
    private TimePickerView endTimeView;
    private OptionsPickerView unlockNumView;
    private List<String> unlockNums = new ArrayList<>();
    private boolean isLimit = false;
    private int unLockNum = 0;//开锁次数，默认为0，不限制
    private DeviceShareModel deviceShareModel = new DeviceShareModel();
    private FriendsModel friendsModel;
    private boolean viewAlarm = true;
    private boolean viewOpenRecord = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_device_setting);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.share_setting));
        Bundle bundle = this.getIntent().getExtras();
        friendsModel = (FriendsModel) bundle.getSerializable("friendsModel");
        bindingView.tvShareUser.setText(friendsModel.getMobile());
        bindingView.setIsLimit(isLimit);
        bindingView.sbOpenTime.setState(!isLimit);
        bindingView.sbOpenTime.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                isLimit = false;
                bindingView.setIsLimit(isLimit);
                bindingView.sbOpenTime.setState(!isLimit);
            }

            @Override
            public void toggleToOff() {
                isLimit = true;
                bindingView.setIsLimit(isLimit);
                bindingView.sbOpenTime.setState(!isLimit);
            }
        });

        bindingView.sbViewAlarmRecord.setState(viewAlarm);
        bindingView.sbViewAlarmRecord.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                viewAlarm = true;
                bindingView.sbViewAlarmRecord.setState(viewAlarm);
            }

            @Override
            public void toggleToOff() {
                viewAlarm = false;
                bindingView.sbViewAlarmRecord.setState(viewAlarm);
            }
        });

        bindingView.sbViewOpenRecord.setState(viewOpenRecord);
        bindingView.sbViewOpenRecord.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                viewOpenRecord = true;
                bindingView.sbViewOpenRecord.setState(viewOpenRecord);
            }

            @Override
            public void toggleToOff() {
                viewOpenRecord = false;
                bindingView.sbViewOpenRecord.setState(viewOpenRecord);
            }
        });

        //设置开门次数
        setUnlockNum();
    }

    public void settingClick(View view){
        switch (view.getId()){
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
            case R.id.btn_confirmShare:
                deviceShareModel("APP");
                deviceShare();
                break;
        }
    }

    private void deviceShareModel(String shareWay) {
        //TODO 次数不限制传空
        deviceShareModel.setBeginDate(bindingView.tvStartTime.getText().toString().isEmpty()? "" : bindingView.tvStartTime.getText().toString() + ":00");
        deviceShareModel.setEndDate(bindingView.tvEndTime.getText().toString().isEmpty()? "" : bindingView.tvEndTime.getText().toString() + ":00");
        deviceShareModel.setDeviceId(String.valueOf(SharedPreferencesUtils.get(Constants.DEVICE_ID, "")));
        deviceShareModel.setDeviceMemberDateConstraintType(isLimit ? "CONSTRAINT" : "NONE");
        deviceShareModel.setDeviceShareWay(shareWay);
        deviceShareModel.setMaxOpenTimes(unLockNum == 0 ? "" : String.valueOf(unLockNum));
        deviceShareModel.setViewAlarm(viewAlarm);
        deviceShareModel.setViewOpenRecord(viewOpenRecord);
        deviceShareModel.setReceiverId(friendsModel.getId());
    }

    private void setUnlockNum() {
        for (int i = 0; i < 51; i++){
            unlockNums.add(i == 0 ? "不限制" : String.valueOf(i));
        }
    }


    private void deviceShare() {
        String deviceShareStr = Convert.toJson(deviceShareModel);
        OkGo.<ResponseModel>post(VHomeApi.DEVICE_SHARE)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .upJson(deviceShareStr)
                .isSpliceUrl(true)
                .tag(this)
                .execute(new HudCallback<ResponseModel>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        LogUtils.e(response.body().data);
                        if (response.body().code == 0){
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("friendsModel", friendsModel);
                            ActivityUtil.goActivityWithBundle(ShareDeviceSettingActivity.this, ShareSuccessActivity.class, bundle);
                            finish();
                        }
                    }
                });
    }
}
