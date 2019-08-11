package so.voc.vhome.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

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

import java.util.HashMap;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityShareKeyBinding;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.HudCallback;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.SharedPreferencesUtils;

/**
 * Fun：分享钥匙
 *
 * @Author：Linus_Xie
 * @Date：2019/6/26 10:37
 */
public class ShareKeyActivity extends BaseActivity<ActivityShareKeyBinding> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_key);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.share_key));
    }

    public void shareClick(View view){
        switch (view.getId()){
            case R.id.btn_confirmShare:
                shareKey();
                break;
            case R.id.btn_shareRecord:
                ActivityUtil.goActivity(this, KeyShareRecordActivity.class);
                break;
        }
    }

    /**
     * 钥匙分享
     */
    private void shareKey() {
        HashMap<String, String> map = new HashMap<>();
        map.put("deviceId", String.valueOf(SharedPreferencesUtils.get(Constants.DEVICE_ID, "")));
        JSONObject jsonObject = new JSONObject(map);
        OkGo.<ResponseModel>post(VHomeApi.KEY_SHARE)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .isSpliceUrl(true)
                .upJson(jsonObject)
                .execute(new HudCallback<ResponseModel>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(response.body().data));
                                wechat(jsonObject.getString("validateCode"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void wechat(String data) {
        if(data.isEmpty()){
            ToastUtils.show("未取到分享密钥，请重试！");
        } else {
            UMMin umMin = new UMMin("https://share.voc.so/voc/share");
            umMin.setThumb(new UMImage(this, R.mipmap.share));
            umMin.setTitle("钥匙分享");
            umMin.setDescription("钥匙分享");
            umMin.setPath("/pages/share/acceptkey/acceptkey?scode=" + data);
            umMin.setUserName("gh_645af25a73d3");
            new ShareAction(this)
                    .withMedia(umMin)
                    .setPlatform(SHARE_MEDIA.WEIXIN)
                    .setCallback(shareListener).share();
        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtils.show(CommonUtils.getString(R.string.share_key_success));
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.show(CommonUtils.getString(R.string.share_key_failed) + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.show(CommonUtils.getString(R.string.share_key_cancel));
        }
    };
}
