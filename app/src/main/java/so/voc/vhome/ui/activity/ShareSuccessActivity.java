package so.voc.vhome.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hjq.toast.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityShareSuccessBinding;
import so.voc.vhome.model.FriendsModel;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Util;

/**
 * Fun：分享成功
 *
 * @Author：Linus_Xie
 * @Date：2019/6/26 15:41
 */
public class ShareSuccessActivity extends BaseActivity<ActivityShareSuccessBinding> {

    private FriendsModel friendsModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_success);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.share_success));
        Bundle bundle = this.getIntent().getExtras();
        friendsModel = (FriendsModel) bundle.getSerializable("friendsModel");
        bindingView.tvUserPhone.setText(friendsModel.getMobile());
    }

    public void shareClick(View view){
        switch (view.getId()){
            case R.id.btn_notifyWechat:
                wechat();
                break;
            case R.id.btn_cancel:
                if (Util.currentVersionCode().equals("2")) {
                    ActivityUtil.goActivityAndFinish(ShareSuccessActivity.this, MainActivity.class);
                } else {
                    ActivityUtil.goActivityAndFinish(ShareSuccessActivity.this, Main01Activity.class);
                }
                break;
        }
    }

    private void wechat() {
        UMMin umMin = new UMMin("https://share.voc.so/voc/share");
        umMin.setThumb(new UMImage(this, R.mipmap.share));
        umMin.setTitle("设备共享");
        umMin.setDescription("设备共享");
        umMin.setPath("/pages/share/acceptdevice/acceptdevice?scode=");
        umMin.setUserName("gh_645af25a73d3");
        new ShareAction(this)
                .withMedia(umMin)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener).share();
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtils.show(CommonUtils.getString(R.string.share_device_success));
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.show(CommonUtils.getString(R.string.share_device_failed) + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.show(CommonUtils.getString(R.string.share_device_cancel));
        }
    };
}
