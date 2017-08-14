package com.ffzxnet.developutil.net;



import com.ffzxnet.developutil.base.net.BaseApiResultData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 创建者： feifan.pi 在 2017/2/20.
 */

public interface ApiService {
    //登录
    @FormUrlEncoded
    @POST("appUser/login.do")
    Observable<BaseApiResultData<String>> login(@Field("params") String params);

}

