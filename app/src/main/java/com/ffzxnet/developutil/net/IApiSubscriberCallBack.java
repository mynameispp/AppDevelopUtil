package com.ffzxnet.developutil.net;


/**
 * 创建者： feifan.pi 在 2017/3/14.
 */

public interface IApiSubscriberCallBack<T> {
    //请求结束
    void onCompleted();
    //请求失败
    void onError(ErrorResponse error);
    //请求成功
    void onNext(T t);
}
