package so.voc.vhome.ble.comm;

import com.clj.fastble.data.BleDevice;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/7/2 15:36
 */
public interface Observer {

    void disConnected(BleDevice bleDevice);
}
