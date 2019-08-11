package so.voc.vhome.model;

import java.io.Serializable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/7/6 13:10
 */
@Entity
public class FingerModel implements Serializable {


    /**
     * no : 55
     * name : 别名
     * deviceWhiteListType : FINGER
     * deviceWhiteListTypeName : 指纹
     * deviceWhiteListLevel : GUEST
     * deviceWhiteListLevelName : 客人
     */
    private int id;
    @Id(assignable = true)
    private long no;
    private String deviceWhiteListType;
    private String deviceWhiteListTypeName;
    private String deviceWhiteListLevel;
    private String deviceWhiteListLevelName;
    private String memberId;
    private String alias;
    /**
     * no : 1
     * memberId : 3
     * memberName : 春哥
     * alias : null
     */

    private String memberName;

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public String getDeviceWhiteListType() {
        return deviceWhiteListType;
    }

    public void setDeviceWhiteListType(String deviceWhiteListType) {
        this.deviceWhiteListType = deviceWhiteListType;
    }

    public String getDeviceWhiteListTypeName() {
        return deviceWhiteListTypeName;
    }

    public void setDeviceWhiteListTypeName(String deviceWhiteListTypeName) {
        this.deviceWhiteListTypeName = deviceWhiteListTypeName;
    }

    public String getDeviceWhiteListLevel() {
        return deviceWhiteListLevel;
    }

    public void setDeviceWhiteListLevel(String deviceWhiteListLevel) {
        this.deviceWhiteListLevel = deviceWhiteListLevel;
    }

    public String getDeviceWhiteListLevelName() {
        return deviceWhiteListLevelName;
    }

    public void setDeviceWhiteListLevelName(String deviceWhiteListLevelName) {
        this.deviceWhiteListLevelName = deviceWhiteListLevelName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "FingerModel{" +
                "id=" + id +
                ", no=" + no +
                ", deviceWhiteListType='" + deviceWhiteListType + '\'' +
                ", deviceWhiteListTypeName='" + deviceWhiteListTypeName + '\'' +
                ", deviceWhiteListLevel='" + deviceWhiteListLevel + '\'' +
                ", deviceWhiteListLevelName='" + deviceWhiteListLevelName + '\'' +
                ", memberId='" + memberId + '\'' +
                ", alias='" + alias + '\'' +
                ", memberName='" + memberName + '\'' +
                '}';
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
