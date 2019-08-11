package so.voc.vhome.okgo;

import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.ui.activity.LoginActivity;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.Convert;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/5/1 8:59
 */
public class JsonConvert<T> implements Converter<T> {

    private Type type;
    private Class<T> clazz;

    public JsonConvert() {
    }

    public JsonConvert(Type type) {
        this.type = type;
    }

    public JsonConvert(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象，生成onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {
        if (type == null) {
            if (clazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                return parseClass(response, clazz);
            }
        }

        if (type instanceof ParameterizedType) {
            return parseParameterizedType(response, (ParameterizedType) type);
        } else if (type instanceof Class) {
            return parseClass(response, (Class<?>) type);
        } else {
            return parseType(response, type);
        }
    }

    private T parseClass(Response response, Class<?> rawType) throws Exception {
        if (rawType == null) {
            return null;
        }

        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }
        JsonReader jsonReader = new JsonReader(body.charStream());
        if (response.code() == 200) {
            if (rawType == String.class) {
                return (T) body.string();
            } else if (rawType == JSONObject.class) {
                return (T) new JSONObject(body.string());
            } else if (rawType == JSONArray.class) {
                return (T) new JSONArray(body.string());
            } else {
                T t = Convert.fromJson(jsonReader, rawType);
                response.close();
                return t;
            }
        } else {
            ResponseModel responseModel = Convert.fromJson(jsonReader, type);
            throw new IllegalStateException(responseModel.msg);
        }
    }

    private T parseType(Response response, Type type) throws Exception {
        if (type == null) {
            return null;
        }
        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }
        JsonReader jsonReader = new JsonReader(body.charStream());

        T t = Convert.fromJson(jsonReader, type);
        response.close();
        return t;
    }

    private T parseParameterizedType(Response response, ParameterizedType type) throws Exception {
        if (type == null) {
            return null;
        }
        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }
        JsonReader jsonReader = new JsonReader(body.charStream());
        response.close();
        if (response.code() == 200) {
            //泛型的实际类型
            Type rawType = type.getRawType();
            //泛型的参数
            Type typeArgument = type.getActualTypeArguments()[0];
            if (rawType != ResponseModel.class) {
                // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
                T t = Convert.fromJson(jsonReader, type);
                return t;
            } else {
                // 泛型格式如下： new JsonCallback<LzyResponse<内层JavaBean>>(this)
                ResponseModel responseModel = Convert.fromJson(jsonReader, type);
                int code = responseModel.code;
                //if (!code.isEmpty()) {
                return (T) responseModel;
//                }  else {
//                    //直接将服务端的错误信息抛出，onError中可以获取
//                    throw new IllegalStateException("错误代码：" + code + "，错误信息：" + responseModel.content);
//                }
            }
        } else {
            ResponseModel responseModel = Convert.fromJson(jsonReader, type);
            throw new IllegalStateException(responseModel.msg);
            //return (T) responseModel;
        }
    }
}
