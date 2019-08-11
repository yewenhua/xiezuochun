package so.voc.vhome.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivitySearchFriendBinding;
import so.voc.vhome.model.DeviceShareModel;
import so.voc.vhome.model.FriendsModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.HudCallback;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.Convert;
import so.voc.vhome.util.SharedPreferencesUtils;
import so.voc.vhome.util.Util;

/**
 * Fun：共享给app好友
 *
 * @Author：Linus_Xie
 * @Date：2019/6/13 14:48
 */
public class SearchFriendActivity extends BaseActivity<ActivitySearchFriendBinding> {

    private FriendsModel friendsModel;
    private DeviceShareModel deviceShareModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.share_to_app));
        deviceShareModel = (DeviceShareModel) this.getIntent().getExtras().getSerializable("deviceShare");
    }

    public void shareClick(View view){
        switch (view.getId()){
            case R.id.ll_search:
                String phoneStr = bindingView.etPhone.getText().toString().trim();
                if (!RegexUtils.isMobileSimple(phoneStr)){
                    ToastUtils.show(CommonUtils.getString(R.string.input_search_phone));
                    break;
                }
                searchFriend(phoneStr);
                break;
            case R.id.btn_confirmShare:
                if (friendsModel == null){
                    ToastUtils.show("请先查询要分享的用户！");
                    break;
                }
                deviceShareModel.setReceiverId(friendsModel.getId());
                deviceShare();
                break;
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
                            if (Util.currentVersionCode().equals("2")) {
                                ActivityUtil.goActivityAndFinish(SearchFriendActivity.this, MainActivity.class);
                            } else {
                                ActivityUtil.goActivityAndFinish(SearchFriendActivity.this, Main01Activity.class);
                            }
                        }
                    }
                });
    }

    private void searchFriend(String phoneStr) {
        OkGo.<ResponseModel<FriendsModel>>get(VHomeApi.MEMBER_SIMPLE + phoneStr)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .tag(this)
                .execute(new HudCallback<ResponseModel<FriendsModel>>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel<FriendsModel>> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            ToastUtils.show("该用户可以被分享，请点击确认按钮！");
                            friendsModel = response.body().data;
                        }
                    }
                });
    }
}
