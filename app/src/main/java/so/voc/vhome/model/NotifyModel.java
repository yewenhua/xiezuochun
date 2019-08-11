package so.voc.vhome.model;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/19 14:11
 */
public class NotifyModel {


    /**
     * notificationType : SHARE
     * notificationTypeName : 分享通知
     * unreadCount : 3
     * message : 来着文华的设备分享, 点击查看详情
     */

    private String notificationType;
    private String notificationTypeName;
    private int unreadCount;
    private String message;

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationTypeName() {
        return notificationTypeName;
    }

    public void setNotificationTypeName(String notificationTypeName) {
        this.notificationTypeName = notificationTypeName;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
