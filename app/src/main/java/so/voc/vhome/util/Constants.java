package so.voc.vhome.util;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/5/1 8:00
 */
public class Constants {

    public static final String APPLICATION_NAME = "appDetails.xml";

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String USER_ID = "user_id";
    public static final String USER_PHONE = "user_phone";
    public static final String USER_NAME = "user_name";
    public static final String HEAD_URL = "head_url";

    /**
     * 当前设备id
     */
    public static final String ID = "id";
    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_NAME = "device_name";
    public static final String AUTHORITY_NAME = "authority_name";//是否这把锁的拥有者，OWNER 拥有者，ORDINARY 普通成员
    public static final String OWNER = "OWNER";
    public static final String BLE_MAC = "ble_mac";//蓝牙地址

    public static final int EDIT_CODE = 1991;

    //请求蓝牙打开
    public final static int REQUEST_ENABLE_BLE = 1;
    public final static int REFRESH_DEVICE = 2;

    //打开相册
    public static final int IMAGE_PICKER = 0316;

    /**
     *  天气记录保存
     */
    public static final String WEA_IMG = "wea_img";
    public static final String TEM = "tem";
    public static final String WEA_AIR = "wea_air";

    public static final int RESULT_CODE = 2019;

    public static String Characteristic_uuid_TX = "0000ffb2-0000-1000-8000-00805f9b34fb";

    public static final String SERVICE_UUID = "0000ffb0-0000-1000-8000-00805f9b34fb";
    public static final String UUID = "0000ffb2-0000-1000-8000-00805f9b34fb";
}
