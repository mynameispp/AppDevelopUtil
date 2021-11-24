package com.ffzxnet.developutil.utils.tools;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * 创建者： feifan.pi 在 2016/9/6.
 */
public class GsonUtil {
    private static final Gson gson = new Gson();

    /**
     * 对象转成json
     *
     * @param o
     * @return
     */
    public static String toJson(final Object o) {
        return gson.toJson(o);
    }

    /**
     * json转成对象
     *
     * @param s json
     * @param c Object.class
     * @return
     */
    public static Object toClass(final String s, final Class c) {
        try {
            return gson.fromJson(s, c);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json转成对象
     *
     * @param s    json
     * @param type Type  Ex: new TypeToken<T>() {}.getType()
     * @return
     */
    public static <T> T toClass(final String s, final Type type) {
        try {
            return gson.fromJson(s, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
