package com.ffzxnet.developutil.base.mvp;


import com.ffzxnet.developutil.net.ErrorResponse;

/**
 * 创建者： Pi 在 2018/6/25.
 * 注释：
 */

public interface BaseNetView {
    //检测请求的的错误代码（这里只做app关键错误代码，如Token失效时同一处理）
    void catchApiSubscriberError(ErrorResponse error);
}
