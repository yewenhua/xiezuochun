package so.voc.vhome.util;

import android.app.Activity;
import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.PermissionUtils;

import so.voc.vhome.R;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/22 11:27
 */
public class DialogUtil {

    public static void appSettingDialog(Activity activity) {
        new MaterialDialog.Builder(activity)
                .title(CommonUtils.getString(R.string.tips))
                .content(CommonUtils.getString(R.string.request_permission))
                .positiveText(CommonUtils.getString(R.string.confirm))
                .positiveColor(CommonUtils.getColor(R.color.colorRed))
                .negativeText(CommonUtils.getString(R.string.cancel))
                .callback(new MaterialDialog.Callback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        PermissionUtils.launchAppDetailsSettings();
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show();
    }
}
