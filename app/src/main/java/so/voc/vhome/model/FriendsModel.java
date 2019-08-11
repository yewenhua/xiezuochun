package so.voc.vhome.model;

import java.io.Serializable;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/13 14:59
 */
public class FriendsModel implements Serializable {

    /**
     * id : 4
     * mobile : 13600672232
     * handledName : 136****2232
     */

    private String id;
    private String mobile;
    private String handledName;
    private String headIcon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHandledName() {
        return handledName;
    }

    public void setHandledName(String handledName) {
        this.handledName = handledName;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }
}
