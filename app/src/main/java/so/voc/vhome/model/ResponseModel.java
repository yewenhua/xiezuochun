package so.voc.vhome.model;

import java.io.Serializable;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/4/20 15:23
 */
public class ResponseModel<T> implements Serializable {


    /**
     * code : 301
     * message : 参数不能为空
     * data : null
     */

    public int code;
    public String msg;
    public T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseModel{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
