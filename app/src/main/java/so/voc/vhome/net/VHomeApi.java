package so.voc.vhome.net;

/**
 * Fun：API接口
 *
 * @Author：Linus_Xie
 * @Date：2019/5/31 11:01
 */
public class VHomeApi {

    public static final String WEATHER_URL = "https://www.tianqiapi.com/api/";

    //public static final String URL = "http://192.168.30.40:9321";
//    public static final String URL = "http://192.168.1.105:9321";
    public static final String URL = "https://things.voc.so";
    public static final String AUTH_LOGIN = URL + "/auth/login";//auth认证登录方式
    public static final String REFRESH_TOKEN = URL + "/oauth/token";//auth刷新token

    /**
     * app-默认
     */
    public static final String FORGET_PWD = URL + "/app/forgetPassword";//忘记密码
    public static final String GET_HEAD_ICON = URL + "/app/headIcon/";//获取头像图片
    public static final String MEMBER_MESSAGE = URL + "/app/memberMessage";//当前会员信息
    public static final String MODIFY_MEMBER = URL + "/app/modifyMember";//修改个人信息
    public static final String MODIFY_PASSWORD = URL + "/app/modifyPassword";//修改密码
    public static final String REGISTER = URL + "/app/register";//注册
    public static final String LOGOUT = URL + "/app/logout";//退出登录
    public static final String UPLOAD_HEAD_ICON = URL + "/app/uploadHeadIcon";//上传头像

    /**
     * app-短信
     */
    public static final String MESSAGE_REGISTER = URL + "/app/shortMessage/register/";//发送注册短信
    public static final String MESSAGE_FORGET_PASSWORD = URL + "/app/shortMessage/forgetPassword/";//发送忘记密码短信

    /**
     * app-会员
     */
    public static final String MEMBER_SIMPLE = URL + "/app/member/simple/";//根据手机号码获取会员简单信息 - 用于设备分享的时候

    /**
     * app-设备
     */
    public static final String DEVICE_BIND = URL + "/app/device/bind";//设备绑定
    public static final String DEVICE_UNBIND = URL + "/app/device/unbind";//解绑
    public static final String DEVICE_UPDATE = URL + "/app/device/update";//修改操作

    /**
     * app-设备白名单
     */
    public static final String DEVICE_WHITE_ALL_MEMBERS = URL + "/app/deviceWhiteList/allMembers/";//指定设备的所有用户，包括 白名单关联的注册用户&&名单别名 - 只能设备拥有者调用
    public static final String DEVICE_WHITE_BIND = URL + "/app/deviceWhiteList/bind";//绑定操作 - 只能设备拥有者调用
    public static final String DEVICE_WHITE_DELETE = URL + "/app/deviceWhiteList/delete/";//删除操作 - 只能设备拥有者调用
    public static final String DEVICE_WHITE_LIST = URL + "/app/deviceWhiteList/list";//列表 - 只能设备拥有者调用
    public static final String DEVICE_WHITE_MEMBER_LIST = URL + "/app/deviceWhiteList/memberList/";//指定设备的可以关联的会员列表 - 只能设备拥有者调用
    public static final String DEVICE_WHITE_UPDATE = URL + "/app/deviceWhiteList/update";//修改操作 - 只能设备拥有者调用

    /**
     * app-设备分享
     */
    public static final String DEVICE_SHARE_CONFIRM = URL + "/app/deviceShare/confirm/";//确认分享 - 接收人操作
    public static final String DEVICE_SHARE_DELETE = URL + "/app/deviceShare/delete/";//删除 - 设备拥有者
    public static final String DEVICE_SHARE = URL + "/app/deviceShare/share";//设备分享
    public static final String DEVICE_SHARE_SEARCH = URL + "/app/deviceShare/search";//分页搜索
    public static final String DEVICE_SHARE_REFUSE = URL + "/app/deviceShare/refuse/";//拒绝分享 - 接收人操作
    public static final String DEVICE_SHARE_UPDATE = URL + "/app/deviceShare/update";//修改 - 设备拥有者修改未接收&&未拒绝的设备分享

    /**
     * app-设备会员
     */
    public static final String DEVICE_MEMBER_DELETE = URL + "/app/deviceMember/delete/";//删除设备会员
    public static final String DEVICE_MEMBER_DETAIL = URL + "/app/deviceMember/detail/";//详情
    public static final String DEVICE_MEMBERS= URL + "/app/deviceMember/deviceMembers/";//指定设备的设备成员 - 设备拥有者调用
    public static final String DEVICE_MEMBER_MINE = URL + "/app/deviceMember/mine";//我的设备
    public static final String DEVICE_MEMBER_ORDINARY = URL + "/app/deviceMember/ordinaryDeviceMembers/";//指定设备的普通设备会员 - 设备拥有者调用
    public static final String DEVICE_MEMBER_SELECT = URL + "/app/deviceMember/select/";//选择操作
    public static final String DEVICE_MEMBER_SELECTED = URL + "/app/deviceMember/selected";//获取默认选中的设备会员
    public static final String DEVICE_MEMBER_TRANSFER_OWNER = URL + "/app/deviceMember/transferOwner/";//转让设备拥有者
    public static final String DEVICE_MEMBER_UPDATE = URL + "/app/deviceMember/update";//修改 - 设备拥有者调用

    /**
     * app-钥匙分享
     */
    public static final String KEY_SHARE = URL + "/app/keyShare/share";//钥匙分享
    public static final String KEY_SHARE_DELETE = URL + "/app/keyShare/delete/";//删除分享---设备拥有者操作
    public static final String KEY_SHARE_SEARCH = URL + "/app/keyShare/search";//钥匙分享记录---分页搜索

    /**
     * app-站内信息
     */
    public static final String NOTIFICATION_DELETE = URL + "/app/notification/delete/";//删除
    public static final String NOTIFICATION_PAGE = URL + "/app/notification/page";//分页搜索消息
    public static final String NOTIFICATION_STATISTICS = URL + "/app/notification/statistics";//信息统计
    public static final String NOTIFICATION_UNREADCOUNT = URL + "/app/notification/unreadCount";//站内消息未读数量

}
