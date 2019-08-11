package so.voc.vhome.ble.data;


import android.util.Log;

import so.voc.vhome.util.HexUtil;

/**
 * Fun：协议格式
 * |start||cmd||Len||Data||Finish|
 *
 * @Author：Linus_Xie
 * @Date：2019/6/28 14:01
 */
public class BleData {

    public byte[] start = {(byte) 0x5A};
    public byte[] finish = {(byte) 0x5B};

    public byte[] cmdFirmwareQuery = {(byte) 0x01};//固件查询
    public byte[] cmdRegistrationDir = {(byte) 0x02};//注册指令
    public byte[] cmdRandom = {(byte) 0x17};//随机数传递
    public byte[] cmdLoginDir = {(byte) 0x03};//登录指令
    public byte[] cmdOpen = {(byte) 0x04};//开锁指令
    public byte[] cmdDataAcquisition = {(byte) 0x05};//数据获取
    public byte[] cmdPasswordModify = {(byte) 0x06};//密码修改
    public byte[] cmdPasswordAdd = {(byte) 0x07};//密码增加
    public byte[] cmdPasswordDel = {(byte) 0x08};//密码删除
    public byte[] cmdFingerAdd = {(byte) 0x09};//指纹添加
    public byte[] cmdFingerDel = {(byte) 0x0A};//指纹删除
    public byte[] cmdCardAdd = {(byte) 0x0B};//卡片添加
    public byte[] cmdCardDel = {(byte) 0x0C};//卡片删除
    public byte[] cmdResumeFactory = {(byte) 0x0D};//清空数据，恢复出厂
    public byte[] cmdModulation = {(byte) 0x0E};//音量调节
    public byte[] cmdTimeSync = {(byte) 0x0F};//时间同步
    public byte[] cmdTermination = {(byte) 0x11};//指令终止
    public byte[] cmdHeart = {(byte) 0x12};//心跳包
    public byte[] cmdQuit = {(byte) 0x13};//退出登录
    public byte[] cmdObtainFinger = {(byte) 0x14};//获取指纹数据
    public byte[] cmdObtainPassword = {(byte) 0x15};//获取密码数据
    public byte[] getCmdObtainCard = {(byte) 0x16};//获取卡片数据

    public byte[] none = {(byte) 0x00};//0
    public byte[] patch = {(byte) 0xFF};//补位
    public byte[] noNum = {(byte) 0x84};

    /**
     * 固件版本查询
     *
     * @return
     */
    public byte[] firmwareQuery() {
        byte[] requestData = null;
        requestData = HexUtil.byteArrayCopy(null, start);
        requestData = HexUtil.byteArrayCopy(requestData, cmdFirmwareQuery);
        requestData = HexUtil.byteArrayCopy(requestData, none);
        requestData = HexUtil.byteArrayCopy(requestData, finish);
        Log.e("firmwareQuery:>>>>>", HexUtil.formatHexString(requestData, true));
        return requestData;
    }

    /**
     * 注册指令
     *
     * @return
     */
    public byte[] registrationByPassword(String pwd) {
        byte[] requestData = null;
        byte[] data = null;
        byte[] password = HexUtil.justToByte(pwd);
        byte[] passwordLen = HexUtil.intTobyte2(password.length);
        while (password.length < 11){
            password = HexUtil.byteArrayCopy(password, patch);
        }
        requestData = HexUtil.byteArrayCopy(null, start);
        requestData = HexUtil.byteArrayCopy(requestData, cmdRegistrationDir);

        data = HexUtil.byteArrayCopy(data, none);
        data = HexUtil.byteArrayCopy(data, passwordLen);
        data = HexUtil.byteArrayCopy(data, password);

        byte[] len = HexUtil.intTobyte2(data.length);
        requestData = HexUtil.byteArrayCopy(requestData, len);
        requestData = HexUtil.byteArrayCopy(requestData, data);
        requestData = HexUtil.byteArrayCopy(requestData, finish);
        Log.e("registrationByPwd:>>>>>", HexUtil.formatHexString(requestData, true));
        return requestData;
    }

    /**
     * 注册指令
     *
     * @return
     */
    public byte[] registrationByKey() {
        byte[] requestData = null;
        requestData = HexUtil.byteArrayCopy(null, start);
        requestData = HexUtil.byteArrayCopy(requestData, cmdRegistrationDir);
        requestData = HexUtil.byteArrayCopy(requestData, none);
        requestData = HexUtil.byteArrayCopy(requestData, finish);
        Log.e("registrationByKey:>>>>>", HexUtil.formatHexString(requestData, true));
        return requestData;
    }

