package so.voc.vhome.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityPersonalBinding;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.HudCallback;
import so.voc.vhome.okgo.StringCallback;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.BindingUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.DialogUtil;
import so.voc.vhome.util.SharedPreferencesUtils;
import so.voc.vhome.util.Util;

/**
 * Fun：个人信息
 *
 * @Author：Linus_Xie
 * @Date：2019/6/20 14:55
 */
public class PersonalActivity extends BaseActivity<ActivityPersonalBinding> {

    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.personal_info));
        bundle = new Bundle();
        bindingView.tvNickName.setText(String.valueOf(SharedPreferencesUtils.get(Constants.USER_NAME, "")).isEmpty() ? "未设置" : String.valueOf(SharedPreferencesUtils.get(Constants.USER_NAME, "")));
        bindingView.tvPhone.setText(String.valueOf(SharedPreferencesUtils.get(Constants.USER_PHONE, "")));
        if (!String.valueOf(SharedPreferencesUtils.get(Constants.HEAD_URL, "")).isEmpty()){
            BindingUtil.loadImage(bindingView.ivHeadIcon, String.valueOf(SharedPreferencesUtils.get(Constants.HEAD_URL, "")));
        }
    }

    public void personalClick(View view){
        switch (view.getId()){
            case R.id.ll_headIcon:
                PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                        .callback(new PermissionUtils.FullCallback() {
                            @Override
                            public void onGranted(List<String> permissionsGranted) {
                                picSelector();
                            }

                            @Override
                            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                                LogUtils.e(permissionsDeniedForever, permissionsDenied);
                                if (!permissionsDeniedForever.isEmpty()){
                                    DialogUtil.appSettingDialog(PersonalActivity.this);
                                }
                            }
                        }).request();
                break;
            case R.id.ll_nickName:
                bundle.putString("type", "nickName");
                bundle.putString("value", bindingView.tvNickName.getText().toString().trim());
                ActivityUtil.goActivityForResultWithBundle(this, EditActivity.class, bundle, Constants.EDIT_CODE);
                break;
            case R.id.ll_phone:

                break;
        }
    }

    private void picSelector() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.PictureSelector)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .enableCrop(true)// 是否裁剪
                //.compressSavePath(Util.getPath(this))
                .compress(true)// 是否压缩
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(Constants.IMAGE_PICKER);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.IMAGE_PICKER && resultCode == RESULT_OK){
            if (data != null){
                List<LocalMedia> imageItems =  PictureSelector.obtainMultipleResult(data);
                LogUtils.e(imageItems.size(), FileUtils.getDirName(imageItems.get(0).getCompressPath()));
                BindingUtil.loadImage(bindingView.ivHeadIcon, "file://" + imageItems.get(0).getCompressPath());
                //SharedPreferencesUtils.put(Constants.HEAD_URL, "file://" + imageItems.get(0).getCompressPath());
                uploadHeadIcon(FileUtils.getFileByPath(imageItems.get(0).getCompressPath()));
            }
        } else if (requestCode == Constants.EDIT_CODE && resultCode == RESULT_OK){
            if (data != null){
                Bundle bundle = data.getExtras();
                String value = bundle.getString("value");
                SharedPreferencesUtils.put(Constants.USER_NAME, value);
                bindingView.tvNickName.setText(value);
            }
        }
    }

    private void uploadHeadIcon(File fileByPath) {
        OkGo.<ResponseModel>post(VHomeApi.UPLOAD_HEAD_ICON)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .isSpliceUrl(true)
                .params("pic", fileByPath)
                .tag(this)
                .execute(new HudCallback<ResponseModel>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            String data = (String) response.body().data;
                            if (!Util.initNullStr(data).isEmpty()){
                                SharedPreferencesUtils.put(Constants.HEAD_URL, VHomeApi.GET_HEAD_ICON
                                        + data + "?access_token="
                                        + String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")));
                            }
                        }
                    }
                });
    }
}
