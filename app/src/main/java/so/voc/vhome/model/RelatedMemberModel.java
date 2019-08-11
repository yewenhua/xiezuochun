package so.voc.vhome.model;

import so.voc.vhome.util.Util;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/7/15 13:30
 */
public class RelatedMemberModel {


    /**
     * relatedMember : false
     * memberId : null
     * memberName : null
     * alias : 叶文华别名官方
     */

    private boolean relatedMember;
    private String memberId;
    private String memberName;
    private String alias;

    public boolean isRelatedMember() {
        return relatedMember;
    }

    public void setRelatedMember(boolean relatedMember) {
        this.relatedMember = relatedMember;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return Util.initNullStr(alias).isEmpty() ? memberName : alias;
    }
}
