package so.voc.vhome.model;

import java.io.Serializable;

/**
 * Fun：成员管理对象
 *
 * @Author：Linus_Xie
 * @Date：2019/6/10 13:44
 */
public class MemberModel implements Serializable {


    /**
     * id : 3
     * memberName : 18757707988
     * deviceMemberAuthority : OWNER
     */

    private int id;
    private String memberName;
    private String deviceMemberAuthority;
    /**
     * visible : false
     */

    private boolean visible;
    private int fingerCount;
    private int cardCount;

    private String memberHeadIcon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getDeviceMemberAuthority() {
        return deviceMemberAuthority;
    }

    public void setDeviceMemberAuthority(String deviceMemberAuthority) {
        this.deviceMemberAuthority = deviceMemberAuthority;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getFingerCount() {
        return fingerCount;
    }

    public void setFingerCount(int fingerCount) {
        this.fingerCount = fingerCount;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }


    public String getMemberHeadIcon() {
        return memberHeadIcon;
    }

    public void setMemberHeadIcon(String memberHeadIcon) {
        this.memberHeadIcon = memberHeadIcon;
    }
}
