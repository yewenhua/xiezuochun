package so.voc.vhome.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/4/20 11:33
 */
public class ActivityUtil {

    public static void goActivity(Activity activity, Class clazz) {
        Intent intent = new Intent(activity,
                clazz);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
    }

    public static void goActivityAndFinish(Activity activity, Class clazz) {
        Intent intent = new Intent(activity,
                clazz);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void goActivityWithBundle(Activity activity, Class clazz, Bundle bundle) {
        Intent intent = new Intent(activity,
                clazz);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
    }

    public static void goActivityForResult(Activity activity, Class clazz, int requestID) {
        Intent intent = new Intent(activity,
                clazz);
        activity.startActivityForResult(intent, requestID);
    }

    public static void goActivityForResultWithBundle(Activity activity, Class clazz, Bundle bundle, int requestID) {
        Intent intent = new Intent(activity,
                clazz);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestID);
    }

}
