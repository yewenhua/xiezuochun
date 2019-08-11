package so.voc.vhome.model;

import java.io.Serializable;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/4 13:25
 */
public class DeviceModel implements Serializable {


    /**
     * id : 3
     * deviceId : 2
     * deviceName : 测试锁
     * deviceTypeName : 蓝牙
     * deviceMemberAuthorityName : 拥有者
     * deviceMemberAuthority ：OWNER
     */

    private int id;
    private String deviceId;
    private String deviceName;
    private String deviceTypeName;
    private String deviceMemberAuthorityName;
    private String deviceMemberAuthority;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getDeviceMemberAuthorityName() {
        return deviceMemberAuthorityName;
    }

    public void setDeviceMemberAuthorityName(String deviceMemberAuthorityName) {
        this.deviceMemberAuthorityName = deviceMemberAuthorityName;
    }

    public String getDeviceMemberAuthority() {
        return deviceMemberAuthority;
    }

    public void setDeviceMemberAuthority(String deviceMemberAuthority) {
        this.deviceMemberAuthority = deviceMemberAuthority;
    }

    @Override
    public String toString() {
        return "DeviceModel{" +
                "id=" + id +
                ", deviceId='" + deviceId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceTypeName='" + deviceTypeName + '\'' +
                ", deviceMemberAuthorityName='" + deviceMemberAuthorityName + '\'' +
                ", deviceMemberAuthority='" + deviceMemberAuthority + '\'' +
                '}';
    }
}
