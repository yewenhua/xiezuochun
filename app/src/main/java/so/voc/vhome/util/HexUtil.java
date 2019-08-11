package so.voc.vhome.util;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/11 18:24
 */
public class HexUtil {

    /**
     * byte——>String
     *
     * @param src
     * @return
     */
    public static final String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String formatHexString(byte[] data) {
        return formatHexString(data, false);
    }

    public static String formatHexString(byte[] data, boolean addSpace) {
        if (data == null || data.length < 1) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(data[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
            if (addSpace) {
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    /**
     * 若都不为空，将arrSrc2添加到arrSrc1的末尾组合成新的byte数组
     *
     * @param arrSrc1
     * @param arrSrc2
     * @return
     */
    public static final byte[] byteArrayCopy(byte[] arrSrc1, byte[] arrSrc2) {
        byte[] arrDes = null;
        if (arrSrc2 == null) {
            arrDes = arrSrc1;
            return arrDes;
        }

        if (arrSrc1 != null) {
            arrDes = new byte[arrSrc1.length + arrSrc2.length];
            System.arraycopy(arrSrc1, 0, arrDes, 0, arrSrc1.length);
            System.arraycopy(arrSrc2, 0, arrDes, arrSrc1.length, arrSrc2.length);
        } else {
            arrDes = new byte[arrSrc2.length];
            System.arraycopy(arrSrc2, 0, arrDes, 0, arrSrc2.length);
        }

        return arrDes;
    }

    /**
     * 数组截取：从arrData的offset开始获取len个长度的byte生成的len的byte[]
     *
     * @param arrData
     * @param offset
     * @param len
     * @return
     */
    public static final byte[] getByteArrayByLength(byte[] arrData, int offset, int len) {
        byte[] newData = new byte[len];
        try {
            for (int i = offset; i < offset + len; i++) {
                newData[i - offset] = arrData[i];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newData;
    }

    /**
     * 判断两个byte数组是否相等
     *
     * @param myByte
     * @param otherByte
     * @return
     */
    public static final boolean checkByte(byte[] myByte, byte[] otherByte) {
        boolean b = false;
        int len = myByte.length;
        if (len == otherByte.length) {
            for (int i = 0; i < len; i++) {
                if (myByte[i] != otherByte[i]) {
                    return b;
                }
            }
            b = true;
        }
        return b;
    }

    public static final byte[] intTobyte2(int s) {
        byte[] shortBuf = new byte[1];
        shortBuf[0] = (byte) (s & 0xff);
        return shortBuf;
    }

    /**
     * 123456789 ---> 0x01 0x02 0x03 0x04 0x05 0x06 0x07 0x08
     * @param str
     * @return
     */
    public static final byte[] justToByte(String str){
        byte[] ret = new byte[str.length()];
        for (int i = 0; i < str.length(); i++) {
            ret[i] = (byte) Integer.parseInt(str.substring(i, i + 1), 16);
        }
        return ret;
    }
}
