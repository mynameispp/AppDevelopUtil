package com.ffzxnet.developutil.net.retrofit_gson_factory;

import com.ffzxnet.developutil.utils.tools.LogUtil;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;

/**
 * 创建者： feifan.pi 在 2017/3/14.
 */

final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        //服务器返回的数据
        String response = value.string();
        int code = 0;
        try {
            //查看是不是错误json
            JSONObject jsonObject = new JSONObject(response);
            //这里可以根据判断来修改json数据或格式
            code = jsonObject.getInt("code");
        } catch (JSONException e) {
            LogUtil.e("CustomGsonResponseBodyConverter", e.toString());
            throw new ResultErrorException("数据解析错误" + response);
        }

//        //服务端返回失败的code，可以在这里处理，也可以给ApiSubscriber onNext()方法处理
//        if (code < 0 || code == 1) {
//            //如果是错误的json 数据抛出异常,进入ApiSubscriber onError（）回调
//            throw new ResultErrorException(response);
//        }

        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
