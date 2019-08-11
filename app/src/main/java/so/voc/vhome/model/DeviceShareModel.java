package so.voc.vhome.model;

import java.io.Serializable;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/13 15:10
 */
public class DeviceShareModel implements Serializable {


    /**
     * alias : string
     * beginDate : 2019-06-13T05:41:44.688Z
     * deviceId : 0
     * deviceMemberConstraintType : NONE
     * deviceShareWay : APP
     * endDate : 2019-06-13T05:41:44.688Z
     * maxOpenTimes : 0
     * receiverId : 0
     */

    private String beginDate;
    private String deviceId;
    private String deviceMemberDateConstraintType;
    private String deviceShareWay;
    private String endDate;
    private String maxOpenTimes;
    private String receiverId;
    private boolean viewAlarm;
    private boolean viewOpenRecord;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceMemberDateConstraintType() {
        return deviceMemberDateConstraintType;
    }

    public void setDeviceMemberDateConstraintType(String deviceMemberDateConstraintType) {
        this.deviceMemberDateConstraintType = deviceMemberDateConstraintType;
    }

    public String getDeviceShareWay() {
        return deviceShareWay;
    }

    public void setDeviceShareWay(String deviceShareWay) {
        this.deviceShareWay = deviceShareWay;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMaxOpenTimes() {
        return maxOpenTimes;
    }

    public void setMaxOpenTimes(String maxOpenTimes) {
        this.maxOpenTimes = maxOpenTimes;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public boolean isViewAlarm() {
        return viewAlarm;
    }

    public void setViewAlarm(boolean viewAlarm) {
        this.viewAlarm = viewAlarm;
    }

    public boolean isViewOpenRecord() {
        return viewOpenRecord;
    }

    public void setViewOpenRecord(boolean viewOpenRecord) {
        this.viewOpenRecord = viewOpenRecord;
    }
}
