package com.ffzxnet.developutil.net;


import com.ffzxnet.developutil.base.net.BaseApiResultData;
import com.ffzxnet.developutil.bean.LoginRequestBean;
import com.ffzxnet.developutil.bean.UserInfoBean;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
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

    @POST("card/class/login/admin")
    Observable<BaseApiResultData<UserInfoBean>> login(@Body LoginRequestBean requestBean);

}

