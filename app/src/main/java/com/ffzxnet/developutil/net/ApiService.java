package com.ffzxnet.developutil.net;


import com.ffzxnet.developutil.base.net.BaseApiResultData;
import com.ffzxnet.developutil.bean.LoginRequestBean;
import com.ffzxnet.developutil.bean.UserInfoBean;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 创建者： feifan.pi 在 2017/2/20.
 */

public interface ApiService {
    //请求服务器地址统一，表单形式传参
    @FormUrlEncoded
    @POST("appUser/login.do")
    Observable<BaseApiResultData<String>> login(@Field("params") String params);

    //请求服务器地址统一， Body形式传参
    @POST("card/class/login/admin")
    Observable<BaseApiResultData<UserInfoBean>> login(@Body LoginRequestBean requestBean);

    //Url服务器不统一请求
    @GET()
    Observable<BaseApiResultData> getConfigBean(@Url String url, @QueryMap Map<String,Object> params);

    //DELETE请求方式，使用body传参和请求头
    @HTTP(method = "DELETE",hasBody = true)
    Observable<BaseApiResultData> deleteData(@Url String url, @Header("token") String token, @Body RequestBody channel);

    //下载文件
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);

    //上傳头像
    @Multipart
    @POST()
    Observable<BaseApiResultData<String>> upLoadFile(@Url String url, @Part MultipartBody.Part part);
}

