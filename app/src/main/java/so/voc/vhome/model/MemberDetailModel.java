package so.voc.vhome.model;

import java.io.Serializable;
import java.util.List;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/26 11:10
 */
public class MemberDetailModel implements Serializable {

    /**
     * id : 3
     * deviceId : 3
     * deviceName : 测试锁111
     * deviceTypeName : 蓝牙
     */

    private int id;
    private int deviceId;
    private String deviceName;
    private String deviceTypeName;
    /**
     * deviceMemberAuthority : OWNER
     * deviceMemberDateConstraintType : NONE
     * beginDate : null
     * endDate : null
     * maxOpenTimes : null
     * viewOpenRecord : null
     * viewAlarm : null
     * fingers : [1,2]
     * cards : []
     * memberName : 春哥
     * memberMobile : 18757707988
     * memberHeadIcon : 2019_07_17_d2cb1f32-d9ec-4d4c-9d13-2a153fe11249.jpg
     */

    private String deviceMemberAuthority;
    private String deviceMemberDateConstraintType;
    private String beginDate;
    private String endDate;
    private String maxOpenTimes;
    private boolean viewOpenRecord;
    private boolean viewAlarm;
    private String memberName;
    private String memberMobile;
    private String memberHeadIcon;
    private List<Integer> fingers;
    private List<Integer> cards;
    /**
     * beginDate : null
     * endDate : null
     * maxOpenTimes : null
     * viewOpenRecord : null
     * viewAlarm : null
     * cards : []
     * memberId : 3
     */

    private int memberId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
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


    public String getDeviceMemberAuthority() {
        return deviceMemberAuthority;
    }

    public void setDeviceMemberAuthority(String deviceMemberAuthority) {
        this.deviceMemberAuthority = deviceMemberAuthority;
    }

    public String getDeviceMemberDateConstraintType() {
        return deviceMemberDateConstraintType;
    }

    public void setDeviceMemberDateConstraintType(String deviceMemberDateConstraintType) {
        this.deviceMemberDateConstraintType = deviceMemberDateConstraintType;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
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

    public boolean isViewOpenRecord() {
        return viewOpenRecord;
    }

    public void setViewOpenRecord(boolean viewOpenRecord) {
        this.viewOpenRecord = viewOpenRecord;
    }

    public boolean isViewAlarm() {
        return viewAlarm;
    }

    public void setViewAlarm(boolean viewAlarm) {
        this.viewAlarm = viewAlarm;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public String getMemberHeadIcon() {
        return memberHeadIcon;
    }

    public void setMemberHeadIcon(String memberHeadIcon) {
        this.memberHeadIcon = memberHeadIcon;
    }

    public String getFingers() {
        String f = "";
        for (int i = 0; i < fingers.size(); i++) {
            f = f + fingers.get(i);
            if (i != fingers.size() - 1){
                f = f + "、";
            }
        }
        return f.isEmpty() ? "暂未关联" : f;
    }

    public void setFingers(List<Integer> fingers) {
        this.fingers = fingers;
    }

    public String getCards() {
        String c = "";
        for (int i = 0; i < cards.size(); i++) {
            c = c + cards.get(i);
            if (i != cards.size() - 1){
                c = c + "、";
            }
        }
        return c.isEmpty() ? "暂未关联" : c;
    }

    public void setCards(List<Integer> cards) {
        this.cards = cards;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }



}
