package com.ffzxnet.developutil.net.retrofit_gson_factory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * 判断json字段为null
 * 创建者： feifan.pi 在 2017/3/14.
 */

public final class NullStringToEmptyAdapterFactory implements TypeAdapterFactory {
    public TypeAdapter create(Gson gson, TypeToken type) {
        Class rawType = (Class) type.getRawType();
        if (rawType != String.class) {
            return null;
        }
        return (TypeAdapter) new StringNullAdapter();
    }
}
