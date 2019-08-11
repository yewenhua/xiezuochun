package so.voc.vhome.model;

/**
 * Fun：蓝牙数组对象
 *
 * @Author：Linus_Xie
 * @Date：2019/7/8 17:04
 */
public class BleDataModel {
    private String cmd;
    private int len;
    private byte[] data;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
