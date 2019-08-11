package so.voc.vhome.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.helper.Logger;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/4 15:53
 */
public class Util {

    /**
     * 初始化数据，避免空指针
     *
     * @param str
     * @return
     */
    public static String initNullStr(String str) {
        if ("null".equals(str) || null == str || "".equals(str)) {
            return "";
        }
        return str;
    }

    public static boolean isEmpty(String s) {
        if (null == s) {
            return true;
        }
        if (s.length() == 0) {
            return true;
        }
        return s.trim().length() == 0;
    }


    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static String hidePhoneNum(String phone) {
        return phone.substring(0,3) + "****" + phone.substring(7, 11);
    }

    /**
     * 自定义压缩存储地址
     *
     * @return
     */
    public static String getPath(Context context) {
        String path = context.getCacheDir() + "/VHome/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    /**
     * 当前app大版本号
     * @return
     */
    public static String currentVersionCode(){
        String versionCode = AppUtils.getAppVersionName();
        return versionCode.split("\\.")[0];
    }
}
