package so.voc.vhome.util;

import android.content.Context;

import io.objectbox.BoxStore;
import so.voc.vhome.model.MyObjectBox;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/7/18 16:24
 */
public class ObjectBox {
    private static BoxStore boxStore;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();
    }

    public static BoxStore get() { return boxStore; }
}