    public byte[] loginDir(){
        byte[] requestData = null;
        requestData = HexUtil.byteArrayCopy(null, start);
        requestData = HexUtil.byteArrayCopy(requestData, cmdRegistrationDir);
        requestData = HexUtil.byteArrayCopy(requestData, none);
        requestData = HexUtil.byteArrayCopy(requestData, finish);
        Log.e("loginDir:>>>>>", HexUtil.formatHexString(requestData, true));
        return requestData;
    }

    /**
     * 开门指令
     * @return
     */
    public byte[] openLock(){
        byte[] requestData = null;
        requestData = HexUtil.byteArrayCopy(null, start);
        requestData = HexUtil.byteArrayCopy(requestData, cmdOpen);
        requestData = HexUtil.byteArrayCopy(requestData, none);
        requestData = HexUtil.byteArrayCopy(requestData, finish);
        Log.e("openLock:>>>>>", HexUtil.formatHexString(requestData, true));
        return requestData;
    }

    /**
     * 数据获取
     * @return
     */
    public byte[] dataAcquisition(){
        byte[] requestData = null;
        requestData = HexUtil.byteArrayCopy(null, start);
        requestData = HexUtil.byteArrayCopy(requestData, cmdDataAcquisition);
        requestData = HexUtil.byteArrayCopy(requestData, none);
        requestData = HexUtil.byteArrayCopy(requestData, finish);
        Log.e("dataAcquisition:>>>>>", HexUtil.formatHexString(requestData, true));
        return requestData;
    }

    /**
     * 添加指纹
     * @param id 指纹编号
     * @return
     */
    public byte[] fingerAdd(int id){
        byte[] requestData = null;
        requestData = HexUtil.byteArrayCopy(null, start);
        requestData = HexUtil.byteArrayCopy(requestData, cmdFingerAdd);
        byte[] fingerId = HexUtil.intTobyte2(id);
        byte[] len = HexUtil.intTobyte2(fingerId.length);
        requestData = HexUtil.byteArrayCopy(requestData, len);
        requestData = HexUtil.byteArrayCopy(requestData, fingerId);
        requestData = HexUtil.byteArrayCopy(requestData, finish);
        Log.e("fingerAdd:>>>>>", HexUtil.formatHexString(requestData, true));
        return requestData;
    }

    /**
     * 删除指纹
     * @param id 指纹编号
     * @return
     */
    public byte[] fingerDel(int id){
        byte[] requestData = null;
        requestData = HexUtil.byteArrayCopy(null, start);
        requestData = HexUtil.byteArrayCopy(requestData, cmdFingerDel);
        byte[] fingerId = HexUtil.intTobyte2(id);
        byte[] len = HexUtil.intTobyte2(fingerId.length);
        requestData = HexUtil.byteArrayCopy(requestData, len);
        requestData = HexUtil.byteArrayCopy(requestData, fingerId);
        requestData = HexUtil.byteArrayCopy(requestData, finish);
        Log.e("fingerDel:>>>>>", HexUtil.formatHexString(requestData, true));
        return requestData;
    }

    /**
     * 音量调节
     * @param index 0x00静音 0x01低音 0x02中音  0x03高音
     * @return
     */
    public byte[] modulation(int index){
        byte[] requestData = null;
        requestData = HexUtil.byteArrayCopy(null, start);
        requestData = HexUtil.byteArrayCopy(requestData, cmdModulation);
        byte[] volume = HexUtil.intTobyte2(index);
        byte[] len = HexUtil.intTobyte2(volume.length);
        requestData = HexUtil.byteArrayCopy(requestData, len);
        requestData = HexUtil.byteArrayCopy(requestData, volume);
        requestData = HexUtil.byteArrayCopy(requestData, finish);
        Log.e("modulation:>>>>>", HexUtil.formatHexString(requestData, true));
        return requestData;
    }

    /**
     * 获取指纹数据
     * @return
     */
    public byte[] obtainFinger(){
        byte[] requestData = null;
        requestData = HexUtil.byteArrayCopy(null, start);
        requestData = HexUtil.byteArrayCopy(requestData, cmdObtainFinger);
        requestData = HexUtil.byteArrayCopy(requestData, none);
        requestData = HexUtil.byteArrayCopy(requestData, finish);
        Log.e("obtainFinger:>>>>>", HexUtil.formatHexString(requestData, true));
        return requestData;
    }

    /**
     * 心跳包
     *
     * @return
     */
    public byte[] heartBeat() {
        byte[] requestData = null;
        requestData = HexUtil.byteArrayCopy(requestData, start);
        requestData = HexUtil.byteArrayCopy(requestData, cmdHeart);
        requestData = HexUtil.byteArrayCopy(requestData, none);
        requestData = HexUtil.byteArrayCopy(requestData, finish);
        Log.e("heartBeat:>>>>>", HexUtil.formatHexString(requestData, true));
        return requestData;
    }

}
