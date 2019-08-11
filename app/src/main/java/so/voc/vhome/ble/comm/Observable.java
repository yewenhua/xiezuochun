package so.voc.vhome.ble.comm;

import com.clj.fastble.data.BleDevice;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/7/2 15:38
 */
public interface Observable {

    void addObserver(Observer obj);

    void deleteObserver(Observer obj);

    void notifyObserver(BleDevice bleDevice);
}
