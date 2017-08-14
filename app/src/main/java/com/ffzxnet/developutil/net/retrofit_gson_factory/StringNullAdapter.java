package com.ffzxnet.developutil.net.retrofit_gson_factory;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * 处理json字段为null
 * 创建者： feifan.pi 在 2017/3/14.
 */

public final class StringNullAdapter extends TypeAdapter<String> {

    @Override
    public String read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return "";
        }
        return reader.nextString();
    }

    @Override
    public void write(JsonWriter writer, String value) throws IOException {
        if (TextUtils.isEmpty(value)) {
            writer.value("");
            return;
        }
        writer.value(value);
    }
}
