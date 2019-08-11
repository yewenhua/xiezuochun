package so.voc.vhome.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.clj.fastble.BleManager;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import cn.jpush.android.api.JPushInterface;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.unit.Subunits;
import okhttp3.OkHttpClient;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.ObjectBox;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/5/1 8:01
 */
public class BaseApplication extends Application {

    public static final String TAG = BaseApplication.class.getSimpleName();

    private static BaseApplication instance;
    private static Context context;
    private static SharedPreferences mSharedPreferences;

    public static BaseApplication getInstance() {
        return instance;
    }

    public static Context getContext(){
        return  context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = this;
        //TODO 发布时，记得关闭日志
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        ObjectBox.init(this);

        mSharedPreferences = getSharedPreferences(Constants.APPLICATION_NAME,  MODE_PRIVATE);
        initOkGo();
        ToastUtils.init(this);
        initAutoSize();
        UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, "5d01c01d3fc1951c73000373");
        //TODO 发布时，要关闭日志
        UMConfigure.setLogEnabled(false);

        BleManager.getInstance().init(this);
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(2, 500)
                .setConnectOverTime(10000)
                .setOperateTimeout(5000);
    }


    {
        PlatformConfig.setWeixin("wx6d2fb3a6ca90cafc", "058947d9d64747fdc922024d390e5d38");
    }

    private void initOkGo(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        builder.readTimeout(20000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(10000, TimeUnit.MILLISECONDS);
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);
        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build())
                .setCacheMode(CacheMode.NO_CACHE)
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                .setRetryCount(3);
    }

    private void initAutoSize() {
        AutoSizeConfig.getInstance().getUnitsManager()
                .setSupportDP(false)
                .setSupportSubunits(Subunits.PT);
        AutoSize.initCompatMultiProcess(this);
    }

    public static SharedPreferences getSP() {
        return mSharedPreferences;
    }

    static {
        //设置全局的Header构造器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColors(CommonUtils.getColor(android.R.color.holo_orange_light),CommonUtils.getColor(android.R.color.holo_blue_light), CommonUtils.getColor(android.R.color.holo_green_light));
                return new MaterialHeader(context) ;
            }
        });
    }
}
