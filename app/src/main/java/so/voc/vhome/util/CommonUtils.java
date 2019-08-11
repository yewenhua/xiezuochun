package so.voc.vhome.util;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import so.voc.vhome.base.BaseApplication;


/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/5/1 8:00
 */
public class CommonUtils {

    public static int getColor(int resId){
        return getResource().getColor(resId);
    }

    public static Resources getResource(){
        return BaseApplication.getInstance().getResources();
    }

    public static String[] getStringArray(int resId){
        return getResource().getStringArray(resId);
    }

    public static String getString(int resId){
        return getResource().getString(resId);
    }

    public static float getDimens(int resId){
        return getResource().getDimension(resId);
    }

    public static void removeSelfFromParent(View child){
        if (child != null){
            ViewParent parent = child.getParent();
            if (parent instanceof ViewGroup){
                ViewGroup group = (ViewGroup) parent;
                group.removeView(child);
            }
        }
    }
}

